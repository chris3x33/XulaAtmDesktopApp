package atmClient.handler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketHandler {

    public static Socket openNewSocket(String ipAddress, int port, int timeOut) throws IOException {

        Socket socket = new Socket();

        SocketAddress socketAddress = new InetSocketAddress(ipAddress, port);

        socket.connect(socketAddress, timeOut);

        return socket;

    }
}
