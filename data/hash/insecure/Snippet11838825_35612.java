public void sha1() throws NoSuchAlgorithmException {

    String hexMEID = "A0000000002329";

    MessageDigest mDigest = MessageDigest.getInstance("SHA1");      
    byte[] b = new BigInteger(hexMEID,16).toByteArray();    

    byte[] result = mDigest.digest(b);
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < result.length; i++) {
        sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
    }

    System.out.println(sb.toString());
}
