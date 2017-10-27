package atmClient.handler;

import java.io.DataOutputStream;
import java.io.IOException;

public class SessionHandler {

    public static final String SOCKET_TIMEOUT_ERROR_MSG = "Unable to Connect Please Try again later!!";
    public static final String IO_EXCEPTION_ERROR_MSG = "Connection Error Please Try again later!!";
    public static final int INVALID_SESSION = -1;

    public static void sendInvalidSession(DataOutputStream dataOut) throws IOException {

        dataOut.writeLong(INVALID_SESSION);
        System.out.println("\tSent INVALID_SESSION id");

    }
}
