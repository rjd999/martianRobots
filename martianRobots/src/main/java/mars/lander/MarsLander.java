package mars.lander;

import mars.communications.MessageReceiver;
import mars.communications.StatusReporter;
import mars.grid.Coordinates;
import mars.grid.GridControl;
import mars.robot.Robot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MarsLander implements Lander {
    private final GridControl        grid;
    private final MessageReceiver    receiver;
    private final StatusReporter     reporter;
    private final ApplicationContext applicationContext;

    @Autowired
    public MarsLander(final MessageReceiver    receiver,
                      final StatusReporter     reporter,
                      final GridControl        gridControl,
                      final ApplicationContext applicationContext) {
        this.grid               = gridControl;
        this.receiver           = receiver;
        this.reporter           = reporter;
        this.applicationContext = applicationContext;
    }

    @Override
    public void coordinateExploration() {
        Coordinates upperRight = getGridUpperRight();

        if (upperRight == null)
            reporter.reportStatus(getClass().getSimpleName(), "no valid grid sizing available: lander shutting down");
        else if (grid.initialise(upperRight))
            while (receiver.hasIncoming()) {
                Robot robot = applicationContext.getBean(Robot.class);

                robot.explore();
            }
    }

    private Coordinates getGridUpperRight() {
        Coordinates upperRight = null;

        if (receiver.hasIncoming()) {
            String   message  = receiver.receiveMessage();
            String[] elements = message.split("\\s+");

            if (elements.length != 2)
                reporter.reportFault(getClass().getSimpleName(), "invalid grid size message received: '" + message + "'");
            else
                try {
                    upperRight = new Coordinates(elements[0], elements[1]);
                }
                catch (final Exception exception) {
                    reporter.reportFault(getClass().getSimpleName(), "invalid coordinates provided for grid size: '" + message + "'");
                }
        }

        return upperRight;
    }
}
