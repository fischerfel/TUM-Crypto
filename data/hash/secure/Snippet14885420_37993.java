MessageDigest hasher = MessageDigest.getInstance("SHA-256");
// Use the factory method to get the SHA-256 instance of a MessageDigest object.
hasher.update(input.getBytes());
// Update the message digest object with the bytes of the value to hash.
return hasher.digest();
// Hash the value and return the string representation.
