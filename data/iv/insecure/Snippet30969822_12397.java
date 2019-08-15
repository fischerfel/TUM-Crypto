import java.io.*;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;

class RunnableDemo implements Runnable {
   private Thread t;
   private String threadName;
   private long[] password_aes;
   private String uh = "";
   private static final char[] CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
   private static final int[] IA = new int[256];
   private String fileName = "C://combo_encrypted.txt";



    static {
        Arrays.fill(IA, -1);
        for (int i = 0, iS = CA.length; i < iS; i++)
            IA[CA[i]] = i;
        IA['='] = 0;
    }

   RunnableDemo( String name){
       threadName = name;
   }

   public void run() {


   FileWriter fileWriter;
try {
    fileWriter = new FileWriter(fileName, true);
    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

    String finalString = "";
    String[] parts = threadName.split(":");
    password_aes = prepare_key_pw(parts[1]);
    uh = stringhash(parts[0], password_aes);
    finalString = (parts[0] + ":" + parts[1] + ":" + uh + "\n");
    System.out.println(finalString);
    bufferedWriter.write(finalString);
} catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
}

   }



   public void start ()
   {
      System.out.println("Starting " +  threadName );
      if (t == null)
      {
         t = new Thread (this, threadName);
         t.start ();
      }
   }


public static long[] str_to_a32(String string) {
    if (string.length() % 4 != 0) {
        string += new String(new char[4 - string.length() % 4]);
    }
    long[] data = new long[string.length() / 4];

    byte[] part = new byte[8];
    for (int k = 0, i = 0; i < string.length(); i += 4, k++) {
        String sequence = string.substring(i, i + 4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.write(sequence.getBytes("ISO-8859-1"));
            System.arraycopy(baos.toByteArray(), 0, part, 4, 4);
            ByteBuffer bb = ByteBuffer.wrap(part);
            data[k] = bb.getLong();
        } catch (IOException e) {
            data[k] = 0;
        }
    }
    return data;
}

public static String a32_to_str(long[] data) {
    byte[] part = null;
    StringBuilder builder = new StringBuilder();
    ByteBuffer bb = ByteBuffer.allocate(8);
    for (int i = 0; i < data.length; i++) {
        bb.putLong(data[i]);
        part = Arrays.copyOfRange(bb.array(), 4, 8);
        bb.clear();
        ByteArrayInputStream bais = new ByteArrayInputStream(part);
        while (bais.available() > 0) {
            builder.append((char) bais.read());
        }
    }
    return builder.toString();
}

public static byte[] aes_cbc_encrypt(byte[] data, byte[] key) {
    String iv = "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";
    IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
    byte[] output = null;
    try {
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        output = cipher.doFinal(data);

    } catch (Exception e) {
        e.printStackTrace();
    }
    return output;
}

public static long[] aes_cbc_encrypt_a32(long[] idata, long[] ikey) {
    try {
        byte[] data = a32_to_str(idata).getBytes("ISO-8859-1");
        byte[] key = a32_to_str(ikey).getBytes("ISO-8859-1");
        byte[] encrypt = aes_cbc_encrypt(data, key);

        return str_to_a32(new String(encrypt, "ISO-8859-1"));

    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
    return new long[0];
}

public static String base64_url_encode(String data) {

    try {
        data = new String(base64_url_encode_byte((data.getBytes("ISO-8859-1")),true), "ISO-8859-1");
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
    data = data.replaceAll("\\+", "-");
    data = data.replaceAll("/", "_");
    data = data.replaceAll("=", "");

    return data;
}

public static String a32_to_base64(long[] a) {
    return base64_url_encode(a32_to_str(a));
}

public static String stringhash(String email, long[] aeskey) {
    long[] s32 = str_to_a32(email);
    long[] h32 = {0, 0, 0, 0};
    for (int i = 0; i < s32.length; i++) {
        h32[i % 4] ^= s32[i];
    }
    for (int r = 0; r < 0x4000; r++) {
        h32 = aes_cbc_encrypt_a32(h32, aeskey);
    }
    long[] h32Part = new long[2];
    h32Part[0] = h32[0];
    h32Part[1] = h32[2];
    return a32_to_base64(h32Part);
}

public static long[] prepare_key(long[] password) {
    long[] pkey = {0x93C467E3, 0x7DB0C7A4, 0xD1BE3F81, 0x0152CB56};
    for (int r = 0; r < 0x10000; r++) {
        for (int j = 0; j < password.length; j += 4) {
            long[] key = {0, 0, 0, 0};
            for (int i = 0; i < 4; i++) {
                if (i + j < password.length) {
                    key[i] = password[i + j];
                }
            }
            pkey = aes_cbc_encrypt_a32(pkey, key);
        }
    }
    return pkey;}

public static long[] prepare_key_pw(String password) {
    return prepare_key(str_to_a32(password));
}

   public final static byte[] base64_url_encode_byte(byte[] sArr, boolean lineSep){
        // Check special case
        int sLen = sArr != null ? sArr.length : 0;
        if (sLen == 0)
            return new byte[0];

        int eLen = (sLen / 3) * 3;                              // Length of even 24-bits.
        int cCnt = ((sLen - 1) / 3 + 1) << 2;                   // Returned character count
        int dLen = cCnt + (lineSep ? (cCnt - 1) / 76 << 1 : 0); // Length of returned array
        byte[] dArr = new byte[dLen];

        // Encode even 24-bits
        for (int s = 0, d = 0, cc = 0; s < eLen;) {
            // Copy next three bytes into lower 24 bits of int, paying attension to sign.
            int i = (sArr[s++] & 0xff) << 16 | (sArr[s++] & 0xff) << 8 | (sArr[s++] & 0xff);

            // Encode the int into four chars
            dArr[d++] = (byte) CA[(i >>> 18) & 0x3f];
            dArr[d++] = (byte) CA[(i >>> 12) & 0x3f];
            dArr[d++] = (byte) CA[(i >>> 6) & 0x3f];
            dArr[d++] = (byte) CA[i & 0x3f];

            // Add optional line separator
            if (lineSep && ++cc == 19 && d < dLen - 2) {
                dArr[d++] = '\r';
                dArr[d++] = '\n';
                cc = 0;

            }
        }

        // Pad and encode last bits if source isn't an even 24 bits.
        int left = sLen - eLen; // 0 - 2.
        if (left > 0) {
            // Prepare the int
            int i = ((sArr[eLen] & 0xff) << 10) | (left == 2 ? ((sArr[sLen - 1] & 0xff) << 2) : 0);

            // Set last four chars
            dArr[dLen - 4] = (byte) CA[i >> 12];
            dArr[dLen - 3] = (byte) CA[(i >>> 6) & 0x3f];
            dArr[dLen - 2] = left == 2 ? (byte) CA[i & 0x3f] : (byte) '=';
            dArr[dLen - 1] = '=';
        }
        return dArr;
    }

}


public class TestThread {


   final static String OUTPUT_FILE_NAME = "C:\\combo_encrypted.txt";

   public static void main(String args[]) throws IOException {





  File file1 = new File("File1.txt");
  File file2 = new File("File2.txt");
  File file3 = new File("File3.txt");
  File file4 = new File("File4.txt");

  FileInputStream fis1 = null;
  FileInputStream fis2 = null;
  FileInputStream fis3 = null;
  FileInputStream fis4 = null;

  BufferedInputStream bis1 = null;
  BufferedInputStream bis2 = null;
  BufferedInputStream bis3 = null;
  BufferedInputStream bis4 = null;

  DataInputStream dis1 = null;
  DataInputStream dis2 = null;
  DataInputStream dis3 = null;
  DataInputStream dis4 = null;

  fis1 = new FileInputStream(file1);    
  fis2 = new FileInputStream(file2);
  fis3 = new FileInputStream(file3);
  fis4 = new FileInputStream(file4);

  bis1 = new BufferedInputStream(fis1);
  bis2 = new BufferedInputStream(fis2);
  bis3 = new BufferedInputStream(fis3);
  bis4 = new BufferedInputStream(fis4);

  dis1 = new DataInputStream(bis1);
  dis2 = new DataInputStream(bis2);
  dis3 = new DataInputStream(bis3);
  dis4 = new DataInputStream(bis4);

  while ( (dis4.available() != 0) ) {
        String combo1 = dis1.readLine();
        String combo2 = dis2.readLine();
        String combo3 = dis3.readLine();
        String combo4 = dis4.readLine();

        RunnableDemo R1 = new RunnableDemo(combo1);
        RunnableDemo R2 = new RunnableDemo(combo2);
        RunnableDemo R3 = new RunnableDemo(combo3);
        RunnableDemo R4 = new RunnableDemo(combo4);

        R1.start();
        R2.start();
        R3.start();
        R4.start();
  }

  fis1.close();
  fis2.close();
  fis3.close();
  fis4.close();

  bis1.close();
  bis2.close();
  bis3.close();
  bis4.close();

  dis1.close();
  dis2.close(); 
  dis3.close(); 
  dis4.close(); 

   }   
}
