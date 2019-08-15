public class Filedecrypt {

public static void main(String args[]) throws IOException,
        InvalidKeyException, java.security.InvalidKeyException,
        NoSuchAlgorithmException, NoSuchPaddingException,
        BadPaddingException {
    try {

        byte[] plainData = null;
        byte[] encryptedData;
        File f1 = new File("C:\\Output\\Privatekey.txt");
        FileInputStream in1 = new FileInputStream(f1);
        byte[] bytekey = new byte[(int) f1.length()];
        in1.read(bytekey);
        KeyFactory keyFac = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytekey);

        PrivateKey key = keyFac.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);

        File f = new File("C:\\Output\\encrypted.txt");
        FileInputStream in = new FileInputStream(f);
        encryptedData = new byte[(int) f.length()];
        in.read(encryptedData);

        try {
            plainData = cipher.doFinal(encryptedData);
        } catch (IllegalBlockSizeException e) {

            e.printStackTrace();
        } catch (BadPaddingException e) {

            e.printStackTrace();
        }

        FileOutputStream target = new FileOutputStream(
                new File(
                        "C:\\Output\\text1.txt"));
        target.write(plainData);
        target.close();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (InvalidKeyException ei) {
        ei.printStackTrace();
    } catch (InvalidKeySpecException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}
}
