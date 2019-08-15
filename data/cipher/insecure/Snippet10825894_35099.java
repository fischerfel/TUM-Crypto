import java.io.UnsupportedEncodingException;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

/**
 *
 * @author aealvarenga
 */
public class TripleDesCipherFromDES {

    public byte[] desEncryptionECBCipher(String key, String text) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        Security.addProvider(new BouncyCastleProvider());        
        SecretKey keySpec = new SecretKeySpec(this.hexStringToByteArray(key), "DES");
        final Cipher encrypter = Cipher.getInstance("DES/ECB/ZeroBytePadding", "BC");
        encrypter.init(Cipher.ENCRYPT_MODE, keySpec);
        final byte[] plainTextBytes = text.getBytes("utf-8");
        final byte[] cipherText = encrypter.doFinal(plainTextBytes);
        return cipherText;
    }

    public String desDecriptionECBCipher(String key, byte[] cipherText) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, UnsupportedEncodingException, BadPaddingException {
        Security.addProvider(new BouncyCastleProvider());
        SecretKey keySpec = new SecretKeySpec(this.hexStringToByteArray(key), "DES");
        final Cipher decrypter = Cipher.getInstance("DES/ECB/ZeroBytePadding", "BC");        
        decrypter.init(Cipher.DECRYPT_MODE, keySpec);
        final byte[] plainText = decrypter.doFinal(cipherText);
        return new String(plainText, "UTF-8");
    }

    public byte[] desEncryptionCBCCipher(String key, String text) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Security.addProvider(new BouncyCastleProvider());
        byte[] iv = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        IvParameterSpec ivSpec = new IvParameterSpec(iv);


        SecretKey keySpec = new SecretKeySpec(this.hexStringToByteArray(key), "DES");
        final Cipher encrypter = Cipher.getInstance("DES/CBC/ZeroBytePadding", "BC");
        encrypter.init(Cipher.ENCRYPT_MODE, keySpec,ivSpec);
        final byte[] plainTextBytes = text.getBytes("utf-8");
        final byte[] cipherText = encrypter.doFinal(plainTextBytes);
        return cipherText;
    }    

    public String desDecriptionCBCCipher(String key, byte[] cipherText) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, UnsupportedEncodingException, BadPaddingException, InvalidAlgorithmParameterException {
        Security.addProvider(new BouncyCastleProvider());
        byte[] iv = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        IvParameterSpec ivSpec = new IvParameterSpec(iv);        
        SecretKey keySpec = new SecretKeySpec(this.hexStringToByteArray(key), "DES");
        final Cipher decrypter = Cipher.getInstance("DES/CBC/ZeroBytePadding", "BC");        
        decrypter.init(Cipher.DECRYPT_MODE, keySpec,ivSpec);
        final byte[] plainText = decrypter.doFinal(cipherText);
        return new String(plainText, "UTF-8");
    }

    public String asciiToHex(String ascii) {
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < ascii.length(); i++) {
            hex.append(Integer.toHexString(ascii.charAt(i)));
        }
        return hex.toString();
    }

    public byte[] hexStringToByteArray(String hexstring) {
        int i = 0;
        if (hexstring == null || hexstring.length() <= 0) {
            return null;
        }
        String stringvector = "0123456789ABCDEF";
        byte[] bytevector = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F};
        byte[] out = new byte[hexstring.length() / 2];
        while (i < hexstring.length() - 1) {
            byte ch = 0x00;
            //Convert high nibble charater to a hex byte
            ch = (byte) (ch | bytevector[stringvector.indexOf(hexstring.charAt(i))]);
            ch = (byte) (ch << 4); //move this to the high bit

            //Convert the low nibble to a hexbyte
            ch = (byte) (ch | bytevector[stringvector.indexOf(hexstring.charAt(i + 1))]); //next hex value
            out[i / 2] = ch;
            i++;
            i++;
        }
        return out;
    }    

    public String tdesedeECBCipher(String text, String doubleLenghtKey) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        //key definition
        String key1 = doubleLenghtKey.substring(0, 16);
        String key2 = doubleLenghtKey.substring(16, 32);
        String key3 = key1;

        byte[] codedText = new TripleDesCipherFromDES().desEncryptionECBCipher(key1, text);
        String decodedText = new TripleDesCipherFromDES().desDecriptionECBCipher(key2, codedText);
        byte[] codedTextFinal = new TripleDesCipherFromDES().desEncryptionECBCipher(key3, decodedText);

      return new String(Hex.encode(codedTextFinal));
    }

    public String tdesedeCBCCipher(String text, String doubleLenghtKey) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        //key definition
        String key1 = doubleLenghtKey.substring(0, 16);
        String key2 = doubleLenghtKey.substring(16, 32);
        String key3 = key1;

        byte[] codedText = new TripleDesCipherFromDES().desEncryptionCBCCipher(key1, text);
        String decodedText = new TripleDesCipherFromDES().desDecriptionCBCCipher(key2, codedText);
        byte[] codedTextFinal = new TripleDesCipherFromDES().desEncryptionCBCCipher(key3, decodedText);

       return new String(Hex.encode(codedTextFinal));
    }    

    public static void main(String[] args) throws Exception {
        String text = "FFFF9876543210E0";        
        String key =  "0123456789ABCDEFFEDCBA9876543210";

        System.out.println(new TripleDesCipherFromDES().tdesedeECBCipher(text,key));
        System.out.println(new TripleDesCipherFromDES().tdesedeCBCCipher(text,key));
    }
}
