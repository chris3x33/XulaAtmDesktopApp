package atmClient.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static atmClient.SocketACK.readACK;

public class CommandHandler {

    public static void sendCommand(Socket socket, DataInputStream dataIn, DataOutputStream dataOut, int timeOut, int ackCode, int command) throws IOException {

        //Send Command
        dataOut.writeInt(command);

        //Read ACK
        readACK(socket, dataIn, timeOut, ackCode);

    }

}
