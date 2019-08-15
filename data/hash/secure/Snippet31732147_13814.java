void saveCredentials(String username, String password) {
 /* create some random salt bytes - the value doesn't need to be secret (which is
  why we can save it) but it must be unpredictable and unique per-user */
 SecureRandom sr = new SecureRandom();
 byte[] salt = new byte[16];
 sr.nextBytes(salt);

  // hash the (salt + password)
  // hashing algorithms vary, but for now, SHA256 is a reasonable choice
  try {
     MessageDigest hasher = MessageDigest.getInstance("SHA-256");
     hasher.update(salt);
     hasher.update(password.getBytes("UTF-8"));
     byte[] hashedbytes = hasher.digest();

     // we can now save the salt and the hashed bytes to a file,
     //  SharedPreference or any other storage location
     savedata(username, salt, hashedbytes);

  } catch (Exception e) {
     // do something sensible on errors
  }

}
