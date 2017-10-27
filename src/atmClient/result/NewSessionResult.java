package atmClient.result;

public class NewSessionResult extends Result{

    private long sessionId = -1;

    public NewSessionResult(int status, long sessionId) {
        super(status);
        this.sessionId = sessionId;
    }

    public NewSessionResult(int status, String message) {
        super(status, message);
    }

    public long getSessionId() {
        return sessionId;
    }

}
