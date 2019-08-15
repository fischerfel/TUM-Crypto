private byte [] concatByteArrays(byte []a, byte [] b ) {
    byte [] result = new byte[a.length + b.length];
    System.arraycopy(a, 0, result, 0, a.length);
    System.arraycopy(b, 0, result, a.length, b.length);
    return result;
}

public void encryptSingleSecret(String serialNo, String secret) {
    try {
        byte[] secretBytes = hexStr2Bytes(secret);

        // First, encrypt the MAC key

        cipherParamSpec = new IvParameterSpec(this.ivMac);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(this.sharedKey, "AES"),
                cipherParamSpec);
        byte[] encryptedMacKey = cipher.doFinal(this.macKey);

        // Now, prepend the IV used to encrypt the mac key to the cipher

        byte [] toBeBase64Encoded = concatByteArrays(this.ivMac, encryptedMacKey);

        // Now base64-encode the result and print it out. This is for the
        // <MACKey> element

        System.out.println("<MACKey> <CipherValue>: " + Base64.encode(toBeBase64Encoded));

        // Next, encrypt the secret

        cipherParamSpec = new IvParameterSpec(this.ivHotp);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(this.sharedKey, "AES"),
                cipherParamSpec);
        byte[] encryptedSecret = cipher.doFinal(secretBytes);

        // Now, prepend the IV used to encrypt the secret to the cipher

        toBeBase64Encoded = concatByteArrays(this.ivHotp, encryptedSecret); 

        // Now base64-encode the result and print it out. This is for the
        // <Data> element

        System.out.println("<Data><Secret><CipherValue>: " + Base64.encode(toBeBase64Encoded));

        // Finally, compute the MAC over the encrypted value

        byte [] macValue = this.mac.doFinal(toBeBase64Encoded);

        // Base64-encode the result and print it out. This is for the
        // ValueMAC element

        System.out.println("<Data><Secret><ValueMAC>: " + Base64.encode(macValue));

        return;
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}
