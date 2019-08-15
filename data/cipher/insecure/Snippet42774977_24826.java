   Cipher desCipher;
SecretKey myDesKey = null;
// Create the cipher

desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

packetIn = new DatagramPacket(dataOut, dataOut.length);
socket.receive(packetIn);
ByteArrayInputStream bais = new ByteArrayInputStream(packetIn.getData());
ObjectInputStream oos = new ObjectInputStream(bais);
myDesKey = (SecretKey) oos.readObject();
System.out.println("the key is " + myDesKey.toString());
desCipher.init(Cipher.DECRYPT_MODE, myDesKey); 
