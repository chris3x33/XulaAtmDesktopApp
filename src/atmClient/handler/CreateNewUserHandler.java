package atmClient.handler;

import atmClient.XulaAtmServerCommands;
import atmClient.result.CreateNewUserResult;
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
import static atmClient.socketData.SocketDataWriter.getDataOutputStream;
import static atmClient.socketData.SocketDataWriter.sendString;

public class CreateNewUserHandler {

    public static CreateNewUserResult handleCreateNewUser(
            String ipAddress, int port, int timeOut, int ackCode,
            long sessionId,String userName, String password) {

        Socket socket;
        try {

            //Open a new socket Connection
            socket = openNewSocket(ipAddress, port, timeOut);

            CreateNewUserResult createNewUserResult = handleCreateNewUserExchange(
                    socket, timeOut,ackCode,
                    sessionId, userName, password
            );

            //Close connection
            socket.close();

            return createNewUserResult;

        } catch (SocketTimeoutException e) {

            return new CreateNewUserResult(
                    SessionResult.ERROR_CODE,
                    SessionHandler.SOCKET_TIMEOUT_ERROR_MSG,
                    Result.ERROR_CODE
            );

        } catch (IOException e) {

            return new CreateNewUserResult(
                    SessionResult.ERROR_CODE,
                    SessionHandler.IO_EXCEPTION_ERROR_MSG,
                    Result.ERROR_CODE
            );
        }
    }

    private static CreateNewUserResult handleCreateNewUserExchange(
            Socket socket, int timeOut, int ackCode,
            long sessionId, String userName, String password) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        System.out.println("\n\nCreateNewUserCMD Start");

        SessionResult sessionResult = getSessionResult(socket, timeOut, ackCode, sessionId);

        int readSessionStatus = sessionResult.getSessionStatus();

        if(readSessionStatus == SessionResult.ERROR_CODE){

            System.out.println("CreateNewUserCMD End\n");

            return new CreateNewUserResult(
                    sessionResult.getSessionStatus(),
                    sessionResult.getSessionMessage(),
                    Result.ERROR_CODE
            );

        }

        //Send Create New User cmd
        System.out.println("\tSent CreateNewUserCMD");
        sendCommand(
                socket, dataIn, dataOut, timeOut,
                ackCode,
                XulaAtmServerCommands.CREATE_NEW_USER_CMD
        );

        //Send userName
        sendString(socket, timeOut, ackCode, userName);

        //Send password
        sendString(socket, timeOut, ackCode, password);

        //Send ACK
        sendACK(dataOut,ackCode);

        Result result = getResult(socket, timeOut, ackCode);

        System.out.println("CreateNewUserCMD End\n");

        if(result.getStatus() == Result.ERROR_CODE){
            return new CreateNewUserResult(
                    sessionResult.getSessionStatus(),
                    result.getStatus(),
                    result.getMessage()
            );
        }

        return new CreateNewUserResult(
                sessionResult.getSessionStatus(),
                result.getStatus()
        );

    }

}
