public String encrypt(String DATA,String key_string) throws Exception {
    String separator = "//msit//";
    byte[] data = key_string.getBytes();
    MessageDigest sha = MessageDigest.getInstance("SHA-1");
    data = sha.digest(data);
    data = Arrays.copyOf(data, 16); // use only first 128 bit
    SecretKey key = new SecretKeySpec(data, "AES");
    String final_matter = DATA + separator;
    System.out.println(final_matter);
    ecipher = Cipher.getInstance("AES");
    ecipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] utf8 = final_matter.getBytes("UTF8");
    byte[] enc = ecipher.doFinal(utf8);
    return new sun.misc.BASE64Encoder().encode(enc);
    }
