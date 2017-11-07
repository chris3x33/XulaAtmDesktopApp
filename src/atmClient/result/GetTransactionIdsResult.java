package atmClient.result;

import java.util.ArrayList;

public class GetTransactionIdsResult extends SessionResult {

    private ArrayList<Long> transactionIds;

    public GetTransactionIdsResult(
            int sessionStatus, int status, ArrayList<Long> transactionIds) {

        super(sessionStatus, status);

        this.transactionIds = transactionIds;
    }

    public GetTransactionIdsResult(int sessionStatus, int status, String message) {
        super(sessionStatus, status, message);
    }

    public GetTransactionIdsResult(int sessionStatus, String sessionMessage, int status) {
        super(sessionStatus, sessionMessage, status);
    }

    public GetTransactionIdsResult(int sessionStatus, String sessionMessage, int status, String message) {
        super(sessionStatus, sessionMessage, status, message);
    }

    public ArrayList<Long> getTransactionIds() {
        return transactionIds;
    }


}
