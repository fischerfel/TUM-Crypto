public class Symmetric2 {

    public static void main(String[] args) throws Exception{
        // TODO Auto-generated method stub

        FileInputStream inFile = new FileInputStream("C:/Users/Administrator/Desktop/original.bmp");
        FileOutputStream outFile = new FileOutputStream("C:/Users/Administrator/Desktop/CBC_original.bmp");

        KeyGenerator kg = KeyGenerator.getInstance("DES");
        kg.init(new SecureRandom());

        SecretKey sk = kg.generateKey();

        Cipher cp = Cipher.getInstance("DES/CBC/PKCS5Padding");

        byte[] ivBytes = new byte[]{
                0x00, 0x01,0x02, 0x03, 0x00, 0x00, 0x00, 0x01       };
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        cp.init(Cipher.ENCRYPT_MODE, sk, ivSpec); //CBC방식이므로 인자가 3개이다.

        byte[] input = new byte[64]; //getBytes() 
        int bytesRead;
        while((bytesRead = inFile.read(input)) != -1){
            byte[] output = cp.update(input,0,bytesRead);
            if(output != null)
                outFile.write(output);
        }
        byte[] output = cp.doFinal();
        if(output != null)
            outFile.write(output);

        inFile.close();
        outFile.flush();
        outFile.close();
    }

}
