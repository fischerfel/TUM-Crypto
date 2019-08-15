    private static final String EXPECTED_SIGNATURE = "cd3ccc949251e0ece014d620bbf306e7";

    @Test
    public void testEnc() throws NoSuchAlgorithmException, InvalidKeyException {
        String key = "key";
        String secret = "secret";
        String payload = "{'method': 'addUserFavoriteSong', 'parameters': {'songID': 0}, 'header': {'wsKey': 'key', 'sessionID': 'sessionID'}}";

        String signature = getHmacMD5(payload, secret);
        assertEquals(EXPECTED_SIGNATURE, signature);

    }

    public static String getHmacMD5(String payload, String secret) {
        String sEncodedString = null;
        try {
            SecretKeySpec key = new SecretKeySpec((secret).getBytes("UTF-8"), "HmacMD5");
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(key);

            byte[] bytes = mac.doFinal(payload.getBytes("UTF-8"));

            StringBuffer hash = new StringBuffer();

            for (int i=0; i<bytes.length; i++) {
                String hex = Integer.toHexString(0xFF &  bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            sEncodedString = hash.toString();
        }
        catch (UnsupportedEncodingException e) {}
        catch(InvalidKeyException e){}
        catch (NoSuchAlgorithmException e) {}
        return sEncodedString ;
    }
