public class AES_Encryption  {

public static void main(String[] args) throws Exception {
    String str = new Scanner(new File("src//plainText.txt")).useDelimiter("\\Z").next();
    FileWriter fstream = new FileWriter("src//cipherText.txt");
    BufferedWriter out = new BufferedWriter(fstream);
    FileWriter fstream2 = new FileWriter("src//decrpytedText.txt");
    BufferedWriter out2 = new BufferedWriter(fstream2);
    System.out.println("" + str);


    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    keyGen.init(128);
    Key key = keyGen.generateKey();
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] cipherText = cipher.doFinal(str.getBytes());
    String ct = new String(cipherText);
    System.out.println( new String(cipherText, "UTF8") );
    out.append(ct);
    out.close();


    String cipherT = new Scanner(new File("src//cipherText.txt")).useDelimiter("\\Z").next();
    cipher.init(Cipher.DECRYPT_MODE, key);

    //byte[] decVal = Base64.decode(cipherT.getBytes());
    byte[] newPlainText = cipher.doFinal(cipherT.getBytes());
    String dt = new String(newPlainText, "UTF8");
    out2.append(dt);
    out2.close();
}
