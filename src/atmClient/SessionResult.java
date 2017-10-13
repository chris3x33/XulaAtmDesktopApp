package atmClient;

public class SessionResult extends Result{

    private int sessionStatus;

    public SessionResult(int sessionStatus,int status) {
        super(status);
        this.sessionStatus = sessionStatus;

    }

    public SessionResult(int sessionStatus, int status, String message) {
        super(status, message);
        this.sessionStatus = sessionStatus;
    }

}
