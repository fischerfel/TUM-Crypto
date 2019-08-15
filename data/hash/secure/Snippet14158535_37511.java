public byte[] getHash(String password, byte[] salt, String algorithm) throws NoSuchAlgorithmException, UnsupportedEncodingException {
       MessageDigest digest = MessageDigest.getInstance(algorithm);//The String rapresents the alg you want to use: for example "SHA-1" or "SHA-256"
       digest.reset();
       digest.update(salt);
       return digest.digest(password.getBytes("UTF-8"));
 }
