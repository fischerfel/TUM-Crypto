public int getUniqueInteger(String name){
    String plaintext = name;
    int hash = name.hashCode();
    MessageDigest m;
    try {
        m = MessageDigest.getInstance("MD5");
        m.reset();
        m.update(plaintext.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1,digest);
        String hashtext = bigInt.toString(10);
        // Now we need to zero pad it if you actually want the full 32 chars.
        while(hashtext.length() < 32 ){
          hashtext = "0"+hashtext;
        }
        int temp = 0;
        for(int i =0; i<hashtext.length();i++){
            char c = hashtext.charAt(i);
            temp+=(int)c;
        }
        return hash+temp;
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return hash;
}
