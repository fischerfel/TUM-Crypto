String passwordSHA="5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";
String complexSHA="8849fb9b221ddec0117e2796d16929179accf3a6012f738e1ed6c11af9cc2081";
@Test
public void testDigest() throws InterruptedException{
    System.out.println("Starting Digest test");
    String complexPassword = "a7$h1UC8";
    try {
        Assert.assertTrue(authenticateUser(complexPassword, complexSHA));
        Assert.assertTrue(authenticateUser("password", passwordSHA));           
        Assert.assertTrue( hashWord(complexPassword).equals(complexSHA) );
    } catch (Exception e) {
        Assert.fail();
    }
}
public boolean authenticateUser(String word, String stored) throws Exception {
    String apache2Pswd = hashApache(word);
    System.out.println(apache2Pswd);                
    return stored.equals(apache2Pswd);
}
private String hashApache(String pswd){
    return DigestUtils.sha256Hex(pswd);     
}
public static String hashWord(String word) throws Exception{
    byte[] digest = MessageDigest.getInstance("SHA-256").digest(word.getBytes("UTF-8"));
    StringBuilder sb = new StringBuilder();
    for (byte b : digest) {
        sb.append(String.format("%02x", b));
    }
    System.out.println(sb.toString());
    return sb.toString();
}
