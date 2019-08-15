   import java.io.*;
   import java.net.*;
   import java.security.*;
   import javax.crypto.*;
   import javax.crypto.spec.*;
   import java.util.*;

 class RealSocketTest {
 public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

    String str = "";
    String str2 = "";
    DataOutputStream out;
    DataInputStream in;

    try {
        Socket t = new Socket("127.0.0.1", 9003);
        in = new DataInputStream(t.getInputStream());
        out = new DataOutputStream(t.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        boolean more = true;
        System.out.println(in.readUTF());

        while (more) {
            str = in.readUTF();
            System.out.print(str);
            str2 = br.readLine();
            out.writeUTF(str2);
            out.flush();
            str = in.readUTF();

            System.out.println("Encrypted Info: " + str);

            try {

                String key1 = "1234567812345678";
                byte[] key2 = key1.getBytes();
                SecretKeySpec secret = new SecretKeySpec(key2, "AES");
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, secret);
                byte[] decrypted = cipher.doFinal(str.getBytes());
                System.out.println("Decrypted Info: " + new String(decrypted));
            } catch (BadPaddingException e) {
                System.out.println("Wrong Key!");
            } catch (InvalidKeyException f) {
                System.out.println("Invalid Key!");
            }
        }
    } catch (IOException e) {
        System.out.println("Error");
    }
  }
}
