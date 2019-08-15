Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding"); 
SecretKeySpec keySpec = new SecretKeySpec("password12345678".getBytes(), "AES");
cipher.init(Cipher.ENCRYPT_MODE, keySpec);

AlgorithmParameters params = cipher.getParameters();
byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();

out.write(iv); //Send IV to Server
out.flush();

// THE ENCRYPTET STREAM
cos = new CipherOutputStream(out, cipher);  

while ((val = byteArrayInputStream.read(buffer, 0, 1024)) > 0) {
       cos.write(buffer, 0, val);
       cos.flush();
}

cipher.doFinal()
