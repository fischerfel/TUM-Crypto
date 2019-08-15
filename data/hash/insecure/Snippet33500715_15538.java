public class MD5CheckSum {

public byte [] createChecksum (String filename) throws Exception {
    InputStream fis = new FileInputStream(filename);

    byte[] buffer = new byte[1024];
    MessageDigest complete = MessageDigest.getInstance("MD5");
    int numRead;

    do {
        numRead = fis.read(buffer);
        if (numRead > 0){
            complete.update(buffer,0,numRead);
        }
    }while (numRead !=1);

    fis.close();
    return complete.digest();

}
public String getMD5Checksum(String filename) throws Exception {
    /*byte[] b = createChecksum(filename);
    String result = "";

    for (int i=0; i < b.length; i++){
        result += Integer.toString(( b[i] & 0xff) + 0x100, 16).substring( 1 );
    }
    return result;*/
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] messageDigest = md.digest(filename.getBytes());
    BigInteger number = new BigInteger(1, messageDigest);
    String hashtext = number.toString(16);
    // Now we need to zero pad it if you actually want the full 32 chars.
    while (hashtext.length() < 32) {
        hashtext = "0" + hashtext;
    }
    return hashtext;
}

public MD5CheckSum() throws Exception{

    String path = "C:/Users/user/Downloads/Documents/ECOMM SUMMER BLOSSOM.docx";

    System.out.println("MD5 Hash Succeed");

    System.out.println(getMD5Checksum(path));

}
