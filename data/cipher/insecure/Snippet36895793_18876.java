public class EncryptDecryptStringWithDES {

    public static Cipher ecipher;
    public static Cipher dcipher;

    public static SecretKey key;


    public static String getEncryptedText(String sty) throws Exception {

        // generate secret key using DES algorithm
        key = KeyGenerator.getInstance("DES").generateKey();
        ecipher = Cipher.getInstance("DES");


        // initialize the ciphers with the given key

        ecipher.init(Cipher.ENCRYPT_MODE, key);




        sty = encrypt(sty);

        return sty;
    }

    public static String getDecryptedText(String sty) throws Exception {
   key = KeyGenerator.getInstance("DES").generateKey();

        dcipher = Cipher.getInstance("DES");
        dcipher.init(Cipher.DECRYPT_MODE, key);
        sty = decrypt(sty);

        return sty;

    }


    public static String encrypt(String str) {

        try {

            // encode the string into a sequence of bytes using the named charset

            // storing the result into a new byte array.

            byte[] utf8 = str.getBytes("UTF8");

            byte[] enc = ecipher.doFinal(utf8);

// encode to base64

            enc = BASE64EncoderStream.encode(enc);

            return new String(enc);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

    public static String decrypt(String str) {

        try {

            // decode with base64 to get bytes

            byte[] dec = BASE64DecoderStream.decode(str.getBytes());

            byte[] utf8 = dcipher.doFinal(dec);

// create new string based on the specified charset

            return new String(utf8, "UTF8");

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }
