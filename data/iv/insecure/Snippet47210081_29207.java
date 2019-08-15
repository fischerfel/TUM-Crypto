public class Encryptor{
private static String key="asdfghjklqwe@321" ;
private static String initVector = "RandomInitVector";

public static void encryptFile(File inputFile, String outputFolderPath) {
    try {
        Key secretKey = new SecretKeySpec(key.getBytes("UTF-8"),"AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey,new IvParameterSpec(initVector.getBytes("UTF-8")));

        FileInputStream inputStream = new FileInputStream(inputFile);
        byte[] inputBytes = new byte[(int) inputFile.length()];
        inputStream.read(inputBytes);

        byte[] outputBytes = cipher.doFinal(inputBytes);

        File encryptedFile = new File(outputFolderPath,inputFile.getName());
        encryptedFile.getParentFile().mkdirs();
        encryptedFile.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(encryptedFile);
        outputStream.write(outputBytes);

        inputStream.close();
        outputStream.close();
        System.out.println("File encryption completed successfully.");
        inputFile.delete();
        System.out.println("File deleted from the original location.");
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
