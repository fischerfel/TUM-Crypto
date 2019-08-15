byte[] key = (Password+Username).getBytes("UTF-8"); // depends on your implementation
MessageDigest sha = MessageDigest.getInstance("SHA-1");
key = sha.digest(key);
key = Arrays.copyOf(key, 16); // AES uses 16 byte of key as a parameter (?)
