package atmClient;

import java.io.IOException;
import java.net.*;

public class ATMClient {

    private Socket socket;

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

        Socket socket = null;
        try {

             socket = new Socket();

            SocketAddress socketAddress = new InetSocketAddress(ipAddress, port);

            socket.connect(socketAddress, timeOut);

        }catch (SocketTimeoutException e) {

            //"Server took to long to respond"

        } catch (IOException e) {

        }

        this.socket = socket;

        return null;

    }

    public Result setConnection(String ipAddress, int port, int timeOut){

        // check ipAddress
        if(!ipAddressExits(ipAddress)){
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

    public boolean ipAddressExits(String ipAddress){

        try {

            InetAddress.getByName(ipAddress);

            return true;

        }catch (UnknownHostException e){
            return false;
        }

    }
}
