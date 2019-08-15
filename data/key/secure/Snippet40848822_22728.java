public class Signature {

    String[] replacementPairs = null;

    public static void main(String args[]) throws GeneralSecurityException, IOException {

        String key = GetKey();

        long now = Instant.now().toEpochMilli();
        System.out.println("key:  " + key);
        String s = String.valueOf(now);
        System.out.println("value of s is " + s);

        String salt = s;
        String generateHmacSHA256Signature = generateHmacSHA256Signature(salt,
            key);
        System.out.println("Signature: " + generateHmacSHA256Signature);

        String urlEncodedSign = URLEncoder.encode(generateHmacSHA256Signature,
            "UTF-8");

        System.out.println("Url encoded value: " + urlEncodedSign);


    }

    public static String GetKey() {
        return Unify(new String[] {
                "1_1", "1_2", "1_3", "1_4", "1_5"
            },
            new int[] {
                4, 2, 0, 3, 1
            },
            new String[] {
                "1_2", "2_2", "3_2", "4_2", "5_2", "6_2"
            });
    }
    private static String Unify(String[] elements, int[] segments, String[] replacementPairs) {
        String key = "";
        System.out.println("Emptykey:" + key);

        for (int segment: segments) {

            if (key != null) {

                key = key.concat(elements[segment]);
            } else {
                key = elements[segment];
            }
        }

        if (replacementPairs != null) {
            for (int i = 0; i < replacementPairs.length - 1; i = i + 2) {
                key = key.replace(replacementPairs[i], replacementPairs[i + 1]);
            }
        }

        return key;

    }


    public static String generateHmacSHA256Signature(String data, String key) throws GeneralSecurityException, IOException {
        byte[] hmacData = null;

        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"),
                "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKey);
            hmacData = mac.doFinal(data.getBytes("UTF-8"));
            ByteArrayOutputStream bout = new ByteArrayOutputStream();

            new Base64Encoder().encode(hmacData, 0, hmacData.length, bout);
            return bout.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {

            throw new GeneralSecurityException(e);
        }
    }
}
