String encryptionAlgorithm = "SHA-1";
String valueToEncrypt = "StackOverflow";
MessageDigest msgDgst = MessageDigest.getInstance(encryptionAlgorithm);
byte[] sha1hash = new byte[40];
msgDgst.update(valueToEncrypt.getBytes(), 0, valueToEncrypt.length());
sha1hash = md.digest();
