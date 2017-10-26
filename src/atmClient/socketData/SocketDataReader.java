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

    public static long readLongWTimeout(Socket socket, DataInputStream dataIn, int timeOut) throws IOException {

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

    public static byte[] readBytesWTimeout(Socket socket, DataInputStream dataIn, int timeOut, int numOfBytes) throws IOException {

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

    public static double readDoubleWTimeout(Socket socket, DataInputStream dataIn, int timeOut) throws IOException {

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

    public static DataInputStream getDataInputStream(Socket socket) throws IOException {
        return new DataInputStream(socket.getInputStream());
    }

}
