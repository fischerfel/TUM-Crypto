import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EchoClientTest {

    public static void main(String[] args) {
        try {
            byte[] keyB = {
                    (byte)0x2C, (byte)0xCF, (byte)0xBC, (byte)0x81, (byte)0x99, (byte)0xB7, (byte)0x37, (byte)0x84, 
                    (byte)0xD1, (byte)0x09, (byte)0x44, (byte)0x4F, (byte)0xB6, (byte)0x66, (byte)0x69, (byte)0x1F, 
                    (byte)0x17, (byte)0x61, (byte)0xAD, (byte)0xD7, (byte)0x43, (byte)0x0E, (byte)0x80, (byte)0x6C                      
            };
            byte[] ivB = {
                    (byte)0x7B, (byte)0x9B, (byte)0x5F, (byte)0x36, (byte)0xC9, (byte)0x26, (byte)0xE9, (byte)0x5E                      
            };


            {
                Socket echoSocket = new Socket("127.0.0.1",1423);
                System.out.println("Connected");

                System.out.println("Setting up - Unencrypted version");
                OutputStream oS = echoSocket.getOutputStream();
                DataOutputStream doS = new DataOutputStream(oS);
                InputStream iS = echoSocket.getInputStream();
                InputStreamReader iSR = new InputStreamReader(iS,"UTF-8");
                BufferedReader bR = new BufferedReader(iSR);


                System.out.println("Writing Unencrypted output");
                doS.write("Test Message\n".getBytes("UTF-8"));
                doS.flush();

                System.out.println("Waiting 1 second");
                Thread.sleep(1000);

                System.out.println("Reading data from socket");

                String inputLine = "";
                while (bR.ready()) {
                    int i = bR.read();
                    inputLine += (char)i;
                }
                if (inputLine.length()==0) {
                    System.out.println("Error nothing read from socket");
                } else {
                    System.out.println("Recieved:" + inputLine);
                    inputLine = "";
                };

                System.out.println("Closing Socket");
                echoSocket.close();
            }
            System.out.println("Repeating but encrypting the data SENT on the socket");
            {
                Socket echoSocket = new Socket("127.0.0.1",1423);
                System.out.println("Connected");

                System.out.println("Setting up Ciphers");
                String Algo = "DESede"; //DES or DESede

                final SecretKey key = new SecretKeySpec(keyB, Algo);
                final IvParameterSpec iv = new IvParameterSpec(ivB);
                final Cipher cipherEncrypt = Cipher.getInstance("DESede/CFB8/NoPadding");
                cipherEncrypt.init(Cipher.ENCRYPT_MODE, key, iv);      

                System.out.println("Setting up - Encrypted version");
                OutputStream oS = echoSocket.getOutputStream();
                CipherOutputStream cipherOutputS = new CipherOutputStream(oS,cipherEncrypt);
                DataOutputStream doS = new DataOutputStream(cipherOutputS);
                InputStream iS = echoSocket.getInputStream();
                InputStreamReader iSR = new InputStreamReader(iS,"UTF-8");
                BufferedReader bR = new BufferedReader(iSR);


                System.out.println("Writing Encrypted output");
                doS.write("Test Message\n".getBytes("UTF-8"));
                doS.flush();

                System.out.println("Waiting 1 second");
                Thread.sleep(1000);

                System.out.println("Reading data from socket");

                String inputLine = "";
                while (bR.ready()) {
                    int i = bR.read();
                    inputLine += (char)i;
                }
                if (inputLine.length()==0) {
                    System.out.println("Error nothing read from socket");
                } else {
                    System.out.println("Recieved:" + inputLine);
                    inputLine = "";
                };

                System.out.println("Closing Socket");
                echoSocket.close();
            }
            System.out.println("Repeating but encrypting the data SENT and decrypting RECIEVED on the socket");
            {
                Socket echoSocket = new Socket("127.0.0.1",1423);
                System.out.println("Connected");

                System.out.println("Setting up Ciphers");
                String Algo = "DESede"; //DES or DESede

                final SecretKey key = new SecretKeySpec(keyB, "DESede");
                final IvParameterSpec iv = new IvParameterSpec(ivB);
                final Cipher cipherEncrypt = Cipher.getInstance("DESede/CFB8/NoPadding");
                final Cipher cipherDecrypt = Cipher.getInstance("DESede/CFB8/NoPadding");
                cipherEncrypt.init(Cipher.ENCRYPT_MODE, key, iv);      
                cipherDecrypt.init(Cipher.DECRYPT_MODE, key, iv);      

                System.out.println("Setting up - Encrypted version");
                OutputStream oS = echoSocket.getOutputStream();
                CipherOutputStream cipherOutputS = new CipherOutputStream(oS,cipherEncrypt);
                DataOutputStream doS = new DataOutputStream(cipherOutputS);
                InputStream iS = echoSocket.getInputStream();
                CipherInputStream cipherInputS =  new CipherInputStream(iS,cipherDecrypt);
                InputStreamReader iSR = new InputStreamReader(cipherInputS,"UTF-8");
                BufferedReader bR = new BufferedReader(iSR);


                System.out.println("Writing Encrypted output");
                doS.write("Test Message\n".getBytes("UTF-8"));
                doS.flush();

                System.out.println("Waiting 1 second");
                Thread.sleep(1000);

                System.out.println("Reading data from socket (and Decrtpying)");

                String inputLine = "";
                while (bR.ready()) {
                    int i = bR.read();
                    inputLine += (char)i;
                }
                if (inputLine.length()==0) {
                    System.out.println("Error nothing read from socket");
                } else {
                    System.out.println("Recieved:" + inputLine);
                    inputLine = "";
                };

                System.out.println("Closing Socket");
                echoSocket.close();
            }           

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
