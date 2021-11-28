package mars.communications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

@Component
public class MarsComms implements MessageReceiver,
                                  MessageTransmitter,
                                  StatusReporter {
    private final BufferedReader incoming;

    private String nextMessage = null;

    @Autowired
    public MarsComms(@Value("${mars.commandFile}") final String commandFileName) {
        InputStream stream = getClass().getResourceAsStream(commandFileName);

        if (stream == null) {
            File file = new File(commandFileName);

            if (file.exists())
                try {
                    stream = new FileInputStream(file);
                }
                catch (final Exception exception) {
                }
        }

        incoming = new BufferedReader(new InputStreamReader(Objects.requireNonNull(stream)));
    }

    @Override
    public boolean hasIncoming() {
        boolean hasIncoming = false;

        if (nextMessage != null)
            hasIncoming = true;
        else
            try {
                while ((nextMessage == null) && (incoming.ready())) {
                    nextMessage = receiveMessage();

                    if ((nextMessage != null) && (!nextMessage.isEmpty()))
                        hasIncoming = true;
                    else
                        nextMessage = null;                     // empty string is treated as radio static and ignored
                }
            }
            catch (final IOException ioException) {
                reportFault(MarsComms.class.getSimpleName(), "communications receipt failure: " + ioException.getMessage());
            }

        return hasIncoming;
    }

    @Override
    public String receiveMessage() {
        String message;

        if (nextMessage != null) {
            message     = nextMessage;
            nextMessage = null;
        }
        else
            try {
                message = incoming.readLine();
            }
            catch (final IOException ioException) {
                reportFault(MarsComms.class.getSimpleName(), "communications receipt failure: " + ioException.getMessage());
                message = null;
            }

        return message;
    }

    @Override
    public void transmitMessage(final String message) {
        System.out.println(message);
    }

    @Override
    public void reportFault(final String reporterId,
                            final String faultReport) {
        // TODO: no current specification for this operation... simply provided as a way to handle a situation
    }

    @Override
    public void reportStatus(final String reporterId,
                             final String faultReport) {
        // TODO: no current specification for this operation... simply provided as a way to handle a situation
    }
}
