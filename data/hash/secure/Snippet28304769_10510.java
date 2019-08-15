 public byte[] hash(String password) throws NoSuchAlgorithmException {
     MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
     byte[] passBytes = password.getBytes();
     byte[] passHash = sha256.digest(passBytes);
     for (int i = 0; i < 10; i++) {
         passHash = sha256.digest(passHash);
     }
     return passHash;
 }
