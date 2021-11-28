package mars.communications;

public interface MessageReceiver {
    boolean hasIncoming();

    String receiveMessage();
}
