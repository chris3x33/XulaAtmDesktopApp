package atmClient.socketData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import static atmClient.SocketACK.sendACK;
import static atmClient.socketData.SocketDataWriter.getDataOutputStream;

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

    public static byte[] readBytesWTimeout(
            Socket socket, DataInputStream dataIn,
            int timeOut, int numOfBytes) throws IOException {

        final int BYTE_SIZE = 1;

        byte[] readBytes = new byte[numOfBytes];

        boolean hasBytes;
        long startTime = System.currentTimeMillis();
        do {
            hasBytes = (socket.getInputStream().available() >= BYTE_SIZE*numOfBytes);
        } while (!hasBytes && (System.currentTimeMillis() - startTime) < timeOut*numOfBytes);

        if (hasBytes) {
            dataIn.readFully(readBytes);
        } else {
            throw new SocketTimeoutException();
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

    public static String readString(
            Socket socket, int timeOut, int ack) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        //read Str Len
        int readStrLen = readIntWTimeout(socket,dataIn,timeOut);
        System.out.println("\tRead readStr Len");

        //Send Ack
        sendACK(dataOut,ack);

        //Read Str
        String readStr = new String(readBytesWTimeout( socket, dataIn, timeOut, readStrLen));
        System.out.println("\tRead readStr: "+readStr);

        //Send Ack
        sendACK(dataOut,ack);

        return readStr;

    }

    public static ArrayList<Long> readLongs(Socket socket, int timeOut, int ack) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        //read ArrayList<Long> Len
        int readLen = readIntWTimeout(socket,dataIn,timeOut);
        System.out.println("\tRead ArrayList<Long> Len");

        //Send Ack
        sendACK(dataOut,ack);

        ArrayList<Long> longs = new ArrayList<Long>();
        for (int i = 0; i < readLen; i++) {

            long curLong = readLongWTimeout(socket, dataIn, timeOut);
            longs.add(curLong);

            //Send Ack
            sendACK(dataOut,ack);

        }

        return longs;

    }

}
