public String encode(Context context, String password, String text)
        throws NoPassGivenException, NoTextGivenException {
    if (password.length() == 0 || password == null) {
        throw new NoPassGivenException("Please give Password");
    }

    if (text.length() == 0 || text == null) {
        throw new NoTextGivenException("Please give text");
    }

    try {
        SecretKeySpec skeySpec = getKey(password);
        byte[] clearText = text.getBytes("UTF8");


        //IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
        final byte[] iv = new byte[16];
        Arrays.fill(iv, (byte) 0x00);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        // Cipher is not thread safe
                    //EDITED AFTER RIGHT ANSWER FROM
                    //*** Cipher cipher = Cipher.getInstance("AES");   ***//
                    // TO  
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");


        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);

        String encrypedValue = Base64.encodeToString(
                cipher.doFinal(clearText), Base64.DEFAULT);
        Log.d(TAG, "Encrypted: " + text + " -> " + encrypedValue);
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


public SecretKeySpec getKey(String password)
        throws UnsupportedEncodingException {


    int keyLength = 128;
    byte[] keyBytes = new byte[keyLength / 8];
    // explicitly fill with zeros
    Arrays.fill(keyBytes, (byte) 0x0);

    // if password is shorter then key length, it will be zero-padded
    // to key length
    byte[] passwordBytes = password.getBytes("UTF-8");
    int length = passwordBytes.length < keyBytes.length ? passwordBytes.length
            : keyBytes.length;
    System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
    return key;
}
