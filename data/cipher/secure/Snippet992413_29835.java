/* Encrypt the message. */
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, secret);
AlgorithmParameters params = cipher.getParameters();
byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
byte[] ciphertext = cipher.doFinal("Hello, World!".getBytes("UTF-8"));
