public class AES_Encryption  {
public static void main(String[] args) throws Exception {
    String str = new Scanner(new File("plainText.txt")).useDelimiter("\\t").next();
    FileOutputStream fstream = new FileOutputStream("cipherText.txt");
    BufferedOutputStream out = new BufferedOutputStream(fstream);
    FileOutputStream fstream2 = new FileOutputStream("decrpytedText.txt");
    BufferedOutputStream out2 = new BufferedOutputStream(fstream2);
    System.out.println("INPUT String:\n" + str);


    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    keyGen.init(128);
    Key key = keyGen.generateKey();
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] cipherText = cipher.doFinal(str.getBytes());
    System.out.println("ENCRYPTED String:\n"+new String(cipherText, "UTF8") );
    out.write(cipherText);
    out.flush();
    out.close();


    //String cipherT = new Scanner(new File("cipherText.txt")).nextLine();
    BufferedInputStream bfin = new BufferedInputStream(new FileInputStream(new File("cipherText.txt")));//To read the file in Binary Mode.
    cipher.init(Cipher.DECRYPT_MODE, key);
    int BUFFERSIZE = 1024;
    byte[] readBytes = new byte[BUFFERSIZE];
    byte[] data = null;
    int totalRead = -1;
    while( (totalRead = bfin.read(readBytes))!=-1)
    {
        byte[] temp = new byte[(data == null ? totalRead : data.length)];
        System.arraycopy((data==null ? readBytes : data),0,temp,0, temp.length); 
        data = new byte[(data == null ? 0 : data.length) + totalRead];
        System.arraycopy(temp, 0, data, 0, temp.length);
        System.arraycopy(readBytes, 0, data, data.length - temp.length, totalRead);
    }
    if (data!=null)
    {
        byte[] newPlainText = cipher.doFinal(data);
        out2.write(newPlainText);
        out2.flush();
        System.out.println("DECRYPTED String:\n"+new String(newPlainText,"UTF8"));
    }
    else
    {
        System.out.println("No Data Found");
    }
    //String dt = new String(newPlainText, "UTF8");
    out2.close();
}
}
