import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.Charset;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class UDP_AES {


    /**
     * @param args
     * @throws SocketException 
     */
    public static void main(String[] args) throws SocketException {


        Thread t = new Thread(new Runnable() {
            DatagramSocket socket;
            byte[] receiveData = new byte[1024];
            Cipher aesCipher;

            {
                try {
                    socket = new DatagramSocket(18000);
                    aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    IvParameterSpec zeroIV = new IvParameterSpec(new byte[aesCipher.getBlockSize()]);
                    SecretKey key = new SecretKeySpec(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 7,}, "AES");
                    aesCipher.init(Cipher.DECRYPT_MODE, key, zeroIV);
                } catch (Exception e) {
                    throw new IllegalStateException("Could not create server socket or cipher", e);
                }

            }

            @Override
            public void run() {
                while (true) {
                    DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);

                    try {
                        socket.receive(packet);
                        final byte[] plaintextBinary = aesCipher.doFinal(packet.getData(), 0, packet.getLength());
                        String plaintext = new String(plaintextBinary, Charset.forName("UTF-8"));
                        System.out.println(plaintext);
                    } catch (Exception e) {
                        throw new IllegalStateException("Could not receive or decrypt packet");
                    }
                }
            }

        });
        t.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // yeah, whatever
        }

        DatagramSocket sendingSocket = new DatagramSocket();
        String plaintext = "1234";
        byte[] plaintextBinary = plaintext.getBytes(Charset.forName("UTF-8"));

        try {
            Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKey key = new SecretKeySpec(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 7,}, "AES");
            IvParameterSpec zeroIV = new IvParameterSpec(new byte[aesCipher.getBlockSize()]);
            aesCipher.init(Cipher.ENCRYPT_MODE, key, zeroIV);
            final byte[] ciphertextBinary = aesCipher.doFinal(plaintextBinary);

        DatagramPacket sendingPacket = new DatagramPacket(ciphertextBinary, ciphertextBinary.length);
        sendingPacket.setSocketAddress(new InetSocketAddress("localhost", 18000));
            sendingSocket.send(sendingPacket);
        } catch (Exception e) {
            throw new IllegalStateException("Could not send or encrypt", e);
        }
    }

}
