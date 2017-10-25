package atmClient;

import java.util.ArrayList;

public class GetAccountBalanceResult extends SessionResult {

    private double accountBalance;

    public GetAccountBalanceResult(
            int sessionStatus, int status, double accountBalance) {

        super(sessionStatus, status);

        this.accountBalance = accountBalance;
    }

    public GetAccountBalanceResult(int sessionStatus, int status, String message) {
        super(sessionStatus, status, message);
    }

    public GetAccountBalanceResult(int sessionStatus, String sessionMessage, int status) {
        super(sessionStatus, sessionMessage, status);
    }

    public GetAccountBalanceResult(int sessionStatus, String sessionMessage, int status, String message) {
        super(sessionStatus, sessionMessage, status, message);
    }

    public double getAccountBalance() {
        return accountBalance;
    }
}
