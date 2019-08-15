boolean checkPassword(String username, String password) {
  // read the info for the user that we saved in storage
  byte[] salt = readdata(username, "salt");
  byte[] correcthash = readdata(username, "pwdhash");

  // hash the password we are checking in the same way that we did
  // for the original password
  try {
     MessageDigest hasher = MessageDigest.getInstance("SHA-256");
     hasher.update(salt);
     hasher.update(password.getBytes("UTF-8"));
     byte[] testhash = hasher.digest();

     // if the password is correct, the two hashed values will match
     // - if it's wrong, the hashed values will have one or more
     // bytes that do not match
     for (int i=0; i < testhash.length; i++) {
         if (testhash[i] != correcthash[i])
             return false;  // mismatch - wrong password
     }

     // if we reach here, all the hash bytes match, so the password
     // matches the original
     return true;

  } catch (Exception e) {
     // do something sensible on errors
  }

  return false;
}
