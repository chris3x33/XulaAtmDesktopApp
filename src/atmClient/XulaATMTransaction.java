package atmClient;

public class XulaATMTransaction {

    private long accountId;
    private long transactionId;
    private double amount;
    private int type;// W , D
    private String otherAccount;
    private String dateTime;
    private double prevAmount;
    private long transactionIndex;

    public XulaATMTransaction(
            long transactionIndex, long accountId, long transactionId,
            double amount, int type, String otherAccount, double prevAmount,
            String dateTime) {

        this.transactionIndex = transactionIndex;
        this.accountId = accountId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = type;
        this.otherAccount = otherAccount;
        this.prevAmount = prevAmount;
        this.dateTime = dateTime;
    }

    public double getAmount() {
        return amount;
    }

    public int getType() {
        return type;
    }

    public String getOtherAccount() {
        return otherAccount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public double getPrevAmount() {
        return prevAmount;
    }

    public long getAccountId() {
        return accountId;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public long getTransactionIndex() {
        return transactionIndex;
    }
}
