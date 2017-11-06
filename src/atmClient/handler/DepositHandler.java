package atmClient.handler;

import atmClient.XulaAtmServerCommands;
import atmClient.result.DepositResult;
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
import static atmClient.socketData.SocketDataReader.readString;
import static atmClient.socketData.SocketDataWriter.getDataOutputStream;
import static atmClient.socketData.SocketDataWriter.sendString;

public class DepositHandler {

    public static DepositResult handleDeposit(
            String ipAddress, int port, int timeOut, int ackCode,
            long sessionId, long toAccountId, double amount) {

        Socket socket;
        try {

            //Open a new socket Connection
            socket = openNewSocket(ipAddress, port, timeOut);

            DepositResult depositResult = handleDepositExchange(
                    socket, timeOut, ackCode,
                    sessionId,
                    toAccountId, amount
            );

            //Close connection
            socket.close();

            return depositResult;

        } catch (SocketTimeoutException e) {

            return new DepositResult(
                    SessionResult.ERROR_CODE,
                    SessionHandler.SOCKET_TIMEOUT_ERROR_MSG,
                    Result.ERROR_CODE
            );

        } catch (IOException e) {

            return new DepositResult(
                    SessionResult.ERROR_CODE,
                    SessionHandler.IO_EXCEPTION_ERROR_MSG,
                    Result.ERROR_CODE
            );
        }
    }

    private static DepositResult handleDepositExchange(
            Socket socket, int timeOut, int ackCode,
            long sessionId, long toAccountId, double amount) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        System.out.println("\n\nDepositCMD Start");

        SessionResult sessionResult = getSessionResult(socket, timeOut, ackCode, sessionId);

        int readSessionStatus = sessionResult.getSessionStatus();

        if(readSessionStatus <= SessionResult.ERROR_CODE){

            System.out.println("DepositCMD End\n");

            return new DepositResult(
                    sessionResult.getSessionStatus(),
                    sessionResult.getSessionMessage(),
                    Result.ERROR_CODE
            );

        }

        //Send Deposit cmd
        System.out.println("\tSend DepositCMD");
        sendCommand(
                socket, dataIn, dataOut, timeOut,
                ackCode,
                XulaAtmServerCommands.DEPOSIT_CMD
        );

        //Send toAccountId
        dataOut.writeLong(toAccountId);
        System.out.println("\tSent AccountId: "+toAccountId);

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
            return new DepositResult(
                    sessionResult.getSessionStatus(),
                    result.getStatus(),
                    result.getMessage()
            );
        }

        DepositResult depositResult = new DepositResult(
                sessionResult.getSessionStatus(),
                result.getStatus(), result.getMessage()
        );

        //Send ACK
        sendACK(dataOut,ackCode);

        //Read depositMsg
        String depositMsg = readString(socket, timeOut, ackCode);
        depositResult.setDepositMsg(depositMsg);

        System.out.println("DepositCMD End\n");

        return depositResult;

    }

}
