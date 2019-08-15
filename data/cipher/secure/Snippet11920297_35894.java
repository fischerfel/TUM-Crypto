Cipher enc = Cipher.getInstance("AES/CBC/PKCS5Padding");
enc.init(Cipher.ENCRYPT_MODE, key);
AlgorithmParameters params = enc.getParameters();
IvParameterSpec iv = params.getParameterSpec(IvParameterSpec.class);
out.write(iv.getIV());
out = new CipherOutputStream(enc, out); 
