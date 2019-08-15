    private byte[] getHash(int iterationNb, String password, byte[] salt, int hashType) throws NoSuchAlgorithmException {
       MessageDigest digest = MessageDigest.getInstance(HASH_TYPE[hashType]);
       digest.reset();
       digest.update(salt);
       byte[] input = null;
        try {
            input = digest.digest(password.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(),e);
        }
       for (int i = 0; i < iterationNb; i++) {
           digest.reset();
           input = digest.digest(input);
       }
       return input;
   }
