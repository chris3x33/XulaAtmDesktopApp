package atmClient.socketData;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class SocketDataReader {

    public static int readIntWTimeout(Socket socket, DataInputStream dataIn, int timeOut) throws IOException {

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


}
