public class AESFileDecryption {
public static void decrypt(String path,String pwd) throws Exception {

    String password = pwd;
    String fileName=path;
    File file=new File(path);
        //System.out.println(inFile.toString());
        String fileNameWithOutExt = path.replaceFirst("[.][^.]+$", "");
        System.out.println(fileName);
        System.out.println(fileNameWithOutExt);
        byte[] salt = {
        (byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
        (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
    };
    System.out.println("1");
    FileInputStream fis = new FileInputStream(path);
    SecretKeyFactory factory = SecretKeyFactory
            .getInstance("PBKDF2WithHmacSHA1");
    KeySpec keySpec = new PBEKeySpec(password.toCharArray(),salt,65536,128);
    SecretKey tmp = factory.generateSecret(keySpec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
    System.out.println("2");
    // file decryption
    Cipher cipher=null;
    byte bytes[]=new byte[16];
    fis.read(bytes, 0, 16); 
    IvParameterSpec ivspec = new IvParameterSpec(bytes);

    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, secret, ivspec);
    System.out.println("3");
    FileOutputStream fos = new FileOutputStream(fileNameWithOutExt);
    System.out.println("4");
    byte[] in   = new byte[64];
    int read;
    while ((read = fis.read(in,16,(int)file.length()-16)) != -1) {
        byte[] output = cipher.update(in, 0, read);
        if (output != null)
            fos.write(output);
    }
    try{
    byte[] output = cipher.doFinal();
    if (output != null)
        fos.write(output);
    fis.close();
    fos.flush();
    fos.close();
    System.out.println("File Decrypted.");
}
catch(IOException | BadPaddingException | IllegalBlockSizeException e)
{
    System.out.println(e+"");
}
}
}
