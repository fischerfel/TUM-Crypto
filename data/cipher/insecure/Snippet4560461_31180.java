    byte[] input = etInput.getText().toString().getBytes();
    byte[] keyBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
        0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 };

    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

    // encryption pass
    cipher.init(Cipher.ENCRYPT_MODE, key);

    byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
    int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
    ctLength += cipher.doFinal(cipherText, ctLength);

    cipher.init(Cipher.DECRYPT_MODE, key);
    byte[] plainText = new byte[cipher.getOutputSize(ctLength)];
    int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);

    String strLength = new String(cipherText,"US-ASCII");
    byte[] byteCiphterText = strLength.getBytes("US-ASCII");
    Log.e("Decrypt", Integer.toString(byteCiphterText.length));

    etOutput.setText(new String(cipherText,"US-ASCII"));

    cipherText  = etOutput.getText().toString().getBytes("US-ASCII");
    Log.e("Decrypt", Integer.toString(cipherText.length));

    ptLength += cipher.doFinal(plainText, ptLength);
    Log.e("Decrypt", new String(plainText));
    Log.e("Decrypt", Integer.toString(ptLength));
