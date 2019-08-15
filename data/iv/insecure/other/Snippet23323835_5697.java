String plainText = "Hello, World! This is a Java/Javascript AES test.";
SecretKey key = new SecretKeySpec(
    Base64.decodeBase64("u/Gu5posvwDsXUnV5Zaq4g=="), "AES");
AlgorithmParameterSpec iv = new IvParameterSpec(
    Base64.decodeBase64("5D9r9ZVzEYYgha93/aUK2w==")); 
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, key, iv);
System.out.println(Base64.encodeBase64String(cipher.doFinal(
    plainText.getBytes("UTF-8"))));
