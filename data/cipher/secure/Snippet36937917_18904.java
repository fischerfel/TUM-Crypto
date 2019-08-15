  package <your package name>;

    import android.util.Base64;
    import java.security.NoSuchAlgorithmException;
    import javax.crypto.Cipher;
    import javax.crypto.NoSuchPaddingException;
    import javax.crypto.spec.IvParameterSpec;
    import javax.crypto.spec.SecretKeySpec;

    public class ApiCrypter3 {

    private byte[] sessionKey = {your 16 character key}; //Where you get this from is beyond the scope of this post
    private byte[] iv = {your 16 character value}; //Ditto
    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private Cipher cipher;

    public ApiCrypter3()
    {
        ivspec = new IvParameterSpec(iv);
        keyspec = new SecretKeySpec(sessionKey, "AES");

        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public byte[] encrypt(String text) throws Exception
    {
        if(text == null || text.length() == 0) {
            throw new Exception("Empty string");
        }
        byte[] encrypted = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            encrypted =  Base64.encode(cipher.doFinal(text.getBytes("UTF-8")), Base64.DEFAULT);
        }
        catch (Exception e) {
            throw new Exception("[encrypt] " + e.getMessage());
        }
        return encrypted;
    }

    public byte[] decrypt(String code) throws Exception
    {
        if(code == null || code.length() == 0) {
            throw new Exception("Empty string");
        }
        byte[] decrypted = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            //decrypted = Base64.decode(cipher.doFinal(code.getBytes()),Base64.DEFAULT);
            decrypted = Base64.decode(cipher.doFinal(hexToBytes(code)),Base64.DEFAULT);
        }
        catch (Exception e) {
            throw new Exception("[decrypt] " + e.getMessage());
        }
        return decrypted;
    }

    public static String bytesToHex(byte[] data) {
        if (data==null) {
            return null;
        }
        int len = data.length;
        String str = "";
        for (int i=0; i<len; i++) {
            if ((data[i]&0xFF)<16) {
                str = str + "0" + Integer.toHexString(data[i]&0xFF);
            }
            else {
                str = str + Integer.toHexString(data[i]&0xFF);
            }
        }
        return str;
    }

    public static byte[] hexToBytes(String str) {
        if (str==null) {
            return null;
        }
        else if (str.length() < 2) {
            return null;
        }
        else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i=0; i<len; i++) {
                //No effect
                //buffer[i] = (byte) Integer.parseInt(str.substring(i*2,i*2+2),16);
                buffer[i]=Integer.valueOf(str.substring(i*2,i*2+2),16).byteValue();
                }
            return buffer;
        }
    }
}
