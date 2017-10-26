package atmClient.result;

import atmClient.result.Result;

public class SessionResult extends Result {

    public static final int SUCCESS_CODE = 1;
    public static final int ERROR_CODE = 0;
    public static final int INVALID_SESSION_CODE = -1;
    public static final int EXPIRED_SESSION_CODE = -2;

    public SessionResult(int sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

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
