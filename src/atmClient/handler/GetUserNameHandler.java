package atmClient.handler;

import atmClient.XulaAtmServerCommands;
import atmClient.result.GetUserNameResult;
import atmClient.result.Result;
import atmClient.result.SessionResult;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static atmClient.SocketACK.sendACK;
import static atmClient.handler.CommandHandler.sendCommand;
import static atmClient.handler.ResultHandler.getResult;
import static atmClient.handler.SessionHandler.getSessionResult;
import static atmClient.socketData.SocketDataReader.getDataInputStream;
import static atmClient.socketData.SocketDataReader.readString;
import static atmClient.socketData.SocketDataWriter.getDataOutputStream;

public class GetUserNameHandler {

    public static GetUserNameResult handleGetUserNameExchange(Socket socket, int timeOut, int ackCode, long sessionId) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        System.out.println("\n\nGetUserNameCMD Start");

        SessionResult sessionResult = getSessionResult(socket, timeOut, ackCode, sessionId);

        int readSessionStatus = sessionResult.getSessionStatus();

        if(readSessionStatus == SessionResult.ERROR_CODE){

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
        if (result.getStatus() == Result.ERROR_CODE){

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
