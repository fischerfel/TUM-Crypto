 import java.io.UnsupportedEncodingException;
    import java.security.InvalidAlgorithmParameterException;
    import java.security.InvalidKeyException;
    import java.security.Key;
    import java.security.NoSuchAlgorithmException;
    import java.security.Security;
    import java.security.spec.AlgorithmParameterSpec;

    import org.apache.commons.codec.binary.BaseNCodec;
    import javax.crypto.BadPaddingException;
    import javax.crypto.Cipher;
    import javax.crypto.IllegalBlockSizeException;
    import javax.crypto.KeyGenerator;
    import javax.crypto.NoSuchPaddingException;
    import javax.crypto.SecretKey;
    import javax.crypto.spec.IvParameterSpec;
    import javax.crypto.spec.SecretKeySpec;

        public static void main(String[] args) {
            // TODO Auto-generated method stub
            String encrypted = "1e9z0zjI9MGBLW6AdLE+BiVnv9R9lDAS+SmSkX/Ufx7zTHOVanpiwd4IC2XejJaq";
             SecretKey key = new SecretKeySpec("f7710e2bcf419dab".getBytes("UTf-8"), "AES");
                AlgorithmParameterSpec iv = new IvParameterSpec("42d39acea111ceb2".getBytes("UTf-8"));
                byte[] decodeBase64 = Base64.decodeBase64(encrypted);

                Cipher cipher = null;
                try {
                    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    cipher.init(Cipher.DECRYPT_MODE, key, iv);
                } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

              try {
                 String s = new String(cipher.doFinal(decodeBase64).toString());
                System.out.println(s);
            } catch (IllegalBlockSizeException | BadPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
