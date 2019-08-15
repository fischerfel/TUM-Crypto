byte[] keyBytes = "1234123412341234".getBytes();
final byte[] ivBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 
     0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f }; //example

final SecretKey key = new SecretKeySpec(keyBytes, "AES");
final IvParameterSpec IV = new IvParameterSpec(ivBytes);
final Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding"); 
cipher.init(Cipher.ENCRYPT_MODE, key, IV);

//assuming your Socket is called "socket"
CipherOutputStream cstream = new CipherOutputStream(socket.getOutputStream(), cipher);
... 
//code to write ChatMessage object
