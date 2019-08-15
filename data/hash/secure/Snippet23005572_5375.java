MessageDigest digest = MessageDigest.getInstance("SHA-256");

// call digest.update(byte[]) for all the chunks of the file

byte[] hash = digest.digest();
