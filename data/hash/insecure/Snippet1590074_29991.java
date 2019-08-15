String encryptionAlgorithm = "MD5";
String valueToEncrypt = "StackOverflow";
MessageDigest msgDgst = MessageDigest.getInstance(encryptionAlgorithm);
msgDgst.update(valueToEncrypt.getBytes(), 0, valueToEncrypt.length());
String md5 = new BigInteger(1, msgDgst.digest()).toString(16) ;
System.out.println(md5);
