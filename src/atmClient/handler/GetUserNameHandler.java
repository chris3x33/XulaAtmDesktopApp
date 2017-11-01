package atmClient.handler;

import atmClient.XulaAtmServerCommands;
import atmClient.result.GetUserNameResult;
import atmClient.result.Result;
import atmClient.result.SessionResult;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

import static atmClient.SocketACK.sendACK;
import static atmClient.handler.CommandHandler.sendCommand;
import static atmClient.handler.ResultHandler.getResult;
import static atmClient.handler.SessionHandler.getSessionResult;
import static atmClient.handler.SocketHandler.openNewSocket;
import static atmClient.socketData.SocketDataReader.getDataInputStream;
import static atmClient.socketData.SocketDataReader.readString;
import static atmClient.socketData.SocketDataWriter.getDataOutputStream;

public class GetUserNameHandler {

    public static GetUserNameResult handleGetUserName(
            String ipAddress, int port, int timeOut, int ackCode, long sessionId) {

        Socket socket;
        try {

            //Open a new socket Connection
            socket = openNewSocket(ipAddress, port, timeOut);

            GetUserNameResult getUserNameResult = handleGetUserNameExchange(
                    socket,
                    timeOut,
                    ackCode,
                    sessionId
            );

            //Close connection
            socket.close();

            return getUserNameResult;

        } catch (SocketTimeoutException e) {

            return new GetUserNameResult(
                    SessionResult.ERROR_CODE,
                    SessionHandler.SOCKET_TIMEOUT_ERROR_MSG,
                    Result.ERROR_CODE
            );

        } catch (IOException e) {

            return new GetUserNameResult(
                    SessionResult.ERROR_CODE,
                    SessionHandler.IO_EXCEPTION_ERROR_MSG,
                    Result.ERROR_CODE);
        }

    }

    private static GetUserNameResult handleGetUserNameExchange(Socket socket, int timeOut, int ackCode, long sessionId) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        System.out.println("\n\nGetUserNameCMD Start");

        SessionResult sessionResult = getSessionResult(socket, timeOut, ackCode, sessionId);

        int readSessionStatus = sessionResult.getSessionStatus();

        if(readSessionStatus <= SessionResult.ERROR_CODE){

            System.out.println("GetUserNameCMD End\n");

            return new GetUserNameResult(
                    sessionResult.getSessionStatus(),
                    sessionResult.getSessionMessage(),
                    Result.ERROR_CODE
            );

        }

        //Send GetUserName cmd
        System.out.println("\tSent GetUserNameCMD");
        sendCommand(
                socket, dataIn, dataOut, timeOut,
                ackCode,
                XulaAtmServerCommands.GET_USERNAME_CMD
        );

        //Send ACK
        sendACK(dataOut,ackCode);

        //Get Result
        Result result = getResult(socket, timeOut, ackCode);

        //Check Result
        if (result.getStatus() <= Result.ERROR_CODE){

            System.out.println("GetUserNameCMD End\n");

            return new GetUserNameResult(
                    sessionResult.getStatus(), result.getStatus(), result.getMessage()
            );

        }

        String userName = readString(socket, timeOut, ackCode);

        GetUserNameResult getUserNameResult = new GetUserNameResult(
                sessionResult.getStatus(), result.getStatus()
        );
        getUserNameResult.setUserName(userName);

        System.out.println("GetUserNameCMD End\n");

        return getUserNameResult;

    }

}
