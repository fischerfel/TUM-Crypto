public class SqlCipherUtil {

    private Cipher ecipher;
    private Cipher dcipher;

    public String encryptString(String pStrPlainText) {

        try {
            generateKey();
            byte[] utf8 = pStrPlainText.getBytes("UTF8");
            byte[] enc = this.ecipher.doFinal(utf8);
            return new BASE64Encoder().encode(enc);

        } catch (Exception e) {
            e.printStackTrace();
        } 

        return null;
    }

    public String decryptString(String pStrCipherText){

        try {
            generateKey();
            byte[] dec = new BASE64Decoder().decodeBuffer(pStrCipherText);
            byte[] utf8 = this.dcipher.doFinal(dec);
            return new String(utf8, "UTF8");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * This method is used to generate the encrypted key.
     */
    private void generateKey() {

        try {
            byte[] decodedStr = new BASE64Decoder().decodeBuffer("rA/LUdBA/hA=");
            SecretKey key = new SecretKeySpec(decodedStr, "DES");
            this.ecipher = Cipher.getInstance("DES");
            this.dcipher = Cipher.getInstance("DES");
            this.ecipher.init(1, key);
            this.dcipher.init(2, key);

        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
}
