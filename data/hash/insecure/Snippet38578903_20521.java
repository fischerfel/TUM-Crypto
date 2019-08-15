    String plainTextKey = "STRING_KEY";
    String plainText = "WORD_TO_ENCRYPT";
    // Encrypt where jo is input, and query is output and ENCRPYTION_KEy is key
    //String inputtt = "some clear text data";
    byte[] input = new byte[0];
    String skyKey;

    input = plainText.getBytes("utf-8");
    MessageDigest md;
    md = MessageDigest.getInstance("MD5");
    byte[] thedigest = md.digest(plainTextKey.getBytes("UTF-8"));
    SecretKeySpec skc = new SecretKeySpec(thedigest, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, skc);
    byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
    int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
    ctLength += cipher.doFinal(cipherText, ctLength);
    String encode = Base64.encode(cipherText);
    System.out.println(encode);
