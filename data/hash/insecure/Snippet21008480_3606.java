public byte[] Sign(byte[] aMessage) {

        try {
            // get an instance of a cipher with RSA with ENCRYPT_MODE
            // Init the signature with the private key
            // Compute signature
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, thePrivateKey);

            Signature instance = Signature.getInstance("MD5withRSA");
            instance.initSign(thePrivateKey);

            // get an instance of the java.security.MessageDigest with MD5
            // process the digest
            MessageDigest md5_digest = MessageDigest.getInstance("MD5");
            md5_digest.update(aMessage);
            byte[] digest = md5_digest.digest();

            // return the encrypted digest
            byte[] cipherText = cipher.doFinal(digest);

            instance.update(cipherText);            
            byte[] signedMSG = instance.sign();

            return signedMSG;

        } catch (Exception e) {
            System.out.println("Signature error");
            e.printStackTrace();
            return null;
        }

    }
