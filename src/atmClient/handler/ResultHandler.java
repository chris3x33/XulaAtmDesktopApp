package atmClient.handler;

import atmClient.result.Result;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static atmClient.SocketACK.readACK;
import static atmClient.SocketACK.sendACK;
import static atmClient.socketData.SocketDataReader.getDataInputStream;
import static atmClient.socketData.SocketDataReader.readBytesWTimeout;
import static atmClient.socketData.SocketDataReader.readIntWTimeout;
import static atmClient.socketData.SocketDataWriter.getDataOutputStream;

public class ResultHandler {

    public static Result getResult(Socket socket, int timeOut, int ackCode) throws IOException {

        DataInputStream dataIn = getDataInputStream(socket);
        DataOutputStream dataOut = getDataOutputStream(socket);

        //Read status result
        int readResultStatus = readIntWTimeout(
                socket,
                dataIn,
                timeOut
        );
        System.out.println("\tRead result status: "+readResultStatus);

        //Send ACK
        sendACK(dataOut,ackCode);

        if(readResultStatus <= Result.ERROR_CODE){

            //Get Result Msg Length in bytes
            int readResultMessageLen = readIntWTimeout(socket, dataIn, timeOut);
            System.out.println("\tRead Result Message Length");

            //Send ACK
            sendACK(dataOut,ackCode);

            //Get Result Msg
            byte[] readResultMessageBytes = readBytesWTimeout(
                    socket,
                    dataIn,
                    timeOut,
                    readResultMessageLen
            );
            String readResultMessage = new String(readResultMessageBytes);
            System.out.println("\tRead result Message: "+readResultMessage);

            //Send ACK
            sendACK(dataOut,ackCode);

            //Read ACK
            readACK(socket, dataIn, timeOut, ackCode);

            return new Result(
                    Result.ERROR_CODE,
                    readResultMessage
            );

        }

        //Read ACK
        readACK(socket, dataIn, timeOut, ackCode);

        return new Result(Result.SUCCESS_CODE);

    }

}
