secretBytes = loadKey(); 
secret = new SecretKeySpec(secretBytes, "AES"); // I thought the key will be reconstructed here?
