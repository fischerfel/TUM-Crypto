public class Encrypter {

    Cipher ecipher;
    Cipher dcipher;

    SecretKeySpec key = new SecretKeySpec("missyou1".getBytes(), "DES");

    public DesEncrypter() throws Exception {
        ecipher = Cipher.getInstance("DES");
        dcipher = Cipher.getInstance("DES");
        ecipher.init(Cipher.ENCRYPT_MODE, key);
        dcipher.init(Cipher.DECRYPT_MODE, key);
    }

    public String encrypt(String str) throws Exception {
        byte[] utf8 = str.getBytes("UTF8");
        byte[] enc = ecipher.doFinal(utf8);
        return new sun.misc.BASE64Encoder().encode(enc);
    }

    public String decrypt(String str) throws Exception {
        byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

        byte[] asd = new byte[(dec.length/8+1)*8];
        for(int i = 0; i < dec.length; i++){
            asd[i] = dec[i];
        }
        byte[] utf8 = dcipher.doFinal(asd);

        return new String(utf8, "UTF8");
    }
}
