InetAddress inetAddress = InetAddress.getLocalHost();
KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
SecretKey myDesKey = keygenerator.generateKey();
Cipher desCipher;

// Create the cipher
desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

// Initialize the cipher for encryption
desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);

ByteArrayOutputStream baos = new ByteArrayOutputStream();
ObjectOutputStream oos = new ObjectOutputStream(baos);
oos.writeObject((Object) myDesKey);
oos.flush();
final byte[] bytes = baos.toByteArray();

DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, inetAddress, 999);
socket.send(sendPacket);
