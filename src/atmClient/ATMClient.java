package atmClient;

import atmClient.result.*;

import java.net.*;

import static atmClient.handler.CreateNewUserHandler.handleCreateNewUser;
import static atmClient.handler.GetAccountBalanceHandler.handleGetAccountBalance;
import static atmClient.handler.GetAccountIdsHandler.handleGetAccountIds;
import static atmClient.handler.GetUserNameHandler.handleGetUserName;
import static atmClient.handler.LoginHandler.handleLogin;
import static atmClient.handler.LogoutHandler.handleLogout;
import static atmClient.handler.NewSessionHandler.handleNewSession;

public class ATMClient {

    private long sessionId = -1;
    private long selectedAccountId = -1;

    private final String DEFAULT_IP_ADDRESS = "127.0.0.1";
    private final int DEFAULT_PORT = 55555;
    private final int DEFAULT_TIMEOUT = 10000;

    private String userDefinedIpAddress = null;
    private int userDefinedPort = -1;
    private int userDefinedTimeOut = -1;

    private final int ACK_CODE = SocketACK.ACK_CODE;

    public Result connect() {

        String ipAddress = getIpAddress();
        int port = getPort();
        int timeOut = getTimeOut();

        //reset sessionId if set
        sessionId = -1;

        NewSessionResult newSessionResult = handleNewSession(
                ipAddress,
                port,
                timeOut,
                ACK_CODE
        );

        if (newSessionResult.getStatus() == Result.SUCCESS_CODE){
            this.sessionId = newSessionResult.getSessionId();
        }

        return newSessionResult;

    }

    public int getTimeOut() {
        if (userDefinedTimeOut > -1) {
            return userDefinedTimeOut;
        } else {
            return DEFAULT_TIMEOUT;
        }

    }

    public int getPort() {

        if (userDefinedPort > -1) {
            return userDefinedPort;
        } else {
            return DEFAULT_PORT;
        }

    }


    public String getIpAddress() {

        if (userDefinedIpAddress != null) {

            return userDefinedIpAddress;

        } else {

            return DEFAULT_IP_ADDRESS;

        }

    }

    public LoginResult login(String userName, String password) {

        String ipAddress = getIpAddress();
        int port = getPort();
        int timeOut = getTimeOut();

        return handleLogin(
                ipAddress, port, timeOut, ACK_CODE,
                this.sessionId,
                userName, password
        );

    }

    public CreateNewUserResult createNewUser(String userName, String password) {

        String ipAddress = getIpAddress();
        int port = getPort();
        int timeOut = getTimeOut();

        return handleCreateNewUser(
                ipAddress, port, timeOut, ACK_CODE,
                this.sessionId,
                userName, password
        );

    }

    public LogOutResult logout(){

        String ipAddress = getIpAddress();
        int port = getPort();
        int timeOut = getTimeOut();

        return handleLogout(ipAddress, port, timeOut, ACK_CODE, this.sessionId);

    }

    public Result setConnection(String ipAddress, int port, int timeOut) {

        // check ipAddress
        if (!ipAddressExists(ipAddress)) {
            String errMsg = "IP Address Not Found!!";

            return new Result(Result.ERROR_CODE, errMsg);

        }

        // check port
        if (port <= -1) {
            String errMsg = "Port Must Be Greater than -1 !!";

            return new Result(Result.ERROR_CODE, errMsg);
        }

        // check timeOut
        if (timeOut <= -1) {
            String errMsg = "Timeout Must Be Greater than -1 !!";

            return new Result(Result.ERROR_CODE, errMsg);
        }

        userDefinedIpAddress = ipAddress;
        userDefinedPort = port;
        userDefinedTimeOut = timeOut;

        return new Result(Result.SUCCESS_CODE);

    }

    public GetUserNameResult getUserName(){

        String ipAddress = getIpAddress();
        int port = getPort();
        int timeOut = getTimeOut();

        return handleGetUserName(ipAddress, port, timeOut, ACK_CODE, sessionId);

    }

    public GetAccountIdsResult getAccountIds(){

        String ipAddress = getIpAddress();
        int port = getPort();
        int timeOut = getTimeOut();

        return handleGetAccountIds(
                ipAddress, port, timeOut,
                ACK_CODE,
                sessionId
        );
    }

    public GetAccountBalanceResult getAccountBalance(long accountId){

        String ipAddress = getIpAddress();
        int port = getPort();
        int timeOut = getTimeOut();

        return handleGetAccountBalance(
                ipAddress, port, timeOut,ACK_CODE,
                sessionId,
                accountId
        );
    }

    public boolean ipAddressExists(String ipAddress) {

        try {

            InetAddress.getByName(ipAddress);

            return true;

        } catch (UnknownHostException e) {
            return false;
        }

    }

    public long getSelectedAccountId() {
        return selectedAccountId;
    }

    public void setSelectedAccountId(long selectedAccountId) {
        this.selectedAccountId = selectedAccountId;
    }

}