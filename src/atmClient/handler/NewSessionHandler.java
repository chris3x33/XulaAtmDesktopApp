package atmClient.handler;

import atmClient.result.NewSessionResult;
import atmClient.result.Result;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

import static atmClient.SocketACK.readACK;
import static atmClient.SocketACK.sendACK;
import static atmClient.handler.SessionHandler.sendInvalidSession;
import static atmClient.handler.SocketHandler.openNewSocket;
import static atmClient.socketData.SocketDataReader.getDataInputStream;
import static atmClient.socketData.SocketDataReader.readLongWTimeout;
import static atmClient.socketData.SocketDataWriter.getDataOutputStream;

public class NewSessionHandler {

    public static NewSessionResult handleNewSession(String ipAddress, int port, int timeOut, int ackCode) {

        Socket socket;
        try {

            socket = openNewSocket(ipAddress, port, timeOut);

            NewSessionResult newSessionResult = handleNewSessionExchange(socket, timeOut, ackCode);

            //Close connection
            socket.close();

            return newSessionResult;

        } catch (SocketTimeoutException e) {

            return new NewSessionResult(Result.ERROR_CODE, SessionHandler.SOCKET_TIMEOUT_ERROR_MSG);

        } catch (IOException e) {

            return new NewSessionResult(Result.ERROR_CODE, SessionHandler.IO_EXCEPTION_ERROR_MSG);

        }
    }

    public static NewSessionResult handleNewSessionExchange(Socket socket, int timeOut, int ackCode) throws SocketTimeoutException, IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        System.out.println("\n\nNewSessionCMD Start");

        //Send Invalid Session to get new session
        sendInvalidSession(dataOut);

        //Read ACK
        readACK(socket, dataIn, timeOut, ackCode);

        //Send ACK
        sendACK(dataOut,ackCode);

        //Read new sessionId > -1
        long newSessionId = readLongWTimeout(socket, dataIn, timeOut);
        System.out.println("\tRead sessionId = " + newSessionId);

        //Send ACK
        sendACK(dataOut,ackCode);

        //Read ACK
        readACK(socket, dataIn, timeOut, ackCode);

        System.out.println("NewSessionCMD End\n");

        return new NewSessionResult(Result.SUCCESS_CODE, newSessionId);

    }

}
