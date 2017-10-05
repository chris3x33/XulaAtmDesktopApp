package atmClient;

import java.net.Socket;

public class ATMClient {

    private Socket socket;

    private final String DEFAULT_IP_ADDRESS = "127.0.0.1";
    private final int DEFAULT_PORT = 55555;
    private final int DEFAULT_TIMEOUT = 3000;

    private String userDefinedIpAddress = null;
    private int userDefinedPort = -1;
    private int userDefinedTimeOut = -1;

    public Result connect(){

        return null;

    }
}
