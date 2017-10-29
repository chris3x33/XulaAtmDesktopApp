package atmClient.handler;

import atmClient.XulaAtmServerCommands;
import atmClient.result.GetAccountIdsResult;
import atmClient.result.Result;
import atmClient.result.SessionResult;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import static atmClient.SocketACK.sendACK;
import static atmClient.handler.CommandHandler.sendCommand;
import static atmClient.handler.ResultHandler.getResult;
import static atmClient.handler.SessionHandler.getSessionResult;
import static atmClient.socketData.SocketDataReader.getDataInputStream;
import static atmClient.socketData.SocketDataReader.readLongs;
import static atmClient.socketData.SocketDataWriter.getDataOutputStream;

public class GetAccountIdsHandler {

    public static GetAccountIdsResult handleGetAccountIdsExchange(Socket socket, int timeOut, int ackCode, long sessionId) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        System.out.println("\n\nGetAccountIdsCMD Start");

        SessionResult sessionResult = getSessionResult(socket, timeOut, ackCode, sessionId);

        int readSessionStatus = sessionResult.getSessionStatus();

        if(readSessionStatus == SessionResult.ERROR_CODE){

            System.out.println("GetAccountIdsCMD End\n");

            return new GetAccountIdsResult(
                    sessionResult.getSessionStatus(),
                    sessionResult.getSessionMessage(),
                    Result.ERROR_CODE
            );

        }

        //Send Get AccountIds cmd
        System.out.println("\tSent GetAccountIdsCMD");
        sendCommand(
                socket, dataIn, dataOut, timeOut,
                ackCode,
                XulaAtmServerCommands.GET_USER_ACCOUNTIDS_CMD
        );

        //Send ACK
        sendACK(dataOut,ackCode);

        Result result = getResult(socket, timeOut, ackCode);
        if(result.getStatus() == Result.ERROR_CODE){

            System.out.println("GetAccountIdsCMD End\n");

            return new GetAccountIdsResult(
                    sessionResult.getSessionStatus(),
                    result.getStatus(),
                    result.getMessage()
            );
        }

        //read accountIDs
        ArrayList<Long> accountIDs = readLongs(socket,timeOut,ackCode);
        System.out.println("\tRead accountIDs: "+accountIDs+"\n");

        System.out.println("GetAccountIdsCMD End\n");

        return new GetAccountIdsResult(
                sessionResult.getSessionStatus(),
                result.getStatus(),
                accountIDs
        );

    }
}
