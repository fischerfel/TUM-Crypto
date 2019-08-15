import org.junit.Test;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.io.BufferedWriter;



public class TestFlushableCipher {
    private static byte[] keyBytes = new byte[]{
            // Change these numbers lest other StackOverflow readers can read your log files
            -53, 93, 59, 108, -34, 17, -72, -33, 126, 93, -62, -50, 106, -44, 17, 55
    };
    private static SecretKeySpec key = new SecretKeySpec(keyBytes,"AES");
    private static int HEADER_LENGTH = 16;


    private static BufferedWriter flushableEncryptedBufferedWriter(File file, boolean append) throws Exception
    {
        FlushableCipherOutputStream fcos = new FlushableCipherOutputStream(file, key, append, false);
        return new BufferedWriter(new OutputStreamWriter(fcos, "UTF-8"));
    }

    private static InputStream readerEncryptedByteStream(File file) throws Exception
    {
        FileInputStream fin = new FileInputStream(file);
        byte[] iv = new byte[16];
        byte[] headerBytes = new byte[HEADER_LENGTH];
        if (fin.read(headerBytes) < HEADER_LENGTH)
            throw new IllegalArgumentException("Invalid file length (failed to read file header)");
        if (headerBytes[0] != 100)
            throw new IllegalArgumentException("The file header does not conform to our encrypted format.");
        if (fin.read(iv) < 16) {
            throw new IllegalArgumentException("Invalid file length (needs a full block for iv)");
        }
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        return new CipherInputStream(fin,cipher);
    }

    private static BufferedReader readerEncrypted(File file) throws Exception
    {
        InputStream cis = readerEncryptedByteStream(file);
        return new BufferedReader(new InputStreamReader(cis));
    }

    @Test
    public void test() throws Exception {
        File zfilename = new File("c:\\WebEdvalData\\log.x");

        BufferedWriter cos = flushableEncryptedBufferedWriter(zfilename, false);
        cos.append("Sunny ");
        cos.append("and green.  \n");
        cos.close();

        int spaces=0;
        for (int i = 0; i<10; i++) {
            cos = flushableEncryptedBufferedWriter(zfilename, true);
            for (int j=0; j < 2; j++) {
                cos.append("Karelia and Tapiola" + i);
                for (int k=0; k < spaces; k++)
                    cos.append(" ");
                spaces++;
                cos.append("and other nice things.  \n");
                cos.flush();
                tail(zfilename);
            }
            cos.close();
        }

        BufferedReader cis = readerEncrypted(zfilename);
        String msg;
        while ((msg=cis.readLine()) != null) {
            System.out.println(msg);
        }
        cis.close();
    }

    private void tail(File filename) throws Exception
    {
        BufferedReader infile = readerEncrypted(filename);
        String last = null, secondLast = null;
        do {
            String msg = infile.readLine();
            if (msg == null)
                break;
            if (! msg.startsWith("}")) {
                secondLast = last;
                last = msg;
            }
        } while (true);
        if (secondLast != null)
            System.out.println(secondLast);
        System.out.println(last);
        System.out.println();
    }
}
