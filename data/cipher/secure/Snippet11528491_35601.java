Cipher contentCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
contentCipher.init(Cipher.ENCRYPT_MODE, contentEncryptionKey);
AlgorithmParameters params = contentCipher.getParameters();
byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
