    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    byte[] raw = sKey.getBytes();
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
    byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
    return new BASE64Encoder().encode(encrypted);
