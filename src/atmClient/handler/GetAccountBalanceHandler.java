package atmClient.handler;

import atmClient.XulaAtmServerCommands;
import atmClient.result.GetAccountBalanceResult;
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
import static atmClient.socketData.SocketDataReader.getDataInputStream;
import static atmClient.socketData.SocketDataReader.readDoubleWTimeout;
import static atmClient.socketData.SocketDataWriter.getDataOutputStream;

public class GetAccountBalanceHandler {

    public static GetAccountBalanceResult handleGetAccountBalance(
            String ipAddress, int port, int timeOut,
            int ackCode, long sessionId, long accountId) {

        Socket socket;
        try {

            //Open a new socket Connection
            socket = openNewSocket(ipAddress, port, timeOut);
            GetAccountBalanceResult getAccountBalanceResult = handleGetAccountBalanceExchange(
                    socket,
                    timeOut,
                    ackCode,
                    sessionId,
                    accountId
            );

            //Close connection
            socket.close();

            return getAccountBalanceResult;

        } catch (SocketTimeoutException e) {

            return new GetAccountBalanceResult(
                    SessionResult.ERROR_CODE,
                    SessionHandler.SOCKET_TIMEOUT_ERROR_MSG,
                    Result.ERROR_CODE
            );

        } catch (IOException e) {

            return new GetAccountBalanceResult(
                    SessionResult.ERROR_CODE,
                    SessionHandler.IO_EXCEPTION_ERROR_MSG,
                    Result.ERROR_CODE
            );
        }

    }

    private static GetAccountBalanceResult handleGetAccountBalanceExchange(
            Socket socket, int timeOut,int ackCode,
            long sessionId, long accountId) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        System.out.println("\n\nGetAccountBalanceCMD Start");

        SessionResult sessionResult = getSessionResult(socket, timeOut, ackCode, sessionId);

        int readSessionStatus = sessionResult.getSessionStatus();

        if(readSessionStatus <= SessionResult.ERROR_CODE){

            System.out.println("GetAccountBalanceCMD End\n");

            return new GetAccountBalanceResult(
                    sessionResult.getSessionStatus(),
                    sessionResult.getSessionMessage(),
                    Result.ERROR_CODE
            );

        }

        System.out.println("\tSent GetAccountBalanceCMD");
        sendCommand(
                socket, dataIn, dataOut, timeOut,
                ackCode,
                XulaAtmServerCommands.GET_ACCOUNT_BALANCE_CMD
        );

        //Send AccountId
        dataOut.writeLong(accountId);
        System.out.println("\tSent AccountId: "+accountId);

        //Read ACK
        readACK(socket, dataIn, timeOut, ackCode);

        //Send ACK
        sendACK(dataOut,ackCode);

        //Read Result
        Result result = getResult(socket, timeOut, ackCode);
        if(result.getStatus() == Result.ERROR_CODE){
            System.out.println("GetAccountBalanceCMD End\n");
            return new GetAccountBalanceResult(
                    sessionResult.getSessionStatus(),
                    result.getStatus(),
                    result.getMessage()
            );
        }

        //read accountBalance
        double accountBalance = readDoubleWTimeout(socket,dataIn,timeOut);
        System.out.println("\tRead accountBalance: "+accountBalance+"\n");

        System.out.println("GetAccountBalanceCMD End\n");

        return new GetAccountBalanceResult(
                sessionResult.getSessionStatus(),
                result.getStatus(),
                accountBalance
        );

    }

}
