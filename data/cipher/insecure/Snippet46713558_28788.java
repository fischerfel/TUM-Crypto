public class des {

    public static void main(String[] args) throws Exception {
        KeyGenerator keygen=KeyGenerator.getInstance("DES");
        SecretKey secretkey=keygen.generateKey();
        Cipher encrypter=Cipher.getInstance("DES");
        Cipher decrypter=Cipher.getInstance("DES");

        String inputText=JOptionPane.showInputDialog("Give input:");

        encrypter.init(Cipher.ENCRYPT_MODE,secretkey);
        byte[] encrypted=encrypter.doFinal(inputText.getBytes());

        decrypter.init(Cipher.DECRYPT_MODE,secretkey);
        byte[] decrypted=decrypter.doFinal(encrypted);

        JOptionPane.showMessageDialog(null,"Encrypted :"+new String(encrypted)+"\n Decrypted :"+new String(decrypted));
        System.exit(0);

    }

}
