import javax.crypto.spec.*;
import javax.crypto.*;
import javax.swing.*;
public class des 
{
    public static void main(String ar[]) throws Exception
    {
        KeyGenerator keygen=KeyGenerator.getInstance("DES");
        SecretKey secretkey=keygen.generateKey();
        Cipher cip=Cipher.getInstance("DES");

        String inputText=JOptionPane.showInputDialog("Give input:");

        byte[] iv=cip.getIV();
        IvParameterSpec ps=new IvParameterSpec(iv);

        cip.init(Cipher.ENCRYPT_MODE,secretkey);
        byte[] encrypted=cip.doFinal(inputText.getBytes());

        cip.init(Cipher.DECRYPT_MODE,secretkey,ps);
        byte[] decrypted=cip.doFinal(encrypted);

        JOptionPane.showMessageDialog(null,"Encrypted :"+new String(encrypted)+"\n Decrypted :"+new String(decrypted));
        System.exit(0);
    }
}
