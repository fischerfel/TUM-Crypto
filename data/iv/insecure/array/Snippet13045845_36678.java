byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, }; // use different random value
AlgorithmParameterSpec algorithmSpec = new IvParameterSpec(iv);
Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
ecipher.init(Cipher.ENCRYPT_MODE, skeySpec, algorithmSpec);
