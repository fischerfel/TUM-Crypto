public boolean CheckSignature(byte[] aMessage, byte[] aSignature,
            PublicKey aPK) {
        try {
            // get an instance of a cipher with RSA with ENCRYPT_MODE
            // Init the signature with the private key
            // decrypt the signature
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, aPK);
            byte[] decrypted_digest =  cipher.doFinal(aSignature);

            // get an instance of the java.security.MessageDigest with MD5
            MessageDigest md5_digest = MessageDigest.getInstance("MD5");

            // process the digest
            md5_digest.update(aMessage);
            byte[] digest = md5_digest.digest();

            // check if digest1 == digest2
            if (decrypted_digest == digest) {
                return true;
            }else {
                return false;
            }

        } catch (Exception e) {
            System.out.println("Verify signature error");
            e.printStackTrace();
            return false;
        }
    }
