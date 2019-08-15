    // got modulus and exp for public key from c# 
    byte[] modulo = {.....};         
    byte[] exp = {1,0,1};                
    BigInteger modulus = new BigInteger(1, modulo);     
    BigInteger pubExp = new BigInteger(1, exp);     
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    RSAPublicKeySpec priKeySpec = new RSAPublicKeySpec(modulus, pubExp);
    RSAPublicKey Key = (RSAPublicKey)keyFactory.generatePublic(priKeySpec);

    // Calculate Hash
    MessageDigest sha1 = MessageDigest.getInstance("SHA-512");
    byte[] digest = sha1.digest(data);     
    // Encrypt digest
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, Key);
    byte[] response = cipher.doFinal(digest);
