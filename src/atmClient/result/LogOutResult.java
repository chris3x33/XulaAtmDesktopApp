package atmClient.result;

public class LogOutResult extends SessionResult {

    public LogOutResult(int sessionStatus, int status) {
        super(sessionStatus, status);
    }

    public LogOutResult(int sessionStatus, int status, String message) {
        super(sessionStatus, status, message);
    }

    public LogOutResult(int sessionStatus, String sessionMessage, int status) {
        super(sessionStatus, sessionMessage, status);
    }

    public LogOutResult(int sessionStatus, String sessionMessage, int status, String message) {
        super(sessionStatus, sessionMessage, status, message);
    }
}
