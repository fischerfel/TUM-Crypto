public Object decryptObject(String base64Data, String base64IV) throws Exception {

        System.out.println("decryptObject " + base64Data);
        System.out.println("decryptObject " + base64IV);

        String urlDecodedData=URLDecoder.decode(base64Data,"UTF-8");
        // Decode the data
        byte[] encryptedData = Base64.decodeBase64(urlDecodedData.getBytes());

        String urlDecodedIV=URLDecoder.decode(base64IV,"UTF-8");
        // Decode the Init Vector
        byte[] rawIV = Base64.decodeBase64(urlDecodedIV.getBytes());
        System.out.println("rawIV " + rawIV.length);

        // Configure the Cipher
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivSpec = new IvParameterSpec(rawIV);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(keyString.getBytes());
        byte[] key = new byte[16];
        System.arraycopy(digest.digest(), 0, key, 0, key.length);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec); //////////////////////////////This is the error line

        // Decrypt the data..
        byte[] decrypted = cipher.doFinal(encryptedData);

        // Deserialize the object       
        ByteArrayInputStream stream = new ByteArrayInputStream(decrypted);
        ObjectInput in = new ObjectInputStream(stream);
        Object obj = null;
        try {
            obj = in.readObject();
            System.out.println("objobj " + obj);

        } catch (Exception e) {
            //logger.debug("Unable to encrypt view id: "+id, e);
            e.printStackTrace();
        } finally {
            stream.close();
            in.close();
        }
        return obj;
    }
