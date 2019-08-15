public SecretKey generateKey(String Ags) throws Exception {
    // make password
    PBEKeySpec keySpec = new PBEKeySpec(this.password.toCharArray(),this.salt,20,56);

    SecretKeyFactory keyFactory = SecretKeyFactory
            .getInstance("PBE");
    SecretKey key = keyFactory.generateSecret(keySpec);
    System.out.println();

    /*
    KeyGenerator kg = KeyGenerator.getInstance("AES");
    kg.init(k);
    //
    SecretKey FINAL_key = new SecretKeySpec(key.getEncoded(), "AES");
    */
    return null;
}
