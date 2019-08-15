    KeySpec spec = new PBEKeySpec(PASSWORD.toCharArray());
    SecretKey tmp = factory.generateSecret(spec);
    SecretKeySpec secret = new SecretKeySpec(tmp.getEncoded(), "PBEWithHmacSHA512AndAES_128");
    Cipher cipher = Cipher.getInstance("PBEWITHHMACSHA512ANDAES_128");
    IvParameterSpec iv = new IvParameterSpec(new byte[16]);
    PBEParameterSpec pbeSpec = new PBEParameterSpec(SALT.getBytes(), 4096, iv);
    cipher.init(Cipher.ENCRYPT_MODE, secret, pbeSpec);
