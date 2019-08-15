Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

//Do this to get the same parameters as were used on the client side
cipher.init(Cipher.ENCRYPT_MODE, this.key);
AlgorithmParameters parameters = cipher.getParameters();

//Set to decrypt mode using the same parameters
byte[] iv = parameters.getParameterSpec(IvParameterSpec.class).getIV();
cipher.init(Cipher.DECRYPT_MODE, this.key, new IvParameterSpec(iv));
