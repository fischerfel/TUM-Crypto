MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

//somewhere later, just need to hash a key, nothing else
messageDigest.update(key);
byte[] bytes = messageDigest.digest(); 
