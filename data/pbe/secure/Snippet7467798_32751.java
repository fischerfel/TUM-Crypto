    SecretKeyFactory f;
    try {
        f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    } catch (NoSuchAlgorithmException e) {
        throw new Exception("Key derivation algorithm not available.", e);
    }
    KeySpec ks = new PBEKeySpec(password.toCharArray());
    SecretKey s;
    try {
        s = f.generateSecret(ks);
    } catch (InvalidKeySpecException e) {
        throw new Exception("Key generation failed.", e);
    }
    Key k = new SecretKeySpec(s.getEncoded(),"AES");
