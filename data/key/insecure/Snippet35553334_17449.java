    public static String getHashString() {
        String algorithm = "HmacSHA256";

        String hashKey = "some_key";
        String message = "abcdefg";

        String hexed = "";

        try {
            Mac sha256_HMAC = Mac.getInstance(algorithm);
            SecretKeySpec secret_key = new SecretKeySpec(hashKey.getBytes(), algorithm);
            sha256_HMAC.init(secret_key);

            byte[] hash = sha256_HMAC.doFinal(message.getBytes("UTF-8"));

            // it doesn't work for me.
//            hexed = new String(hash, "UTF-8");

            // it works.
            hexed = bytesToHex(hash);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return hexed;
    }

    public static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();
    public static String bytesToHex(final byte[] data ) {
        final int l = data.length;
        final char[] hexChars = new char[l<<1];
        for( int i=0, j =0; i < l; i++ ) {
            hexChars[j++] = HEX_DIGITS[(0xF0 & data[i]) >>> 4];
            hexChars[j++] = HEX_DIGITS[0x0F & data[i]];
        }
        return new String(hexChars);
    }
