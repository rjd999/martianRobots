package mars.communications;

public interface StatusReporter {
    void reportFault(final String reporterId,
                     final String faultReport);

    void reportStatus(final String reporterId,
                      final String faultReport);
}
