ObjectInputStream oIn = new ObjectInputStream(new FileInputStream(new File("key")));
SecretKeySpec spec = (SecretKeySpec) oIn.readObject();
//'key' file was saved previously


Cipher cEncrypt = Cipher.getInstance("AES");
cEncrypt.init(Cipher.ENCRYPT_MODE, spec); 
Cipher cDecrypt = Cipher.getInstance("AES");
cDecrypt.init(Cipher.DECRYPT_MODE, spec); 
//should have no problems here, I tried the ciphers out by encoding and decoding a String, works fine

ObjectOutputStream objectOutputStream= new ObjectOutputStream(new CipherOutputStream(socket.getOutputStream,cEncrypt)); 
objectOutputStream.flush(); 
ObjectInputStream objectInputStream = new ObjectInputStream(new CipherInputStream(socket.getInputStream,cDecrypt));
