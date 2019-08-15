public static String encode(String password, String text)
        throws NoPassGivenException, NoTextGivenException {
    /*if (password.length() == 0 || password == null) {
        throw new NoPassGivenException("Please give Password");
    }

    if (text.length() == 0 || text == null) {
        throw new NoTextGivenException("Please give text");
    }*/

    try {
        SecretKeySpec skeySpec = getKey(password);
        byte[] clearText = text.getBytes("UTF8");

        //IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
        final byte[] iv = new byte[16];
        Arrays.fill(iv, (byte) 0x00);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        //System.out.println(iv);
        // Cipher is not thread safe
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);

        String encrypedValue = new Base64().encodeAsString(
                cipher.doFinal(clearText));

        //Log.d(TAG, "Encrypted: " + text + " -> " + encrypedValue);
        return encrypedValue;

    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
        e.printStackTrace();
    }
    return "";
}
public static String decode(String password, String text)
        throws NoPassGivenException, NoTextGivenException {

    /*if (password.length() == 0 || password == null) {
        throw new NoPassGivenException("Please give Password");
    }

    if (text.length() == 0 || text == null) {
        throw new NoTextGivenException("Please give text");
    }*/

    try {
        SecretKey key = getKey(password);

        //IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
        final byte[] iv = new byte[16];
        Arrays.fill(iv, (byte) 0x00);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        //System.out.println(iv);
        byte[] encrypedPwdBytes = new Base64().decodeBase64(text);
        // cipher is not thread safe
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
        byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));

        String decrypedValue = new String(decrypedValueBytes);

       // BigDecimal bd = new BigDecimal(decrypedValue);
        //Log.d(TAG, "Decrypted: " + text + " -> " + decrypedValue);
       // String data =  Long.toString(bd.longValue());
        return decrypedValue;

    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
        e.printStackTrace();
    }
    return "";
}
