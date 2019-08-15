    byte[] keyBuf= new byte[32];

    byte[] b= key.getBytes("UTF-8");
    int len= b.length;
    if (len > keyBuf.length) len = keyBuf.length;

    System.arraycopy(b, 0, keyBuf, 0, len);
    SecretKey keySpec = new SecretKeySpec(keyBuf, "AES");


    byte[] ivBuf= new byte[16];
    IvParameterSpec ivSpec = new IvParameterSpec(ivBuf);

    cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
