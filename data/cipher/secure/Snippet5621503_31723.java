//Reads private key from file
//StringPasswordFinder is my tmp implementation of PasswordFinder
PEMReader pemReader = new PEMReader(new FileReader("/path/to/server.key"), new StringPasswordFinder());
KeyPair keyPair = (KeyPair) pemReader.readObject();
PrivateKey pk = keyPair.getPrivate();
//text for encryption
String openText = "openText";
//encryption
Cipher rsaCipher = Cipher.getInstance("RSA", "BC");
rsaCipher.init(Cipher.ENCRYPT_MODE, pk);
byte[] encrypted = rsaCipher.doFinal(openText.getBytes("utf-8"));
