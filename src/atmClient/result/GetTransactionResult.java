package atmClient.result;

import atmClient.XulaATMTransaction;

import java.util.ArrayList;

public class GetTransactionResult extends SessionResult {

    private XulaATMTransaction atmTransaction;

    public GetTransactionResult(
            int sessionStatus, int status, XulaATMTransaction atmTransaction) {

        super(sessionStatus, status);

        this.atmTransaction = atmTransaction;
    }

    public GetTransactionResult(int sessionStatus, int status, String message) {
        super(sessionStatus, status, message);
    }

    public GetTransactionResult(int sessionStatus, String sessionMessage, int status) {
        super(sessionStatus, sessionMessage, status);
    }

    public GetTransactionResult(int sessionStatus, String sessionMessage, int status, String message) {
        super(sessionStatus, sessionMessage, status, message);
    }

    public XulaATMTransaction getAtmTransaction() {
        return atmTransaction;
    }
}
