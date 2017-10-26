package atmClient.result;

public class ACKResult {

    public static void printACKResult(int ackCode, int ackTest){

        if (ackTest == ackCode){
            System.out.println("\tRead ACK");
        }else {
            System.out.println("\tACK Read Error");
        }

    }

}
