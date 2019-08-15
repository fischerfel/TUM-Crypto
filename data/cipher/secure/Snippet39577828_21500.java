byte[] convertHexString = DatatypeConverter.parseHexBinary(key);
String newKey = new String(convertHexString);
byte[] keyByte = newKey.getBytes();
String[] asciiArray = new String[256];
FileInputStream file = new FileInputStream(path);
Cipher aesCipher = Cipher.getInstance(transformation);
for(int i = 0;i<256; i++){
    arrayInts[i] = Character.toString((char)i);
    byte[] b = asciiArray [i].getBytes();
    byte[] result = new byte[b.length + keyByte.length]; 
    System.arraycopy(b, 0, result, 0, b.length); 
    System.arraycopy(keyByte, 0, result, b.length, keyByte.length); 
    FileOutputStream out = new FileOutputStream("AESencrypt_view" + String.valueOf(i)+".jpg");
    SecretKeySpec key1 = new SecretKeySpec(result,"AES");
    aesCipher.init(Cipher.DECRYPT_MODE, key1);
    CipherOutputStream  outSt = new CipherOutputStream(out,aesCipher);
    byte[] buf = new byte[1024];
    int read;
    while((read=file.read(buf))!=-1){
        outSt.write(buf, 0, read);

    }
    //file.close();
    out.flush();
    outSt.flush();
}
