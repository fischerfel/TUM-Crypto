SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(), algorithm);
Cipher cipher = Cipher.getInstance(algorithm);
cipher.init(Cipher.ENCRYPT_MODE, sks);
CipherOutputStream cos = new CipherOutputStream(outputStream, cipher);
int b;
    byte[] d = new byte[8];
    while((b = inputStream.read(d)) != -1){
        cos.write(d, 0, b);
