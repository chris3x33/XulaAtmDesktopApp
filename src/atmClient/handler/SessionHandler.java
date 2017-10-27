package atmClient.handler;

import atmClient.result.Result;
import atmClient.result.SessionResult;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static atmClient.SocketACK.readACK;
import static atmClient.SocketACK.sendACK;
import static atmClient.socketData.SocketDataReader.getDataInputStream;
import static atmClient.socketData.SocketDataReader.readBytesWTimeout;
import static atmClient.socketData.SocketDataReader.readIntWTimeout;
import static atmClient.socketData.SocketDataWriter.getDataOutputStream;

public class SessionHandler {

    public static final String SOCKET_TIMEOUT_ERROR_MSG = "Unable to Connect Please Try again later!!";
    public static final String IO_EXCEPTION_ERROR_MSG = "Connection Error Please Try again later!!";
    public static final int INVALID_SESSION = -1;

    public static void sendInvalidSession(DataOutputStream dataOut) throws IOException {

        dataOut.writeLong(INVALID_SESSION);
        System.out.println("\tSent INVALID_SESSION id");

    }

    public static SessionResult getSessionResult(Socket socket, int timeOut, int ackCode, long sessionId) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        //Send sessionId
        dataOut.writeLong(sessionId);
        System.out.println("\tSent sessionId: "+sessionId);

        //Read ACK
        readACK(socket, dataIn, timeOut, ackCode);

        //Send ACK
        sendACK(dataOut,ackCode);

        //Read session Result
        int readSessionStatus = readIntWTimeout(socket, dataIn, timeOut);
        System.out.println("\tRead readSessionStatus: "+readSessionStatus);

        //Send ACK
        sendACK(dataOut,ackCode);

        if (readSessionStatus <= SessionResult.ERROR_CODE) {

            //Get Session Msg Length in bytes
            int readSessionMessageLen = readIntWTimeout(socket, dataIn, timeOut);
            System.out.println("\tRead readSessionMessage Length");

            //Send ACK
            sendACK(dataOut,ackCode);

            //Get Session Msg
            byte[] readSessionBytes = readBytesWTimeout(
                    socket,
                    dataIn,
                    timeOut,
                    readSessionMessageLen
            );
            String readSessionMessage = new String(readSessionBytes);
            System.out.println("\tRead readSessionMessage: "+readSessionMessage);

            //Send ACK
            sendACK(dataOut,ackCode);

            //Read ACK
            readACK(socket, dataIn, timeOut, ackCode);

            return new SessionResult(
                    readSessionStatus,
                    readSessionMessage,
                    Result.ERROR_CODE
            );

        }

        //Read ACK
        readACK(socket, dataIn, timeOut, ackCode);

        return new SessionResult(SessionResult.SUCCESS_CODE);

    }
}
