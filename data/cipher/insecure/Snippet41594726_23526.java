public String doDESEncryption(String key, String text) {
        String encryptedInfo = "";
        try {
            byte[] theCph = null;
            byte[] theKey = null;
            byte[] theMsg = null;
            theKey = hexToBytes(key);
            theMsg = hexToBytes(text);
            DESKeySpec ks = new DESKeySpec(theKey);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);
            Cipher cf = Cipher.getInstance("DES/ECB/NoPadding");
            cf.init(Cipher.ENCRYPT_MODE, ky);
            theCph = cf.doFinal(theMsg);
            encryptedInfo = bytesToHex(theCph);
            System.out.println("Just the ePINBLOCK"+encryptedInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedInfo;
    }
