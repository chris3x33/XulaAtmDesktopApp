package atmClient;

import java.io.DataOutputStream;
import java.io.IOException;

public class SocketACK {

    public final static int ACK_CODE = 10101010;

    public static void printACKResult(int ackCode, int ackTest){

        if (ackTest == ackCode){
            System.out.println("\tRead ACK");
        }else {
            System.out.println("\tACK Read Error");
        }

    }

    public static void sendACK(DataOutputStream dataOut, int ack) throws IOException {

        dataOut.writeInt(ack);
        System.out.println("\tSent ACK");

    }

}
