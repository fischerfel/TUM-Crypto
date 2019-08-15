String key = "qwer1234qwetr123wqw";
        String x = "sadgsagd:%%^%ghsagdh";
        byte[] keyBytes = new byte[1024];
        byte[] plaintext = x.getBytes();
        byte[] tdesKeyData = key.getBytes();

    Cipher c3des = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, "DESede");
    IvParameterSpec ivspec = new IvParameterSpec(keyBytes);

    c3des.init(Cipher.ENCRYPT_MODE, myKey, ivspec);
    byte[] cipherText = c3des.doFinal(plaintext);
    int hash = Base64.encode(cipherText).hashCode();

    return Base64.encode(cipherText);`
