import com.google.common.io.ByteStreams;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class Main {

    public static void main(String[] args) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        KeyPair rsaKeyPair = kpg.generateKeyPair();
        ServerSocket serverSocket = new ServerSocket(9000);
        Socket socket = serverSocket.accept();
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        byte[] encodedPubKey = rsaKeyPair.getPublic().getEncoded();
        dos.writeShort(encodedPubKey.length);
        dos.write(encodedPubKey);
        byte[] cipher = ByteStreams.toByteArray(socket.getInputStream());
        socket.close();
        Cipher c = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
        c.init(Cipher.DECRYPT_MODE, rsaKeyPair.getPrivate());
        byte[] plain = c.doFinal(cipher);
        System.out.println(new String(plain, StandardCharsets.UTF_8));
    }
}
