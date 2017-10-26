package atmClient.result;

public class CreateNewUserResult extends SessionResult {

    public CreateNewUserResult(int sessionStatus, int status) {
        super(sessionStatus, status);
    }

    public CreateNewUserResult(int sessionStatus, int status, String message) {
        super(sessionStatus, status, message);
    }

    public CreateNewUserResult(int sessionStatus, String sessionMessage, int status) {
        super(sessionStatus, sessionMessage, status);
    }

    public CreateNewUserResult(int sessionStatus, String sessionMessage, int status, String message) {
        super(sessionStatus, sessionMessage, status, message);
    }

}
