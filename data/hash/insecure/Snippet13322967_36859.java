public String MD5ToString(String plain) {
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.reset();
        md.update(plain.getBytes());
        byte[] digest = md.digest();
        BigInteger bigInt = new BigInteger(1,digest);
        String hashtext = bigInt.toString(16);
        // Now we need to zero pad it if you actually want the full 32 chars.
        while(hashtext.length() < 32 ){
            hashtext = "0"+hashtext;
        }
        return plain;
    } catch (Exception e) {
        System.out.println("Cannot encrypt String to Hash");
        e.printStackTrace();
    }
    return null;
}
