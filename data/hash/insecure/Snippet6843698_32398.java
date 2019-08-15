public void testHash() {

    String password = "Test";

    byte[] key = password.getBytes();

    MessageDigest md = MessageDigest.getInstance("SHA-1");

    byte[] hash = md.digest(key);

    String result = "";
    for ( byte b : hash ) {
        result += Integer.toHexString(b + 256) + " ";
    }

    System.out.println(result);

}
