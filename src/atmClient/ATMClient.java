package atmClient;

import java.io.IOException;
import java.net.*;

public class ATMClient {

    private int sessionId = -1;

    private final String DEFAULT_IP_ADDRESS = "127.0.0.1";
    private final int DEFAULT_PORT = 55555;
    private final int DEFAULT_TIMEOUT = 3000;

    private String userDefinedIpAddress = null;
    private int userDefinedPort = -1;
    private int userDefinedTimeOut = -1;

    public Result connect(){

        String ipAddress = DEFAULT_IP_ADDRESS;
        int port = DEFAULT_PORT;
        int timeOut = DEFAULT_TIMEOUT;

        if (userDefinedIpAddress != null){
            ipAddress = userDefinedIpAddress;
        }

        if (userDefinedPort > -1){
            port = userDefinedPort;
        }

        if (userDefinedTimeOut > -1){
            timeOut = userDefinedTimeOut;
        }

        Socket socket;
        try {

             socket = new Socket();

            SocketAddress socketAddress = new InetSocketAddress(ipAddress, port);

            socket.connect(socketAddress, timeOut);

            //Send sessionId = -1 for new sessionId

            //Read ACK

            //Send ACK

            //Read new sessionId > -1

            //Send ACK

            //Read ACK

            //Close connection


        }catch (SocketTimeoutException e) {

            String errMsg = "Unable to Connect Please Try again later!!";

            return new Result(Result.ERROR_CODE, errMsg);

        } catch (IOException e) {

            String errMsg = "Connection Error Please Try again later!!";

            return new Result(Result.ERROR_CODE, errMsg);
        }

        //Set new sessionId


        return new Result(Result.SUCCESS_CODE);

    }

    public Result login(String userName, String password){

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
