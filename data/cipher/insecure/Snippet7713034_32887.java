String key = PropertyManager.getInstance().getProperty("phikey");
    try {
        ecipher = Cipher.getInstance("AES");
        dcipher = Cipher.getInstance("AES");
        byte[] raw = new BASE64Decoder().decodeBuffer(key);
        SecretKey skey = new SecretKeySpec(raw, "AES");
        ecipher.init(Cipher.ENCRYPT_MODE, skey);
        dcipher.init(Cipher.DECRYPT_MODE, skey);
    } catch (NoSuchAlgorithmException e) {
        log.error("No encryption algorithm present!", e);
    } catch (NoSuchPaddingException e) {
        log.error("No such padding for encryption!", e);
    } catch (InvalidKeyException e) {
        log.error("Invalid key exception!", e);
    } catch (IOException e) {
        log.error("Unable to decode encryption key!", e);
    }
