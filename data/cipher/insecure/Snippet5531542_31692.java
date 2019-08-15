import java.io.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

class Blowfish {
    public static void main(String[] args) throws Exception {
        String s;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Cipher encrypt = Cipher.getInstance("DES");
        Cipher decrypt = Cipher.getInstance("DES");
        System.out.print("Enter the key: ");
        s = br.readLine();
        /*
         * Names of algorithms used "Blowfish" "DES" 64 bit key ie. 8 bytes
         * "AES" key size has to be 16 bytes ie. 128 bits
         */

        byte key[] = new byte[8];
        for (int i = 0; i < s.length() && i < 8; i++)
            key[i] = (byte) s.charAt(i);

        encrypt.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "DES"));
        FileInputStream fin = new FileInputStream("test.txt");
        FileOutputStream out = new FileOutputStream("encrypted.p4e");
        CipherOutputStream cout = new CipherOutputStream(out, encrypt);

        int input = 0;
        while ((input = fin.read()) != -1) {
            cout.write(input);
        }

        out.close();
        cout.close();
        System.out.println("Starting the decryption");
        System.out.print("Enter the key: ");
        s = br.readLine();

        byte key2[] = new byte[8];
        for (int i = 0; i < s.length() && i < 8; i++)
            key2[i] = (byte) s.charAt(i);

        decrypt.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key2, "DES"));
        fin = new FileInputStream("encrypted.p4e");
        out = new FileOutputStream("test2.txt");
        CipherInputStream in = new CipherInputStream(fin, decrypt);
        input = 0;
        while ((input = in.read()) != -1) {
            out.write(input);
        }
        out.close();
        in.close();
    }
}
