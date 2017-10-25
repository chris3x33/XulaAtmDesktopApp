package atmClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class ATMClient {

    private long sessionId = -1;

    private final String DEFAULT_IP_ADDRESS = "127.0.0.1";
    private final int DEFAULT_PORT = 55555;
    private final int DEFAULT_TIMEOUT = 10000;

    private String userDefinedIpAddress = null;
    private int userDefinedPort = -1;
    private int userDefinedTimeOut = -1;

    private final int ACK_CODE = 10101010;

    public Result connect() {

        String ipAddress = getIpAddress();
        int port = getPort();
        int timeOut = getTimeOut();

        return handleNewSession(ipAddress, port, timeOut);

    }

    private Result handleNewSession(String ipAddress, int port, int timeOut) {

        //reset sessionId if set
        sessionId = -1;

        Socket socket;
        try {

            socket = openNewSocket(ipAddress, port, timeOut);

            handleNewSessionExchange(socket, timeOut);

            //Close connection
            socket.close();

            return new Result(Result.SUCCESS_CODE);

        } catch (SocketTimeoutException e) {

            String errMsg = "Unable to Connect Please Try again later!!";

            return new Result(Result.ERROR_CODE, errMsg);

        } catch (IOException e) {

            String errMsg = "Connection Error Please Try again later!!";

            return new Result(Result.ERROR_CODE, errMsg);
        }
    }

    public Socket openNewSocket(String ipAddress, int port, int timeOut) throws IOException {

        Socket socket = new Socket();

        SocketAddress socketAddress = new InetSocketAddress(ipAddress, port);

        socket.connect(socketAddress, timeOut);

        return socket;

    }

    private void handleNewSessionExchange(Socket socket, int timeOut) throws SocketTimeoutException, IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        System.out.println("\n\nNewSessionCMD Start");

        int ack;

        //Send sessionId = -1 for new sessionId
        dataOut.writeLong(this.sessionId);
        System.out.println("\tSent sessionId = " + this.sessionId);

        //Read ACK
        ack = readIntWTimeout(socket, dataIn, timeOut);
        printACKResult(ack);

        //Send ACK
        dataOut.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");

        //Read new sessionId > -1
        long newSessionId = readLongWTimeout(socket, dataIn, timeOut);
        System.out.println("\tRead sessionId = " + newSessionId);

        //Send ACK
        dataOut.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");

        //Read ACK
        ack = readIntWTimeout(socket, dataIn, timeOut);
        printACKResult(ack);

        this.sessionId = newSessionId;

        System.out.println("NewSessionCMD End\n");
    }

    private void sendString(
            Socket socket, int timeOut, String sendStr) throws IOException {

        int ack;

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        //Send sendStr Length
        dataOut.writeInt(sendStr.length());
        System.out.println("\tSent sendStr Length");

        //Read ACK
        ack = readIntWTimeout(socket, dataIn, timeOut);
        printACKResult(ack);

        //Send sendStr Bytes
        dataOut.write(sendStr.getBytes());
        System.out.println("\tSent sendStr: "+sendStr);

        //Read ACK
        ack = readIntWTimeout(socket, dataIn, timeOut);
        printACKResult(ack);

    }

    private ArrayList<Long> readLongs(Socket socket, int timeOut) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        //read ArrayList<Long> Len
        int readLen = readIntWTimeout(socket,dataIn,timeOut);
        System.out.println("\tRead ArrayList<Long> Len");

        //Send Ack
        dataOut.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");

        ArrayList<Long> longs = new ArrayList<Long>();
        for (int i = 0; i < readLen; i++) {

            long curLong = readLongWTimeout(socket, dataIn, timeOut);
            longs.add(curLong);

            //Send Ack
            dataOut.writeInt(ACK_CODE);
            System.out.println("\tSent ACK");

        }

        return longs;

    }

    private String readString(
            Socket socket, int timeOut) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        //read Str Len
        int readStrLen = readIntWTimeout(socket,dataIn,timeOut);
        System.out.println("\tRead readStr Len");

        //Send Ack
        dataOut.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");

        //Read Str
        String readStr = new String(readBytesWTimeout( socket, dataIn, timeOut, readStrLen));
        System.out.println("\tRead readStr: "+readStr);

        //Send Ack
        dataOut.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");

        return readStr;

    }

    private long readLongWTimeout(Socket socket, DataInputStream dataIn, int timeOut) throws IOException {

        final long BYTE_SIZE_OF_LONG = Long.SIZE / Byte.SIZE;

        boolean hasLong;

        long startTime = System.currentTimeMillis();

        do {
            hasLong = (socket.getInputStream().available() >= BYTE_SIZE_OF_LONG);
        } while (!hasLong && (System.currentTimeMillis() - startTime) < timeOut);

        if (hasLong) {
            return dataIn.readLong();
        } else {
            throw new SocketTimeoutException();
        }
    }

    private int readIntWTimeout(Socket socket, DataInputStream dataIn, int timeOut) throws IOException {

        final int BYTE_SIZE_OF_INT = Integer.SIZE / Byte.SIZE;

        boolean hasInt;

        long startTime = System.currentTimeMillis();

        do {
            hasInt = (socket.getInputStream().available() >= BYTE_SIZE_OF_INT);
        } while (!hasInt && (System.currentTimeMillis() - startTime) < timeOut);

        if (hasInt) {
            return dataIn.readInt();
        } else {
            throw new SocketTimeoutException();
        }
    }

    private byte[] readBytesWTimeout(Socket socket, DataInputStream dataIn, int timeOut, int numOfBytes) throws IOException {

        final int BYTE_SIZE = 1;

        byte[] readBytes = new byte[numOfBytes];

        boolean hasByte;
        for (int i = 0; i < readBytes.length; i++) {

            long startTime = System.currentTimeMillis();

            do {
                hasByte = (socket.getInputStream().available() >= BYTE_SIZE);
            } while (!hasByte && (System.currentTimeMillis() - startTime) < timeOut);

            if (hasByte) {
                readBytes[i] = dataIn.readByte();
            } else {
                throw new SocketTimeoutException();
            }

        }

        return readBytes;
    }

    private double readDoubleWTimeout(Socket socket, DataInputStream dataIn, int timeOut) throws IOException {

        final int BYTE_SIZE_OF_DOUBLE = Double.SIZE / Byte.SIZE;

        boolean hasDouble;

        long startTime = System.currentTimeMillis();

        do {
            hasDouble = (socket.getInputStream().available() >= BYTE_SIZE_OF_DOUBLE);
        } while (!hasDouble && (System.currentTimeMillis() - startTime) < timeOut);

        if (hasDouble) {
            return dataIn.readDouble();
        } else {
            throw new SocketTimeoutException();
        }
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

    private DataInputStream getDataInputStream(Socket socket) throws IOException {
        return new DataInputStream(socket.getInputStream());
    }

    private DataOutputStream getDataOutputStream(Socket socket) throws IOException {
        return new DataOutputStream(socket.getOutputStream());
    }

    private SessionResult getSessionResult(Socket socket, int timeOut, long sessionId) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        int ack;

        //Send sessionId
        dataOut.writeLong(sessionId);
        System.out.println("\tSent sessionId: "+sessionId);

        //Read ACK
        ack = readIntWTimeout(socket, dataIn, timeOut);
        printACKResult(ack);

        //Send ACK
        dataOut.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");

        //Read session Result
        int readSessionStatus = readIntWTimeout(socket, dataIn, timeOut);
        System.out.println("\tRead readSessionStatus: "+readSessionStatus);

        //Send ACK
        dataOut.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");

        if (readSessionStatus <= SessionResult.ERROR_CODE) {

            //Get Session Msg Length in bytes
            int readSessionMessageLen = readIntWTimeout(socket, dataIn, timeOut);
            System.out.println("\tRead readSessionMessage Length");

            //Send ACK
            dataOut.writeInt(ACK_CODE);
            System.out.println("\tSent ACK");

            //Get Session Msg
            byte[] readSessionBytes = readBytesWTimeout(
                    socket,
                    dataIn,
                    timeOut,
                    readSessionMessageLen
            );
            String readSessionMessage = new String(readSessionBytes);
            System.out.println("\tRead readSessionMessage: "+readSessionMessage);

            //Send ACK
            dataOut.writeInt(ACK_CODE);
            System.out.println("\tSent ACK");

            //Read ACK
            ack = readIntWTimeout(socket, dataIn, timeOut);
            printACKResult(ack);

            return new SessionResult(
                    readSessionStatus,
                    readSessionMessage,
                    Result.ERROR_CODE
            );

        }

        //Read ACK
        ack = readIntWTimeout(socket, dataIn, timeOut);
        printACKResult(ack);

        return new SessionResult(SessionResult.SUCCESS_CODE);

    }

    public Result getResult(Socket socket, int timeOut) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        int ack;

        //Send ACK
        dataOut.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");

        //Read login status result
        int readResultStatus = readIntWTimeout(
                socket,
                dataIn,
                timeOut
        );
        System.out.println("\tRead result status: "+readResultStatus);

        //Send ACK
        dataOut.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");

        if(readResultStatus == Result.ERROR_CODE){

            //Get Result Msg Length in bytes
            int readResultMessageLen = readIntWTimeout(socket, dataIn, timeOut);
            System.out.println("\tRead Result Message Length");

            //Send ACK
            dataOut.writeInt(ACK_CODE);
            System.out.println("\tSent ACK");

            //Get Result Msg
            byte[] readResultMessageBytes = readBytesWTimeout(
                    socket,
                    dataIn,
                    timeOut,
                    readResultMessageLen
            );
            String readResultMessage = new String(readResultMessageBytes);
            System.out.println("\tRead result Message: "+readResultMessage);

            //Send ACK
            dataOut.writeInt(ACK_CODE);
            System.out.println("\tSent ACK");

            //Read ACK
            ack = readIntWTimeout(socket, dataIn, timeOut);
            printACKResult(ack);

            return new Result(
                    Result.ERROR_CODE,
                    readResultMessage
            );

        }

        //Read ACK
        ack = readIntWTimeout(socket, dataIn, timeOut);
        printACKResult(ack);

        return new Result(Result.SUCCESS_CODE);

    }


    public LoginResult login(String userName, String password) {

        String ipAddress = getIpAddress();
        int port = getPort();
        int timeOut = getTimeOut();

        return handleLogin(ipAddress, port, timeOut, userName, password);

    }


    private LoginResult handleLogin(String ipAddress, int port, int timeOut, String userName, String password) {

        Socket socket;
        try {

            //Open a new socket Connection
            socket = openNewSocket(ipAddress, port, timeOut);

            LoginResult loginResult = handleLoginExchange(
                    socket, timeOut, userName, password
            );

            //Close connection
            socket.close();

            return loginResult;

        } catch (SocketTimeoutException e) {

            String errMsg = "Unable to Connect Please Try again later!!";

            return new LoginResult(SessionResult.ERROR_CODE, errMsg, Result.ERROR_CODE);

        } catch (IOException e) {

            String errMsg = "Connection Error Please Try again later!!";

            return new LoginResult(SessionResult.ERROR_CODE, errMsg, Result.ERROR_CODE);
        }
    }

    private LoginResult handleLoginExchange(Socket socket, int timeOut, String userName, String password) throws IOException {

        //sessionId=0;

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        System.out.println("\n\nLoginCMD Start");

        int ack;

        SessionResult sessionResult = getSessionResult(socket, timeOut, sessionId);

        int readSessionStatus = sessionResult.getSessionStatus();

        if(readSessionStatus == SessionResult.ERROR_CODE){

            System.out.println("LoginCMD End\n");

            return new LoginResult(
                    sessionResult.getSessionStatus(),
                    sessionResult.getSessionMessage(),
                    Result.ERROR_CODE
            );

        }

        //Send login cmd
        dataOut.writeInt(XulaAtmServerCommands.LOGIN_CMD);
        System.out.println("\tSent LOGIN_CMD");

        //Read ACK
        ack = readIntWTimeout(socket, dataIn, timeOut);
        printACKResult(ack);

        //Send userName
        sendString(socket, timeOut, userName);

        //Send password
        sendString(socket, timeOut, password);

        Result result = getResult(socket, timeOut);

        System.out.println("LoginCMD End\n");

        if(result.getStatus() == Result.ERROR_CODE){
            return new LoginResult(sessionResult.getSessionStatus(), result.getStatus(), result.getMessage());
        }

        return new LoginResult(sessionResult.getSessionStatus(), result.getStatus());

    }

    public CreateNewUserResult createNewUser(String userName, String password) {

        String ipAddress = getIpAddress();
        int port = getPort();
        int timeOut = getTimeOut();

        return handleCreateNewUser(
                ipAddress, port, timeOut,
                userName, password
        );

    }

    private CreateNewUserResult handleCreateNewUser(
            String ipAddress, int port, int timeOut, String userName, String password) {

        Socket socket;
        try {

            //Open a new socket Connection
            socket = openNewSocket(ipAddress, port, timeOut);

            CreateNewUserResult createNewUserResult = handleCreateNewUserExchange(
                    socket, timeOut, userName, password
            );

            //Close connection
            socket.close();

            return createNewUserResult;

        } catch (SocketTimeoutException e) {

            String errMsg = "Unable to Connect Please Try again later!!";

            return new CreateNewUserResult(SessionResult.ERROR_CODE, errMsg, Result.ERROR_CODE);

        } catch (IOException e) {

            String errMsg = "Connection Error Please Try again later!!";

            return new CreateNewUserResult(SessionResult.ERROR_CODE, errMsg, Result.ERROR_CODE);
        }
    }

    private CreateNewUserResult handleCreateNewUserExchange(
            Socket socket, int timeOut, String userName, String password) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        System.out.println("\n\nCreateNewUserCMD Start");

        int ack;

        SessionResult sessionResult = getSessionResult(socket, timeOut, sessionId);

        int readSessionStatus = sessionResult.getSessionStatus();

        if(readSessionStatus == SessionResult.ERROR_CODE){

            System.out.println("CreateNewUserCMD End\n");

            return new CreateNewUserResult(
                    sessionResult.getSessionStatus(),
                    sessionResult.getSessionMessage(),
                    Result.ERROR_CODE
            );

        }

        //Send Create New User cmd
        dataOut.writeInt(XulaAtmServerCommands.CREATE_NEW_USER_CMD);
        System.out.println("\tSent CreateNewUserCMD");

        //Read ACK
        ack = readIntWTimeout(socket, dataIn, timeOut);
        printACKResult(ack);

        //Send userName
        sendString(socket, timeOut, userName);

        //Send password
        sendString(socket, timeOut, password);

        Result result = getResult(socket, timeOut);

        System.out.println("CreateNewUserCMD End\n");

        if(result.getStatus() == Result.ERROR_CODE){
            return new CreateNewUserResult(sessionResult.getSessionStatus(), result.getStatus(), result.getMessage());
        }

        return new CreateNewUserResult(sessionResult.getSessionStatus(), result.getStatus());


    }

    public LogOutResult logout(){

        String ipAddress = getIpAddress();
        int port = getPort();
        int timeOut = getTimeOut();

        return handleLogout(ipAddress, port, timeOut);

    }

    private LogOutResult handleLogout(String ipAddress, int port, int timeOut) {

        Socket socket;
        try {

            //Open a new socket Connection
            socket = openNewSocket(ipAddress, port, timeOut);

            LogOutResult logOutResult = handleLogoutExchange(
                    socket,
                    timeOut
            );

            //Close connection
            socket.close();

            return logOutResult;

        } catch (SocketTimeoutException e) {

            String errMsg = "Unable to Connect Please Try again later!!";

            return new LogOutResult(SessionResult.ERROR_CODE, errMsg, Result.ERROR_CODE);

        } catch (IOException e) {

            String errMsg = "Connection Error Please Try again later!!";

            return new LogOutResult(SessionResult.ERROR_CODE, errMsg, Result.ERROR_CODE);
        }

    }

    private LogOutResult handleLogoutExchange(Socket socket, int timeOut) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        System.out.println("\n\nLogoutCMD Start");

        int ack;

        SessionResult sessionResult = getSessionResult(socket, timeOut, sessionId);

        int readSessionStatus = sessionResult.getSessionStatus();

        if(readSessionStatus == SessionResult.ERROR_CODE){

            System.out.println("LogoutCMD End\n");

            return new LogOutResult(
                    sessionResult.getSessionStatus(),
                    sessionResult.getSessionMessage(),
                    Result.ERROR_CODE
            );

        }

        //Send Logout cmd
        dataOut.writeInt(XulaAtmServerCommands.LOGOUT_CMD);
        System.out.println("\tSent LogoutCMD");

        //Read ACK
        ack = readIntWTimeout(socket, dataIn, timeOut);
        printACKResult(ack);

        //Send ACK
        dataOut.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");

        //Read ACK
        ack = readIntWTimeout(socket, dataIn, timeOut);
        printACKResult(ack);

        System.out.println("LogoutCMD End\n");

        return new LogOutResult(SessionResult.SUCCESS_CODE, Result.SUCCESS_CODE);
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

            String errMsg = "Unable to Connect Please Try again later!!";

            return new GetUserNameResult(SessionResult.ERROR_CODE, errMsg, Result.ERROR_CODE);

        } catch (IOException e) {

            String errMsg = "Connection Error Please Try again later!!";

            return new GetUserNameResult(SessionResult.ERROR_CODE, errMsg, Result.ERROR_CODE);
        }



    }

    private GetUserNameResult handleGetUserNameExchange(Socket socket, int timeOut) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        System.out.println("\n\nGetUserNameCMD Start");

        int ack;

        SessionResult sessionResult = getSessionResult(socket, timeOut, sessionId);

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
        dataOut.writeInt(XulaAtmServerCommands.GET_USERNAME_CMD);
        System.out.println("\tSent GetUserNameCMD");

        //Read ACK
        ack = readIntWTimeout(socket, dataIn, timeOut);
        printACKResult(ack);

        //Get Result
        Result result = getResult(socket, timeOut);

        //Check Result
        if (result.getStatus() == Result.ERROR_CODE){

            System.out.println("GetUserNameCMD End\n");

            return new GetUserNameResult(
                    sessionResult.getStatus(), result.getStatus(), result.getMessage()
            );

        }

        String userName = readString(socket, timeOut);

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

            String errMsg = "Unable to Connect Please Try again later!!";

            return new GetAccountIdsResult(SessionResult.ERROR_CODE, errMsg, Result.ERROR_CODE);

        } catch (IOException e) {

            String errMsg = "Connection Error Please Try again later!!";

            return new GetAccountIdsResult(SessionResult.ERROR_CODE, errMsg, Result.ERROR_CODE);
        }

    }

    private GetAccountIdsResult handleGetAccountIdsExchange(Socket socket, int timeOut) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        System.out.println("\n\nCreateNewUserCMD Start");

        int ack;

        SessionResult sessionResult = getSessionResult(socket, timeOut, sessionId);

        int readSessionStatus = sessionResult.getSessionStatus();

        if(readSessionStatus == SessionResult.ERROR_CODE){

            System.out.println("GetAccountIdsCMD End\n");

            return new GetAccountIdsResult(
                    sessionResult.getSessionStatus(),
                    sessionResult.getSessionMessage(),
                    Result.ERROR_CODE
            );

        }

        //Send Create New User cmd
        dataOut.writeInt(XulaAtmServerCommands.GET_USER_ACCOUNTIDS_CMD);
        System.out.println("\tSent GetAccountIdsCMD");

        //Read ACK
        ack = readIntWTimeout(socket, dataIn, timeOut);
        printACKResult(ack);


        Result result = getResult(socket, timeOut);
        if(result.getStatus() == Result.ERROR_CODE){
            System.out.println("GetAccountIdsCMD End\n");
            return new GetAccountIdsResult(sessionResult.getSessionStatus(), result.getStatus(), result.getMessage());
        }

        //read accountIDs
        ArrayList<Long> accountIDs = readLongs(socket,timeOut);
        System.out.println("\tRead accountIDs: "+accountIDs+"\n");

        System.out.println("GetAccountIdsCMD End\n");

        return new GetAccountIdsResult(
                sessionResult.getSessionStatus(),
                result.getStatus(),
                accountIDs
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

    private void printACKResult(int ack){

        if (ack == ACK_CODE){
            System.out.println("\tRead ACK");
        }else {
            System.out.println("\tACK Read Error");
        }

    }
}
