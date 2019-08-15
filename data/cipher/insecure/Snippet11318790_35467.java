// Create data array with size greater 256 bytes
byte[] SOURCE_DATA = new byte[257];     
for (int i=0;i<SOURCE_DATA.length; i++) {
    SOURCE_DATA[i] = (byte)((i+1) & 0xff);
}

// Init ciphers
Cipher encC = Cipher.getInstance("AES/ECB/PKCS5Padding");
Cipher decC = Cipher.getInstance("AES/ECB/PKCS5Padding");
encC.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(new byte[] {0,1,2,3,4,5,6,7,8,9,1,2,3,4,5,6}, "AES"));                  
decC.init(Cipher.DECRYPT_MODE, new SecretKeySpec(new byte[] {0,1,2,3,4,5,6,7,8,9,1,2,3,4,5,6}, "AES"));          

// Encrypt
ByteArrayOutputStream bos = new ByteArrayOutputStream();
CipherOutputStream cos = new CipherOutputStream(bos, encC);
DataOutputStream dos = new DataOutputStream(cos);

dos.write(SOURCE_DATA, 0, SOURCE_DATA.length);
dos.close();

byte[] ENCRYPTED_DATA = bos.toByteArray();

// Decrypt
ByteArrayInputStream bis = new ByteArrayInputStream(ENCRYPTED_DATA);
CipherInputStream cis = new CipherInputStream(bis, decC);
DataInputStream dis = new DataInputStream(cis);

byte[] DECRYPTED_DATA = new byte[SOURCE_DATA.length];
dis.read(DECRYPTED_DATA, 0, DECRYPTED_DATA.length);
cis.close();

System.out.println("Source Data:    "+toHex(SOURCE_DATA));
System.out.println("Decrypted Data: "+toHex(DECRYPTED_DATA));            
