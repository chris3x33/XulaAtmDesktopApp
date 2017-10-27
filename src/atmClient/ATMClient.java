package atmClient;

import atmClient.handler.SessionHandler;
import atmClient.result.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

import static atmClient.SocketACK.readACK;
import static atmClient.SocketACK.sendACK;
import static atmClient.handler.CommandHandler.sendCommand;
import static atmClient.handler.CreateNewUserHandler.handleCreateNewUser;
import static atmClient.handler.LoginHandler.handleLogin;
import static atmClient.handler.LogoutHandler.handleLogout;
import static atmClient.handler.NewSessionHandler.handleNewSession;
import static atmClient.handler.ResultHandler.getResult;
import static atmClient.handler.SessionHandler.getSessionResult;
import static atmClient.handler.SocketHandler.openNewSocket;
import static atmClient.socketData.SocketDataReader.*;
import static atmClient.socketData.SocketDataWriter.getDataOutputStream;

public class ATMClient {

    private long sessionId = -1;

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

        return handleGetUserName(ipAddress, port, timeOut);

    }

    private GetUserNameResult handleGetUserName(String ipAddress, int port, int timeOut) {

        Socket socket;
        try {

            //Open a new socket Connection
            socket = openNewSocket(ipAddress, port, timeOut);

            GetUserNameResult getUserNameResult = handleGetUserNameExchange(
                    socket,
                    timeOut
            );

            //Close connection
            socket.close();

            return getUserNameResult;

        } catch (SocketTimeoutException e) {

            return new GetUserNameResult(
                    SessionResult.ERROR_CODE,
                    SessionHandler.SOCKET_TIMEOUT_ERROR_MSG,
                    Result.ERROR_CODE
            );

        } catch (IOException e) {

            return new GetUserNameResult(
                    SessionResult.ERROR_CODE,
                    SessionHandler.IO_EXCEPTION_ERROR_MSG,
                    Result.ERROR_CODE);
        }

    }

    private GetUserNameResult handleGetUserNameExchange(Socket socket, int timeOut) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        System.out.println("\n\nGetUserNameCMD Start");

        SessionResult sessionResult = getSessionResult(socket, timeOut, ACK_CODE, sessionId);

        int readSessionStatus = sessionResult.getSessionStatus();

        if(readSessionStatus == SessionResult.ERROR_CODE){

            System.out.println("GetUserNameCMD End\n");

            return new GetUserNameResult(
                    sessionResult.getSessionStatus(),
                    sessionResult.getSessionMessage(),
                    Result.ERROR_CODE
            );

        }

        //Send GetUserName cmd
        System.out.println("\tSent GetUserNameCMD");
        sendCommand(
                socket, dataIn, dataOut, timeOut,
                ACK_CODE,
                XulaAtmServerCommands.GET_USERNAME_CMD
        );

        //Send ACK
        sendACK(dataOut,ACK_CODE);

        //Get Result
        Result result = getResult(socket, timeOut, ACK_CODE);

        //Check Result
        if (result.getStatus() == Result.ERROR_CODE){

            System.out.println("GetUserNameCMD End\n");

            return new GetUserNameResult(
                    sessionResult.getStatus(), result.getStatus(), result.getMessage()
            );

        }

        String userName = readString(socket, timeOut, ACK_CODE);

        GetUserNameResult getUserNameResult = new GetUserNameResult(
                sessionResult.getStatus(), result.getStatus()
        );
        getUserNameResult.setUserName(userName);

        System.out.println("GetUserNameCMD End\n");

        return getUserNameResult;

    }
    public GetAccountIdsResult getAccountIds(){

        String ipAddress = getIpAddress();
        int port = getPort();
        int timeOut = getTimeOut();

        return handleGetAccountIds(
                ipAddress, port, timeOut
        );
    }

    private GetAccountIdsResult handleGetAccountIds(String ipAddress, int port, int timeOut) {

        Socket socket;
        try {

            //Open a new socket Connection
            socket = openNewSocket(ipAddress, port, timeOut);
            GetAccountIdsResult getAccountIdsResult = handleGetAccountIdsExchange(
                    socket,
                    timeOut
            );

            //Close connection
            socket.close();

            return getAccountIdsResult;

        } catch (SocketTimeoutException e) {

            return new GetAccountIdsResult(
                    SessionResult.ERROR_CODE,
                    SessionHandler.SOCKET_TIMEOUT_ERROR_MSG,
                    Result.ERROR_CODE
            );

        } catch (IOException e) {

            return new GetAccountIdsResult(
                    SessionResult.ERROR_CODE,
                    SessionHandler.IO_EXCEPTION_ERROR_MSG,
                    Result.ERROR_CODE
            );

        }

    }

    private GetAccountIdsResult handleGetAccountIdsExchange(Socket socket, int timeOut) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        System.out.println("\n\nGetAccountIdsCMD Start");

        SessionResult sessionResult = getSessionResult(socket, timeOut, ACK_CODE, sessionId);

        int readSessionStatus = sessionResult.getSessionStatus();

        if(readSessionStatus == SessionResult.ERROR_CODE){

            System.out.println("GetAccountIdsCMD End\n");

            return new GetAccountIdsResult(
                    sessionResult.getSessionStatus(),
                    sessionResult.getSessionMessage(),
                    Result.ERROR_CODE
            );

        }

        //Send Get AccountIds cmd
        System.out.println("\tSent GetAccountIdsCMD");
        sendCommand(
                socket, dataIn, dataOut, timeOut,
                ACK_CODE,
                XulaAtmServerCommands.GET_USER_ACCOUNTIDS_CMD
        );

        //Send ACK
        sendACK(dataOut,ACK_CODE);

        Result result = getResult(socket, timeOut, ACK_CODE);
        if(result.getStatus() == Result.ERROR_CODE){
            System.out.println("GetAccountIdsCMD End\n");
            return new GetAccountIdsResult(sessionResult.getSessionStatus(), result.getStatus(), result.getMessage());
        }

        //read accountIDs
        ArrayList<Long> accountIDs = readLongs(socket,timeOut,ACK_CODE);
        System.out.println("\tRead accountIDs: "+accountIDs+"\n");

        System.out.println("GetAccountIdsCMD End\n");

        return new GetAccountIdsResult(
                sessionResult.getSessionStatus(),
                result.getStatus(),
                accountIDs
        );

    }

    public GetAccountBalanceResult getAccountBalance(long accountId){

        String ipAddress = getIpAddress();
        int port = getPort();
        int timeOut = getTimeOut();

        return handleGetAccountBalance(
                ipAddress, port, timeOut, accountId
        );
    }

    private GetAccountBalanceResult handleGetAccountBalance(String ipAddress, int port, int timeOut, long accountId) {

        Socket socket;
        try {

            //Open a new socket Connection
            socket = openNewSocket(ipAddress, port, timeOut);
            GetAccountBalanceResult getAccountBalanceResult = handleGetAccountBalanceExchange(
                    socket,
                    timeOut,
                    accountId
            );

            //Close connection
            socket.close();

            return getAccountBalanceResult;

        } catch (SocketTimeoutException e) {

            return new GetAccountBalanceResult(
                    SessionResult.ERROR_CODE,
                    SessionHandler.SOCKET_TIMEOUT_ERROR_MSG,
                    Result.ERROR_CODE
            );

        } catch (IOException e) {

            return new GetAccountBalanceResult(
                    SessionResult.ERROR_CODE,
                    SessionHandler.IO_EXCEPTION_ERROR_MSG,
                    Result.ERROR_CODE
            );
        }

    }

    private GetAccountBalanceResult handleGetAccountBalanceExchange(Socket socket, int timeOut, long accountId) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        System.out.println("\n\nGetAccountBalanceCMD Start");

        SessionResult sessionResult = getSessionResult(socket, timeOut, ACK_CODE, sessionId);

        int readSessionStatus = sessionResult.getSessionStatus();

        if(readSessionStatus == SessionResult.ERROR_CODE){

            System.out.println("GetAccountBalanceCMD End\n");

            return new GetAccountBalanceResult(
                    sessionResult.getSessionStatus(),
                    sessionResult.getSessionMessage(),
                    Result.ERROR_CODE
            );

        }

        System.out.println("\tSent GetAccountBalanceCMD");
        sendCommand(
                socket, dataIn, dataOut, timeOut,
                ACK_CODE,
                XulaAtmServerCommands.GET_ACCOUNT_BALANCE_CMD
        );

        //Send AccountId
        dataOut.writeLong(accountId);
        System.out.println("\tSent AccountId: "+accountId);

        //Read ACK
        readACK(socket, dataIn, timeOut, ACK_CODE);

        //Send ACK
        sendACK(dataOut,ACK_CODE);

        //Read Result
        Result result = getResult(socket, timeOut, ACK_CODE);
        if(result.getStatus() == Result.ERROR_CODE){
            System.out.println("GetAccountBalanceCMD End\n");
            return new GetAccountBalanceResult(
                    sessionResult.getSessionStatus(),
                    result.getStatus(),
                    result.getMessage()
            );
        }

        //read accountBalance
        double accountBalance = readDoubleWTimeout(socket,dataIn,timeOut);
        System.out.println("\tRead accountBalance: "+accountBalance+"\n");

        System.out.println("GetAccountBalanceCMD End\n");

        return new GetAccountBalanceResult(
                sessionResult.getSessionStatus(),
                result.getStatus(),
                accountBalance
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
    
}
