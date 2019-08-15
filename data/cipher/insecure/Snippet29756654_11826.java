import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
public class RealEchoServer {

public static void main(String[] args) {
    int i = 1;
    try {
        ServerSocket s = new ServerSocket(9003);

        for (;;) {
            Socket incoming = s.accept();
            System.out.println("Spawning " + i);
            new RealEchoHandler(incoming, i).start();
            i++;
        }
    } catch (Exception e) {
        System.out.println(e);
    }
 }
}

class RealEchoHandler extends Thread {

DataInputStream in;
DataOutputStream out;
private Socket incoming;
private int counter;

public RealEchoHandler(Socket i, int c) {
    incoming = i;
    counter = c;
}

public void run() {
    try {

        String key1 = "1234567812345678";
        byte[] key2 = key1.getBytes();
        SecretKeySpec secret = new SecretKeySpec(key2, "AES");
        String msg = "Singapore Malaysia Japan India Indonesia HongKong Taiwan China England";
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] encrypted = cipher.doFinal(msg.getBytes());

        in = new DataInputStream(incoming.getInputStream());
        out = new DataOutputStream(incoming.getOutputStream());

        boolean done = false;
        String str = "";
        out.writeUTF("Connected!\n");
        out.flush();
        while (!done) {
            out.writeUTF(">");
            out.flush();
            str = in.readUTF();
            System.out.println(in + ":" + str);
            if (str == null) {
                done = true;
            } else {
                System.out.println("Sending Ciphertext : " + new String(encrypted));
                out.writeUTF(new String(encrypted));
                out.flush();
            }
        }
        incoming.close();
    } catch (Exception e) {
        System.out.println(e);
    }
 }
}
