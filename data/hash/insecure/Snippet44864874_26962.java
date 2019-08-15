// Get a random ID coressponding to the user address or whatever, here i will just use address

MessageDigest digest = MessageDigest.getInstance("MD5");
byte[] hash = digest.digest(ville.getBytes("UTF-8"));

String randomId = DatatypeConverter.printHexBinary(hash);

// Here you have a uniqueID for an address.
// You could have just used ville.hashCode() also but it might give you collisions with some different Strings.
