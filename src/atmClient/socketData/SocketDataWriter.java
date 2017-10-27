package atmClient.socketData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static atmClient.SocketACK.readACK;
import static atmClient.socketData.SocketDataReader.getDataInputStream;

public class SocketDataWriter {

    public static DataOutputStream getDataOutputStream(Socket socket) throws IOException {
        return new DataOutputStream(socket.getOutputStream());
    }

    public static void sendString(
            Socket socket, int timeOut, int ackCode, String sendStr) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        //Send sendStr Length
        dataOut.writeInt(sendStr.length());
        System.out.println("\tSent sendStr Length: " + sendStr.length());

        //Read ACK
        readACK(socket, dataIn, timeOut, ackCode);

        //Send sendStr Bytes
        dataOut.write(sendStr.getBytes());
        System.out.println("\tSent sendStr: "+sendStr);

        //Read ACK
        readACK(socket, dataIn, timeOut, ackCode);

    }

}
