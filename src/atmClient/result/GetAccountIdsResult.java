package atmClient.result;

import java.util.ArrayList;

public class GetAccountIdsResult extends SessionResult {

    private ArrayList<Long> accountIds;

    public GetAccountIdsResult(
            int sessionStatus, int status, ArrayList<Long> accountIds) {

        super(sessionStatus, status);

        this.accountIds = accountIds;
    }

    public GetAccountIdsResult(int sessionStatus, int status, String message) {
        super(sessionStatus, status, message);
    }

    public GetAccountIdsResult(int sessionStatus, String sessionMessage, int status) {
        super(sessionStatus, sessionMessage, status);
    }

    public GetAccountIdsResult(int sessionStatus, String sessionMessage, int status, String message) {
        super(sessionStatus, sessionMessage, status, message);
    }

    public ArrayList<Long> getAccountIds() {
        return accountIds;
    }


}
