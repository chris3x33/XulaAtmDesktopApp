package atmClient;

public class SessionResult extends Result{

    public int getSessionStatus() {
        return sessionStatus;
    }

    public String getSessionMessage() {
        return sessionMessage;
    }

    private int sessionStatus;
    private String sessionMessage;
    public SessionResult(int sessionStatus,int status) {
        super(status);
        this.sessionStatus = sessionStatus;

    }

    public SessionResult(int sessionStatus, int status, String message) {
        super(status, message);
        this.sessionStatus = sessionStatus;
    }

    public SessionResult(int sessionStatus,String sessionMessage, int status) {
        super(status);
        this.sessionStatus = sessionStatus;
        this.sessionMessage = sessionMessage;

    }

    public SessionResult(int sessionStatus, String sessionMessage, int status, String message) {
        super(status, message);
        this.sessionStatus = sessionStatus;
        this.sessionMessage = sessionMessage;
    }

}
