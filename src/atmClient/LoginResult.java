package atmClient;

public class LoginResult extends SessionResult {

    public LoginResult(int sessionStatus, int status) {
        super(sessionStatus, status);
    }

    public LoginResult(int sessionStatus, int status, String message) {
        super(sessionStatus, status, message);
    }

    public LoginResult(int sessionStatus, String sessionMessage, int status) {
        super(sessionStatus, sessionMessage, status);
    }

    public LoginResult(int sessionStatus, String sessionMessage, int status, String message) {
        super(sessionStatus, sessionMessage, status, message);
    }
}
