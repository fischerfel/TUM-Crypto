    /**
     * Encrypts the data passed to it using Hmac-SHA1.
     * 
     * @param dataToEncrypt
     *            data that is to be encrypted.
     * @return The token that is generated after encrypting data.
     */
    public static String convertDataToHmacSHA1(final String dataToEncrypt) {
        String returnString;
        try {
            // Get an hmac_sha1 key from the raw key bytes
            final byte[] keyBytes = HMAC_SHA1_KEY.getBytes();
            final SecretKeySpec signingKey = new SecretKeySpec(keyBytes,
                    "HmacSHA1");

            // Get an hmac_sha1 Mac instance and initialize with the signing key
            final Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);

            // Compute the hmac on input data bytes
            final byte[] rawHmac = mac.doFinal(dataToEncrypt.getBytes());

            final StringBuffer stringBuffer = new StringBuffer();
            for (byte b : rawHmac) {
                stringBuffer.append(String.format("%02x", b));
            }
            returnString = stringBuffer.toString();
            Log.e("Token", returnString);
            return returnString;
        } catch (Exception e) {
            Log.e(TAG, "" + e);
        }
        return returnString;
    }
