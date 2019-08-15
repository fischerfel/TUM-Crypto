package chert.chert;

import chert.chert.R;
import chert.chert.SimpleCrypto;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ChertActivity extends Activity {
    /** Called when the activity is first created. */
    TextView textView1;



    byte[] key=new byte[16];
    byte[] state=new byte[16];
    public SimpleCrypto Crypt = new SimpleCrypto(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        int i;
        String ss,state_str,key_str;
        char c;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);




        //======================================================================================================================        

        state[0]=0x32;           state[1]=(byte) 0x88;  state[2]=0x31;          state[3]=(byte) 0xe0;
        state[4]=0x43;           state[5]=0x5a;         state[6]=0x31;          state[7]=0x37;
        state[8]=(byte) 0xf6;    state[9]=0x30;         state[10]=(byte) 0x98;  state[11]=0x07;
        state[12]=(byte) 0xa8;   state[13]=(byte) 0x8d; state[14]=(byte) 0xa2;  state[15]=0x34;


     //======================================================================================================================        

        key[0]=0x2b;           key[1]=0x28;                key[2]=(byte) 0xab;   key[3]=0x09;
        key[4]=0x7e;           key[5]=(byte) 0xae;         key[6]=(byte) 0xf7;   key[7]=(byte) 0xcf;
        key[8]=0x15;           key[9]=(byte) 0xd2;         key[10]=0x15;         key[11]=0x4f;
        key[12]=0x16;          key[13]=(byte) 0xa6;        key[14]=(byte) 0x88;  key[15]=0x3c;

      //======================================================================================================================        

        try {
            byte[] rawKey = Crypt.getRawKey(key);
            byte[] result = Crypt.encrypt(rawKey, state);
            } catch (Exception e) {
                e.printStackTrace();
            }

    }
}







package chert.chert;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SimpleCrypto {

    public SimpleCrypto(ChertActivity chertActivity) {
        // TODO Auto-generated constructor stub
    }

    public static String encrypt(String seed, String cleartext) throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] result = encrypt(rawKey, cleartext.getBytes());
        return toHex(result);
    }

    public static String decrypt(String seed, String encrypted) throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] enc = toByte(encrypted);
        byte[] result = decrypt(rawKey, enc);
        return new String(result);
    }

    public static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
        kgen.init(128, sr); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }


    public static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }
    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length()/2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2*buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }
    private final static String HEX = "0123456789ABCDEF";
    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
    }

}
