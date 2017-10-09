package atmClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class ATMClient {

    private long sessionId = -1;

    private final String DEFAULT_IP_ADDRESS = "127.0.0.1";
    private final int DEFAULT_PORT = 55555;
    private final int DEFAULT_TIMEOUT = 3000;

    private String userDefinedIpAddress = null;
    private int userDefinedPort = -1;
    private int userDefinedTimeOut = -1;

    private final int ACK_CODE = 10101010;

    public Result connect(){

        String ipAddress = getIpAddress();
        int port = getPort();
        int timeOut = getTimeOut();

        return handleNewSession(ipAddress, port, timeOut);

    }
    private Result handleNewSession(String ipAddress, int port, int timeOut){

        //reset sessionId if set
        sessionId = -1;

        Socket socket;
        try {

            socket = new Socket();

            SocketAddress socketAddress = new InetSocketAddress(ipAddress, port);

            socket.connect(socketAddress, timeOut);

            handleNewSessionExchange(socket, timeOut);

            //Close connection
            socket.close();

            return new Result(Result.SUCCESS_CODE);

        }catch (SocketTimeoutException e) {

            String errMsg = "Unable to Connect Please Try again later!!";

            return new Result(Result.ERROR_CODE, errMsg);

        } catch (IOException e) {

            String errMsg = "Connection Error Please Try again later!!";

            return new Result(Result.ERROR_CODE, errMsg);
        }
    }

    private void handleNewSessionExchange(Socket socket, int timeOut) throws SocketTimeoutException, IOException{

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        System.out.println("\n\nNewSessionCMD Start");

        int ack;

        //Send sessionId = -1 for new sessionId
        dataOut.writeLong(this.sessionId);
        System.out.println("\tSent sessionId = "+this.sessionId);

        //Read ACK
        ack = readIntWTimeout(socket, dataIn, timeOut);
        if (ack == ACK_CODE){
            System.out.println("\tRead ACK");
        }else {
            System.out.println("\tACK Read Error");
        }

        //Send ACK
        dataOut.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");

        //Read new sessionId > -1
        long newSessionId = readLongWTimeout(socket, dataIn, timeOut);
        System.out.println("\tRead sessionId = "+newSessionId);

        //Send ACK
        dataOut.writeInt(ACK_CODE);
        System.out.println("\tSent ACK");

        //Read ACK
        ack = readIntWTimeout(socket, dataIn, timeOut);

        if (ack == ACK_CODE){
            System.out.println("\tRead ACK");
        }else {
            System.out.println("\tACK Read Error");
        }

        this.sessionId = newSessionId;

        System.out.println("NewSessionCMD End\n");
    }


    private long readLongWTimeout(Socket socket, DataInputStream dataIn, int timeOut) throws IOException {

        final long BYTE_SIZE_OF_LONG = Long.SIZE/ Byte.SIZE;

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

        final int BYTE_SIZE_OF_INT = Integer.SIZE/ Byte.SIZE;

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


    public int getTimeOut(){
        if (userDefinedTimeOut > -1){
            return userDefinedTimeOut;
        }else{
            return DEFAULT_TIMEOUT;
        }

    }

    public int getPort(){

        if (userDefinedPort > -1){
            return userDefinedPort;
        }else {
            return DEFAULT_PORT;
        }

    }


    public String getIpAddress(){

        if (userDefinedIpAddress != null){

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



    public Result login(String userName, String password){

        //Open a new socket Connection

        //Send sessionId

        //Read ACK

        //Send login cmd

        //Read ACK

        //Send userName

        //Read ACK

        //Send password

        //Read ACK

        //Send ACK

        //Read login result

        //Send ACK

        //Read ACK

        //Close connection

        return new Result(Result.SUCCESS_CODE);

    }

    public Result createNewUser(String userName, String password){

        return new Result(Result.SUCCESS_CODE);

    }

    public Result setConnection(String ipAddress, int port, int timeOut){

        // check ipAddress
        if(!ipAddressExists(ipAddress)){
            String errMsg = "IP Address Not Found!!";

            return new Result(Result.ERROR_CODE, errMsg);

        }

        // check port
        if (port <= -1){
            String errMsg = "Port Must Be Greater than -1 !!";

            return new Result(Result.ERROR_CODE, errMsg);
        }

        // check timeOut
        if (timeOut <= -1){
            String errMsg = "Timeout Must Be Greater than -1 !!";

            return new Result(Result.ERROR_CODE, errMsg);
        }

        userDefinedIpAddress = ipAddress;
        userDefinedPort = port;
        userDefinedTimeOut = timeOut;

        return new Result(Result.SUCCESS_CODE);
    }

    public boolean ipAddressExists(String ipAddress){

        try {

            InetAddress.getByName(ipAddress);

            return true;

        }catch (UnknownHostException e){
            return false;
        }

    }
}
