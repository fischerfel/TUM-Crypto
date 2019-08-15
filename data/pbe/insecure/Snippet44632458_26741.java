public class AESFileEncryption {
public static void encrypt(String path,String pwd) throws Exception {

    FileOutputStream outFile;

    try ( 
            FileInputStream inFile = new FileInputStream(path)) {

        String fileName=path;

        System.out.println(path);

        outFile = new FileOutputStream(fileName+".aes");
        // password to encrypt the file
        String password = pwd;
        byte[] salt = {
        (byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
        (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
       };

        SecretKeyFactory factory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(),salt,65536,128);// user-chosen password that can be used with password-based encryption (PBE).
        SecretKey secretKey = factory.generateSecret(keySpec);
        SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");//Secret KeySpec is a class and implements inteface SecretKey

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[16];
        random.nextBytes(bytes);
     IvParameterSpec ivspec = new IvParameterSpec(bytes);   
        cipher.init(Cipher.ENCRYPT_MODE, secret,ivspec);//opmode,key
        outFile.write(bytes);
        byte[] input = new byte[64];
        int bytesRead;
        while ((bytesRead = inFile.read(input)) != -1) {
            byte[] output = cipher.update(input, 0, bytesRead);
            if (output != null)
                Files.write(Paths.get(fileName+".aes"), output, StandardOpenOption.APPEND);

        }   byte[] output = cipher.doFinal();
        if (output != null)
            Files.write(Paths.get(fileName+".aes"), output, StandardOpenOption.APPEND);
    }
    outFile.flush();
    outFile.close();
    File f=new File(path);
    boolean x=f.delete();
    if(x){
        System.out.println("File deleted");
    }
    JOptionPane.showMessageDialog(null,"File Encrypted.");

}
}
