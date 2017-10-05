package atmClient;

public class Result {

    private int status;// 0 for fail, 1 for success
    private String message;

    public Result() {

        // sets status to 1 for a success
        this.status = 1;

        this.message = "";

    }

    public Result(int status) {

        // sets status
        this.status = status;

        this.message = "";

    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}