package atmClient;

public class GetUserNameResult extends SessionResult {

    private String userName = "";

    public GetUserNameResult(int sessionStatus, int status) {
        super(sessionStatus, status);
    }

    public GetUserNameResult(int sessionStatus, int status, String message) {
        super(sessionStatus, status, message);
    }

    public GetUserNameResult(int sessionStatus, String sessionMessage, int status) {
        super(sessionStatus, sessionMessage, status);
    }

    public GetUserNameResult(int sessionStatus, String sessionMessage, int status, String message) {
        super(sessionStatus, sessionMessage, status, message);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
