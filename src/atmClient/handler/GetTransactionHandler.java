package atmClient.handler;

import atmClient.XulaATMTransaction;
import atmClient.XulaAtmServerCommands;
import atmClient.result.GetTransactionResult;
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
import static atmClient.handler.ResultHandler.getResult;
import static atmClient.handler.SessionHandler.getSessionResult;
import static atmClient.handler.SocketHandler.openNewSocket;
import static atmClient.socketData.SocketDataReader.*;
import static atmClient.socketData.SocketDataWriter.getDataOutputStream;

public class GetTransactionHandler {

    public static GetTransactionResult handleGetTransaction(
            String ipAddress, int port, int timeOut, int ackCode,
            long sessionId, long accountId, long transactionId) {

        Socket socket;
        try {

            //Open a new socket Connection
            socket = openNewSocket(ipAddress, port, timeOut);

            GetTransactionResult getTransactionResult = handleGetTransactionExchange(
                    socket,
                    timeOut,
                    ackCode,
                    sessionId,
                    accountId, transactionId
            );

            //Close connection
            socket.close();

            return getTransactionResult;

        } catch (SocketTimeoutException e) {

            return new GetTransactionResult(
                    SessionResult.ERROR_CODE,
                    SessionHandler.SOCKET_TIMEOUT_ERROR_MSG,
                    Result.ERROR_CODE
            );

        } catch (IOException e) {

            return new GetTransactionResult(
                    SessionResult.ERROR_CODE,
                    SessionHandler.IO_EXCEPTION_ERROR_MSG,
                    Result.ERROR_CODE
            );

        }

    }

    private static  GetTransactionResult handleGetTransactionExchange(
            Socket socket, int timeOut, int ackCode,
            long sessionId, long accountId, long transactionId) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        System.out.println("\n\nGetTransactionCMD Start");

        SessionResult sessionResult = getSessionResult(socket, timeOut, ackCode, sessionId);

        int readSessionStatus = sessionResult.getSessionStatus();

        if(readSessionStatus <= SessionResult.ERROR_CODE){

            System.out.println("GetTransactionCMD End\n");

            return new GetTransactionResult(
                    sessionResult.getSessionStatus(),
                    sessionResult.getSessionMessage(),
                    Result.ERROR_CODE
            );

        }

        //Send Get Transaction cmd
        System.out.println("\tSent GetTransactionCMD");
        sendCommand(
                socket, dataIn, dataOut, timeOut,
                ackCode,
                XulaAtmServerCommands.GET_TRANSACTION_CMD
        );

        //Send AccountId
        dataOut.writeLong(accountId);
        System.out.println("\tSent AccountId: "+accountId);

        //Read ACK
        readACK(socket, dataIn, timeOut, ackCode);

        //Send transactionId
        dataOut.writeLong(transactionId);
        System.out.println("\tSent transactionId: "+transactionId);

        //Read ACK
        readACK(socket, dataIn, timeOut, ackCode);

        Result result = getResult(socket, timeOut, ackCode);
        if(result.getStatus() <= Result.ERROR_CODE){

            System.out.println("GetTransactionCMD End\n");

            return new GetTransactionResult(
                    sessionResult.getSessionStatus(),
                    result.getStatus(),
                    result.getMessage()
            );
        }

        //read Type
        int type = readIntWTimeout(socket,dataIn,timeOut);
        System.out.println("\tRead type: "+type);

        //Send ACK
        sendACK(dataOut,ackCode);

        //read amount
        double amount = readDoubleWTimeout(socket,dataIn,timeOut);
        System.out.println("\tRead amount: "+amount);

        //Send ACK
        sendACK(dataOut,ackCode);

        //read otherAccount
        String otherAccount = readString(socket,timeOut,ackCode);

        //read DateTime
        String dateTime = readString(socket,timeOut,ackCode);

        //read prevAmount
        double prevAmount = readDoubleWTimeout(socket,dataIn,timeOut);
        System.out.println("\tRead prevAmount: "+prevAmount);

        //Send ACK
        sendACK(dataOut,ackCode);

        XulaATMTransaction atmTransaction = new XulaATMTransaction(
                accountId,
                transactionId,
                amount,
                type,
                otherAccount,
                prevAmount,
                dateTime
        );

        System.out.println("GetTransactionCMD End\n");

        return new GetTransactionResult(
                sessionResult.getSessionStatus(),
                result.getStatus(),
                atmTransaction
        );

    }
}
