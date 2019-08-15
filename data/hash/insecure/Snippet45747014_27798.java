        // set mode to encrypt
        AesCipher.init(Cipher.ENCRYPT_MODE, key);

        DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());

        // get the digest of the file
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(bytes);

        // encrypt digest and write it to file
        byte [] encryptedHash = AesCipher.doFinal(hash);
        toServer.writeInt(encryptedHash.length);
        toServer.write(encryptedHash);

        // write file name to server
        toServer.writeUTF(fileName);

        //encrypt file
        byte[] encryptedByte = AesCipher.doFinal(bytes);

        // write file to server
        toServer.writeInt(encryptedByte.length);
        toServer.write(encryptedByte);
        socket.close();
