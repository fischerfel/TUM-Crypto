   String hexCipher = null;
    try {
        byte[] byteClearText = pwd.getBytes("UTF-8");
        byte[] ivBytes = SecurityUtil.hexToBytes("0000000000000000");
        // read secretkey from key file
        byte[] secretKeyByte = secretKey.getBytes();
        Cipher cipher = null;
        SecretKeySpec key = new SecretKeySpec(secretKeyByte, SecurityConstant.BLOW_FISH_ALGO);
        // Create and initialize the encryption engine
        cipher = Cipher.getInstance(SecurityConstant.BLOW_FISH_CBC_ZEROBYTE_ALGO, SecurityConstant.BC);

        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec); // throws exception
        byte[] cipherText = new byte[cipher.getOutputSize(byteClearText.length)];
        int ctLength = cipher.update(byteClearText, 0, byteClearText.length, cipherText, 0);
        ctLength += cipher.doFinal(cipherText, ctLength);
        hexCipher = SecurityUtil.bytesToHex(cipherText);// hexdecimal password stored in DB
    } catch (Exception e) {
        ExceptionLogger.logException(logger, e);
    }
