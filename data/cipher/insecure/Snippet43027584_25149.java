public class Symmetric1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        // TODO code application logic here
        KeyGenerator kg = KeyGenerator.getInstance("DES");
    kg.init(new SecureRandom());
    SecretKey secretKey = kg.generateKey();

    FileInputStream inFile = new FileInputStream("C:/Users/Administrator/Desktop/original.bmp");

    Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding"); 
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);

    FileOutputStream outFile = new FileOutputStream("C:/Users/Administrator/Desktop/ECB_original.bmp");

    byte[] input = new byte[64];
    int bytesRead;
    while((bytesRead = inFile.read(input)) != -1){
        byte[] output = cipher.update(input,0,bytesRead);
        if(output != null)
            outFile.write(output);
    }

    byte[] output = cipher.doFinal();
    if(output != null)
        outFile.write(output);

    inFile.close();
    outFile.flush();
    outFile.close();
    }
}
