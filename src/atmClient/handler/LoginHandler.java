package atmClient.handler;

import atmClient.XulaAtmServerCommands;
import atmClient.result.LoginResult;
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

public class LoginHandler {

    public static LoginResult handleLogin(
            String ipAddress, int port, int timeOut, int ackCode,
            long sessionId, String userName, String password) {

        Socket socket;
        try {

            //Open a new socket Connection
            socket = openNewSocket(ipAddress, port, timeOut);

            LoginResult loginResult = handleLoginExchange(
                    socket, timeOut, ackCode,
                    sessionId,
                    userName, password
            );

            //Close connection
            socket.close();

            return loginResult;

        } catch (SocketTimeoutException e) {

            return new LoginResult(
                    SessionResult.ERROR_CODE,
                    SessionHandler.SOCKET_TIMEOUT_ERROR_MSG,
                    Result.ERROR_CODE
            );

        } catch (IOException e) {

            return new LoginResult(
                    SessionResult.ERROR_CODE,
                    SessionHandler.IO_EXCEPTION_ERROR_MSG,
                    Result.ERROR_CODE
            );
        }
    }

    private static LoginResult handleLoginExchange(
            Socket socket, int timeOut, int ackCode,
            long sessionId, String userName, String password) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        System.out.println("\n\nLoginCMD Start");

        SessionResult sessionResult = getSessionResult(socket, timeOut, ackCode, sessionId);

        int readSessionStatus = sessionResult.getSessionStatus();

        if(readSessionStatus == SessionResult.ERROR_CODE){

            System.out.println("LoginCMD End\n");

            return new LoginResult(
                    sessionResult.getSessionStatus(),
                    sessionResult.getSessionMessage(),
                    Result.ERROR_CODE
            );

        }

        //Send login cmd
        System.out.println("\tSend LoginCMD");
        sendCommand(
                socket, dataIn, dataOut, timeOut,
                ackCode,
                XulaAtmServerCommands.LOGIN_CMD
        );

        //Send userName
        sendString(socket, timeOut, ackCode, userName);

        //Send password
        sendString(socket, timeOut, ackCode, password);

        //Send ACK
        sendACK(dataOut,ackCode);

        Result result = getResult(socket, timeOut, ackCode);

        System.out.println("LoginCMD End\n");

        if(result.getStatus() == Result.ERROR_CODE){
            return new LoginResult(
                    sessionResult.getSessionStatus(),
                    result.getStatus(),
                    result.getMessage()
            );
        }

        return new LoginResult(sessionResult.getSessionStatus(), result.getStatus());

    }

}
