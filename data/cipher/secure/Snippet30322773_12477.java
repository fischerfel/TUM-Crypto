Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, secret);
AlgorithmParameters params = cipher.getParameters();
iv = params.getParameterSpec(IvParameterSpec.class).getIV();
ciphertext = cipher.doFinal(cleartext.getBytes("UTF-8"));
System.out.println("IV:" + Base64.encode(iv));
System.out.println("Cipher text:" + Base64.encode(ciphertext));
