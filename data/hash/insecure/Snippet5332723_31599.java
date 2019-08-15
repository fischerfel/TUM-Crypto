     /* Compute the key and IV with OpenSSL's non-standard method. */
     final byte[] digest = new byte[32];
     final MessageDigest md5 = MessageDigest.getInstance("MD5");
     md5.update(password, 0);
     // append the salt
     md5.update(salt);
     // run the digest and output 16 bytes to the first 16 bytes to the digest array. Digest is reset
     md5.digest(digest, 0, 16);
     // write the first 16 bytes from the digest array back to the buffer
     md5.update(digest, 0, 16);
     // append the password
     md5.update(password, 0);
     // append the salt
     md5.update(salt);
     // run the digest and output 16 bytes to the last 16 bytes of the digest array
     md5.digest(digest, 16, 16);
     key = Arrays.copyOfRange(digest, 0, 16);
     iv = Arrays.copyOfRange(digest, 16, 24);
