    String key = "1234567890123456";
    String source = "The quick brown fox jumped over the lazy dog";

    byte[] raw = key.getBytes();
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

    // Instantiate the cipher
    Cipher cipher = Cipher.getInstance("AES");

    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

    byte[] encrypted = cipher.doFinal(source.getBytes());
    System.out.println(new String(Base64.encodeBase64(encrypted)));
