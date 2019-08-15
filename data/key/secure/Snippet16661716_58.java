private void establishConnection() {
    try {
        //generate public/private key pair for communicating with initially
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(1024, random);

        KeyPair pair = keyGen.generateKeyPair();
        PrivateKey privKey = pair.getPrivate();
        PublicKey pubKey = pair.getPublic();

        //send public key to client
        byte[] bytesPubKey = pubKey.getEncoded();
        out.writeInt(bytesPubKey.length);
        out.write(bytesPubKey, 0, bytesPubKey.length);

        //read in secret key to use for further communications
        int numBytesSecretKey = in.readInt();
        byte[] bytesSecretKey = new byte[numBytesSecretKey];
        in.readFully(bytesSecretKey, 0, numBytesSecretKey);

        decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, privKey, decryptCipher.getParameters());
        byte[] byteDecriptedSecretKey = decryptCipher.doFinal(bytesSecretKey);
        SecretKeySpec secretKeySpec = new SecretKeySpec(byteDecriptedSecretKey,"AES");

        //send back acknowledgment encoded with secret key: ACK
        encryptCipher = Cipher.getInstance("AES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] cipheredACK = encryptCipher.doFinal("ACK".getBytes());

        out.writeUTF(new String(cipheredACK));

        //read password from client
        String encryptedPassword = in.readUTF();
        decryptCipher = Cipher.getInstance("AES");
        decryptCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] byteDecriptedAck = decryptCipher.doFinal(encryptedPassword.getBytes());

        if(!(new String(byteDecriptedAck).equals("password"))) {
            System.err.println("Access Denied. Client password incorrect. Terminate communications.");
            System.exit(1);
        } else {
            System.err.println("Access Granted.");
        } 
    } catch (Exception e) {
        e.printStackTrace();
    }
} 
