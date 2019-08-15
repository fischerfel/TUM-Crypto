try
{
    // Transfer of the RSA 4096 key to encrypt session key
    X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Storage.getKeyPair().getPublic().getEncoded());
    String keyBuffer = HexEncoder.byteArrayToHexString(x509EncodedKeySpec.getEncoded());
    this.targetUser.getOutputStream().write(keyBuffer + "\n");
    this.targetUser.getOutputStream().flush();
    System.out.println("After key write");
    System.out.println("pubkey buffer: " + HexEncoder.hexStringToByteArray(keyBuffer).hashCode());
    System.out.println("string key: " + keyBuffer);

    this.targetUser.initDecryption();

    String loginDecrypted = this.targetUser.getEncryptionTool().decrypt(this.targetUser.getInputStream().readLine());
    if (loginDecrypted.startsWith("LOGIN"))
    {   
        if (this.doLoginProcedure(loginDecrypted)) // <- The main login stuff happens here
        {   
            try
            {
                // Old key transfer 
                /**byte[] clientPubkeyBuffer = HexEncoder.hexStringToByteArray(this.targetUser.getInputStream().readLine());

                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(clientPubkeyBuffer);
                PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);**/

                // Receive the encrypted session key from client
                byte[] wrappedKey = new byte[16];
                this.targetUser.getRawInput().read(wrappedKey, 0, 16);

                System.out.println("key wrapped: " + HexEncoder.byteArrayToHexString(wrappedKey));

                // Decrypt the session key
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.DECRYPT_MODE, Storage.getKeyPair().getPrivate());
                SecretKey key = new SecretKeySpec(wrappedKey, "AES");

                if (!IMServer.getServer().getCache().isKeyCached((SecretKey)key))
                {
                    IMServer.getServer().getCache().addKey(this.targetUser.getUserID(), (SecretKey)key);
                }

                // Old encryption initialization
                /**this.targetUser.initEncryption(publicKey);**/

                // New encryption
                this.targetUser.setEncryptionStreams(Encryption.decryptInputStream(this.targetUser.getRawInput(), (SecretKey)key), Encryption.encryptOutputStream(this.targetUser.getRawOutput(), (SecretKey)key));

                IMServer.getServer().getClientHandlerByUser(targetUser.getUserID()).start();
            }
            catch (Exception ex)
            {
                this.targetUser.logout();
                ex.printStackTrace();
            }
        }
        else
        {
            targetUser.logout();
            targetUser = null;
        }
    }
}
catch (IOException ex)
{
    IMServer.getServer().getSystemLogger().log(Logger.ERROR, "Failure during login procedure");
}
