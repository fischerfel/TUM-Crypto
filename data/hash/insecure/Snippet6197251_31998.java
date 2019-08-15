void checkVersion() {
  String[] v = loadStrings("version.txt");
  for(int i=0; i<v.length; i++) {
    String[] piece = split(v[i], " ");  //BREAKS INTO FILENAME, HASH
    println("Checking "+piece[0]+"..."+piece[1]);

    if(checkHash(piece[0], piece[1])) {
      println("ok!");
    } else {
      println("NOT OKAY!");
      //CONTINUE TO DOWNLOAD FILE AND THEN CALL CHECKVERSION AGAIN
    }
  }
}

boolean checkHash(String path_, String hash_) {
  return createHash(path_).equals(hash_);
}

byte[] messageDigest(String message, String algorithm) {
  try {
  java.security.MessageDigest md = java.security.MessageDigest.getInstance(algorithm);
  md.update(message.getBytes());
  return md.digest();
  } catch(java.security.NoSuchAlgorithmException e) {
    println(e.getMessage());
    return null;
  }
} 

String createHash(String path_) {
  byte[] md5hash = messageDigest(new String(loadBytes(path_)),"MD5");
  BigInteger bigInt = new BigInteger(1, md5hash);
  return bigInt.toString(16);
}
