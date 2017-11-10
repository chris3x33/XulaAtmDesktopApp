package atmClient.handler;

import atmClient.XulaAtmServerCommands;
import atmClient.result.Result;
import atmClient.result.SessionResult;
import atmClient.result.WithdrawResult;

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
import static atmClient.socketData.SocketDataReader.readString;
import static atmClient.socketData.SocketDataWriter.getDataOutputStream;

public class WithdrawHandler {

    public static WithdrawResult handleWithdraw(
            String ipAddress, int port, int timeOut, int ackCode,
            long sessionId, long fromAccountId, double amount) {

        Socket socket;
        try {

            //Open a new socket Connection
            socket = openNewSocket(ipAddress, port, timeOut);

            WithdrawResult withdrawResult = handleWithdrawExchange(
                    socket, timeOut, ackCode,
                    sessionId,
                    fromAccountId, amount
            );

            //Close connection
            socket.close();

            return withdrawResult;

        } catch (SocketTimeoutException e) {

            return new WithdrawResult(
                    SessionResult.ERROR_CODE,
                    SessionHandler.SOCKET_TIMEOUT_ERROR_MSG,
                    Result.ERROR_CODE
            );

        } catch (IOException e) {

            return new WithdrawResult(
                    SessionResult.ERROR_CODE,
                    SessionHandler.IO_EXCEPTION_ERROR_MSG,
                    Result.ERROR_CODE
            );
        }
    }

    private static WithdrawResult handleWithdrawExchange(
            Socket socket, int timeOut, int ackCode,
            long sessionId, long fromAccountId, double amount) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        System.out.println("\n\nWithdrawCMD Start");

        SessionResult sessionResult = getSessionResult(socket, timeOut, ackCode, sessionId);

        int readSessionStatus = sessionResult.getSessionStatus();

        if(readSessionStatus <= SessionResult.ERROR_CODE){

            System.out.println("WithdrawCMD End\n");

            return new WithdrawResult(
                    sessionResult.getSessionStatus(),
                    sessionResult.getSessionMessage(),
                    Result.ERROR_CODE
            );

        }

        //Send Withdraw cmd
        System.out.println("\tSend WithdrawCMD");
        sendCommand(
                socket, dataIn, dataOut, timeOut,
                ackCode,
                XulaAtmServerCommands.WITHDRAW_CMD
        );

        //Send fromAccountId
        dataOut.writeLong(fromAccountId);
        System.out.println("\tSent AccountId: "+fromAccountId);

        //Read ACK
        readACK(socket, dataIn, timeOut, ackCode);

        //Send amount
        dataOut.writeDouble(amount);
        System.out.println("\tSent amount: "+amount);

        //Read ACK
        readACK(socket, dataIn, timeOut, ackCode);

        //Send ACK
        sendACK(dataOut,ackCode);

        Result result = getResult(socket, timeOut, ackCode);
        if(result.getStatus() <= Result.ERROR_CODE){
            return new WithdrawResult(
                    sessionResult.getSessionStatus(),
                    result.getStatus(),
                    result.getMessage()
            );
        }

        WithdrawResult withdrawResult = new WithdrawResult(
                sessionResult.getSessionStatus(),
                result.getStatus(), result.getMessage()
        );

        //Send ACK
        sendACK(dataOut,ackCode);

        //Read withdrawMsg
        String withdrawMsg = readString(socket, timeOut, ackCode);
        withdrawResult.setWithdrawMsg(withdrawMsg);

        System.out.println("WithdrawCMD End\n");

        return withdrawResult;

    }

}
