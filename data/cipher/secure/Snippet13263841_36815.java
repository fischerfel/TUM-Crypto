String Decrypt(String text) throws Exception {
    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

    // here
    byte[] results = cipher.doFinal(Base64.decode(text));

    int results1 = cipher.doFinal(Base64.decode(text), 0, Base64.decode(text).length, results, 0);
    System.out.println("String resultssssssssssssss " + results1);
    return new String(results, "UTF-8");
}

String Encrypt(String text)
    throws Exception {
    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    System.out.println("String input : " + text);

    cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

    // and here
    byte[] results = cipher.doFinal(text.getBytes());

    int results1 = cipher.doFinal(text.getBytes(), 0, text.getBytes().length, results, 0);
    return Base64.encode(results);
}
