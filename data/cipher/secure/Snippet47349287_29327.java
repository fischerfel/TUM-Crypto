Cipher c = Cipher.getInstance("RSA/NONE/OAEPWithSHA1AndMGF1Padding");
        c.init(Cipher.ENCRYPT_MODE, rsa);
        return c.doFinal(aes.getEncoded());
