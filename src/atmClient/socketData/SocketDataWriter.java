package atmClient.socketData;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketDataWriter {

    public static DataOutputStream getDataOutputStream(Socket socket) throws IOException {
        return new DataOutputStream(socket.getOutputStream());
    }

}
