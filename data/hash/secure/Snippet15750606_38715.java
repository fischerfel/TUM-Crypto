private String hash;
private String salt;

public HASHME(String plaintext) 
{
    try {
    System.setProperty("file.encoding", "UTF-8");
    salt = "salt";
    plaintext = plaintext + salt;
    byte[] bytesOfPlain = plaintext.getBytes("UTF8");

    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] hashedBytes = md.digest(bytesOfPlain);
    hash = new String(hashedBytes, "UTF8");
    System.out.println("hash: " + hash);

    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}
