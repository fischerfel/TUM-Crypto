public class EncryptDecrypt 

{
public static String Password;
public static FileInputStream fileIn;
public static FileOutputStream fileOut;
public static boolean a = true;

public static void  Encrypt(String FileInName, String FileOutName ) throws    IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
{

    //There are three methods via which u can give a password. First direct input, second random text;
    //third given by user during execution of the program.
    //Password = "ThisShoouldWork"; // to Use the first method, un-comment this line and comment the rest of the methods.

    Password = JOptionPane.showInputDialog("Set The Password"); //To use second method , un-comment this line and comment the rest of the methods.

    /*while(a)                      //to use third method, un-comment this "block" and comment the rest of the methods.
    {

    String setPassword = JOptionPane.showInputDialog(null, "Enter Your Password");
    String confirmPassword = JOptionPane.showInputDialog(null, "Re-Enter Your Password");

    if(setPassword.equals(confirmPassword))
    {

        Password = confirmPassword;
        a = false;
    }


    else
        JOptionPane.showMessageDialog(null, "Passwords Dont Match !!");

    }*/


    fileIn = new FileInputStream(FileInName);
    fileOut = new FileOutputStream(FileOutName);

    PBEKeySpec keySpec = new PBEKeySpec(Password.toCharArray());
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
    SecretKey passwordKey = keyFactory.generateSecret(keySpec);

    byte[] salt = new byte[8];
    Random rnd = new Random();
    rnd.nextBytes(salt);
    int iterations = 100;

    PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterations);

    Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
    cipher.init(Cipher.ENCRYPT_MODE, passwordKey, parameterSpec);
    fileOut.write(salt);

    byte[] input = new byte[64];
    int bytesRead;

        while ((bytesRead = fileIn.read(input)) != -1)
        {

             byte[] output = cipher.update(input, 0, bytesRead);
             if (output != null) fileOut.write(output);
        }

    byte[] output = cipher.doFinal();

        if (output != null) fileOut.write(output);

    fileIn.close();
    fileOut.flush();
    fileOut.close();

}
