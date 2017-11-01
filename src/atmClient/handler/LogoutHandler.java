package atmClient.handler;

import atmClient.XulaAtmServerCommands;
import atmClient.result.LogOutResult;
import atmClient.result.Result;
import atmClient.result.SessionResult;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

import static atmClient.SocketACK.readACK;
import static atmClient.SocketACK.sendACK;
import static atmClient.handler.CommandHandler.sendCommand;
import static atmClient.handler.SessionHandler.getSessionResult;
import static atmClient.handler.SocketHandler.openNewSocket;
import static atmClient.socketData.SocketDataReader.getDataInputStream;
import static atmClient.socketData.SocketDataWriter.getDataOutputStream;

public class LogoutHandler {

    public static LogOutResult handleLogout(
            String ipAddress, int port, int timeOut, int ackCode, long sessionId) {

        Socket socket;
        try {

            //Open a new socket Connection
            socket = openNewSocket(ipAddress, port, timeOut);

            LogOutResult logOutResult = handleLogoutExchange(
                    socket,
                    timeOut,
                    ackCode,
                    sessionId
            );

            //Close connection
            socket.close();

            return logOutResult;

        } catch (SocketTimeoutException e) {

            return new LogOutResult(
                    SessionResult.ERROR_CODE,
                    SessionHandler.SOCKET_TIMEOUT_ERROR_MSG,
                    Result.ERROR_CODE
            );

        } catch (IOException e) {

            return new LogOutResult(
                    SessionResult.ERROR_CODE,
                    SessionHandler.IO_EXCEPTION_ERROR_MSG,
                    Result.ERROR_CODE
            );

        }

    }

    private static LogOutResult handleLogoutExchange(
            Socket socket, int timeOut, int ackCode, long sessionId) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        System.out.println("\n\nLogoutCMD Start");

        SessionResult sessionResult = getSessionResult(socket, timeOut, ackCode, sessionId);

        int readSessionStatus = sessionResult.getSessionStatus();

        if(readSessionStatus <= SessionResult.ERROR_CODE){

            System.out.println("LogoutCMD End\n");

            return new LogOutResult(
                    sessionResult.getSessionStatus(),
                    sessionResult.getSessionMessage(),
                    Result.ERROR_CODE
            );

        }

        //Send Logout cmd
        System.out.println("\tSent LogoutCMD");
        sendCommand(
                socket, dataIn, dataOut, timeOut,
                ackCode,
                XulaAtmServerCommands.LOGOUT_CMD
        );

        //Send ACK
        sendACK(dataOut,ackCode);

        //Read ACK
        readACK(socket, dataIn, timeOut, ackCode);

        System.out.println("LogoutCMD End\n");

        return new LogOutResult(SessionResult.SUCCESS_CODE, Result.SUCCESS_CODE);
    }

}
