package atmClient.result;

public class WithdrawResult extends SessionResult {

    private String withdrawMsg = "";

    public WithdrawResult(int sessionStatus, int status) {
        super(sessionStatus, status);
    }

    public WithdrawResult(int sessionStatus, int status, String message) {
        super(sessionStatus, status, message);
    }

    public WithdrawResult(int sessionStatus, String sessionMessage, int status) {
        super(sessionStatus, sessionMessage, status);
    }

    public WithdrawResult(int sessionStatus, String sessionMessage, int status, String message) {
        super(sessionStatus, sessionMessage, status, message);
    }

    public String getWithdrawMsg() {
        return withdrawMsg;
    }

    public void setWithdrawMsg(String withdrawMsg) {
        this.withdrawMsg = withdrawMsg;
    }
}
