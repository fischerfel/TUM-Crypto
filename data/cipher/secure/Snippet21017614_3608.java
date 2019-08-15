public byte[] Sign(byte[] aMessage) {

        try {
            // get an instance of a cipher with RSA with ENCRYPT_MODE
            // Init the signature with the private key
            // Compute signature
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, thePrivateKey);

            // get an instance of the java.security.MessageDigest with MD5
            // process the digest
            MessageDigest md5_digest = MessageDigest.getInstance("MD5");
            byte[] digest = md5_digest.digest(aMessage);

            // return the encrypted digest
            byte[] cipherText = cipher.doFinal(digest);

            return cipherText;

        } catch (Exception e) {
            System.out.println("Signature error");
            e.printStackTrace();
            return null;
        }

    }
