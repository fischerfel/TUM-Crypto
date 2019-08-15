// This concatenates size+date and the md5 hash, and then creates a SHA-256 hash from that value.

byte[] sizeBytes = (size + date).getBytes();  //Get byte array of size and date concatenation
byte[] tokenBytes = new byte[md5name.length + sizeBytes.length]; //Create the right size byte array to fit md5 and size+date

System.arraycopy(sizeBytes, 0, tokenBytes, 0, sizeBytes.length); // copies the size+date byte array into the first part of the token bytes byte array.
System.arraycopy(md5name, 0, tokenBytes, sizeBytes.length, md5name.length); //concatenates the md5 hash and size+bytes into the tokenBytes array 

// SHA256
MessageDigest sha256 = MessageDigest.getInstance("SHA-256");//Get the SHA-256 algorithm instance
sha256.update(tokenBytes); // give it a value to hash
byte[] tokenHash = sha256.digest(); // create SHA-256 hash from token bytes
