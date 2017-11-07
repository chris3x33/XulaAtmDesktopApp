package atmClient.handler;

import atmClient.XulaAtmServerCommands;
import atmClient.result.GetTransactionIdsResult;
import atmClient.result.Result;
import atmClient.result.SessionResult;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import static atmClient.SocketACK.readACK;
import static atmClient.handler.CommandHandler.sendCommand;
import static atmClient.handler.ResultHandler.getResult;
import static atmClient.handler.SessionHandler.getSessionResult;
import static atmClient.handler.SocketHandler.openNewSocket;
import static atmClient.socketData.SocketDataReader.getDataInputStream;
import static atmClient.socketData.SocketDataReader.readLongs;
import static atmClient.socketData.SocketDataWriter.getDataOutputStream;

public class GetTransactionIdsHandler {

    public static GetTransactionIdsResult handleGetTransactionIds(
            String ipAddress, int port, int timeOut, int ackCode,
            long sessionId, long accountId) {

        Socket socket;
        try {

            //Open a new socket Connection
            socket = openNewSocket(ipAddress, port, timeOut);

            GetTransactionIdsResult getTransactionIdsResult = handleGetTransactionIdsExchange(
                    socket,
                    timeOut,
                    ackCode,
                    sessionId,
                    accountId
            );

            //Close connection
            socket.close();

            return getTransactionIdsResult;

        } catch (SocketTimeoutException e) {

            return new GetTransactionIdsResult(
                    SessionResult.ERROR_CODE,
                    SessionHandler.SOCKET_TIMEOUT_ERROR_MSG,
                    Result.ERROR_CODE
            );

        } catch (IOException e) {

            return new GetTransactionIdsResult(
                    SessionResult.ERROR_CODE,
                    SessionHandler.IO_EXCEPTION_ERROR_MSG,
                    Result.ERROR_CODE
            );

        }

    }

    private static  GetTransactionIdsResult handleGetTransactionIdsExchange(
            Socket socket, int timeOut, int ackCode,
            long sessionId, long accountId) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        System.out.println("\n\nGetTransactionIdsCMD Start");

        SessionResult sessionResult = getSessionResult(socket, timeOut, ackCode, sessionId);

        int readSessionStatus = sessionResult.getSessionStatus();

        if(readSessionStatus <= SessionResult.ERROR_CODE){

            System.out.println("GetTransactionIdsCMD End\n");

            return new GetTransactionIdsResult(
                    sessionResult.getSessionStatus(),
                    sessionResult.getSessionMessage(),
                    Result.ERROR_CODE
            );

        }

        //Send Get TransactionIds cmd
        System.out.println("\tSent GetTransactionIdsCMD");
        sendCommand(
                socket, dataIn, dataOut, timeOut,
                ackCode,
                XulaAtmServerCommands.GET_TRANSACTIONIDS_CMD
        );

        //Send AccountId
        dataOut.writeLong(accountId);
        System.out.println("\tSent AccountId: "+accountId);

        //Read ACK
        readACK(socket, dataIn, timeOut, ackCode);

        Result result = getResult(socket, timeOut, ackCode);
        if(result.getStatus() <= Result.ERROR_CODE){

            System.out.println("GetTransactionIdsCMD End\n");

            return new GetTransactionIdsResult(
                    sessionResult.getSessionStatus(),
                    result.getStatus(),
                    result.getMessage()
            );
        }

        //read transactionIds
        ArrayList<Long> transactionIds = readLongs(socket,timeOut,ackCode);
        System.out.println("\tRead transactionIds: "+transactionIds+"\n");

        System.out.println("GetTransactionIdsCMD End\n");

        return new GetTransactionIdsResult(
                sessionResult.getSessionStatus(),
                result.getStatus(),
                transactionIds
        );

    }
}
