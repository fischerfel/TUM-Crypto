pbKey = (PublicKey) pr.readObject();
    Cipher cipher = Cipher.getInstance("RSA/None/OAEPWithSHA1AndMGF1Padding", "BC");
