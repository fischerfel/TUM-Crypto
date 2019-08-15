MessageDigest md = MessageDigest.getInstance("SHA-256");

md.update("5nonce=5".getBytes()); 
byte[] digest = md.digest();
