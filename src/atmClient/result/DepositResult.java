package atmClient.result;

public class DepositResult extends SessionResult {

    private String depositMsg = "";

    public DepositResult(int sessionStatus, int status) {
        super(sessionStatus, status);
    }

    public DepositResult(int sessionStatus, int status, String message) {
        super(sessionStatus, status, message);
    }

    public DepositResult(int sessionStatus, String sessionMessage, int status) {
        super(sessionStatus, sessionMessage, status);
    }

    public DepositResult(int sessionStatus, String sessionMessage, int status, String message) {
        super(sessionStatus, sessionMessage, status, message);
    }

    public String getDepositMsg() {
        return depositMsg;
    }

    public void setDepositMsg(String depositMsg) {
        this.depositMsg = depositMsg;
    }
}
