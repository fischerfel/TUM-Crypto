public class Decrpytion {

public static void  Decrypt(String FileInName, String FileOutName ) throws  IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
{

    FileOutName = FileOutName+"/D.txt";
    String password = JOptionPane.showInputDialog("Enter The Password"); //Un comment this line if u are usng either 1st or 2nd method of password for encryption

    FileInputStream fileIn = new FileInputStream(FileInName);
    FileOutputStream fileOut = new FileOutputStream(FileOutName);


    PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
    SecretKey passwordKey = keyFactory.generateSecret(keySpec);

    byte[] salt = new byte[8];
    fileIn.read(salt);
    int iterations = 100;

    PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterations);

    Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
    cipher.init(Cipher.DECRYPT_MODE, passwordKey, parameterSpec);

    byte[] input = new byte[128];

    int bytesRead;

        while((bytesRead = fileIn.read(input)) != -1)
        {

            byte[] output = cipher.update(input, 0, bytesRead);

                if (output != null)
                    fileOut.write(output);
        }

    byte[] output = cipher.doFinal();

        if (output != null)
            fileOut.write(output);

    fileIn.close();
    fileOut.flush();
    fileOut.close();


}
