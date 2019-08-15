private final static String ALGORITHM = "AES";


    public static String cipher(String secretKey, String data) throws Exception {



        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), secretKey.getBytes(), 128, 256);

        SecretKey tmp = factory.generateSecret(spec);

        SecretKey key = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);



        Cipher cipher = Cipher.getInstance(ALGORITHM);

        cipher.init(Cipher.ENCRYPT_MODE, key);



        return toHex(cipher.doFinal(data.getBytes()));

    }


    public static String decipher(String secretKey, String data) throws Exception {



        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), secretKey.getBytes(), 128, 256);

        SecretKey tmp = factory.generateSecret(spec);

        SecretKey key = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);



        Cipher cipher = Cipher.getInstance(ALGORITHM);



        cipher.init(Cipher.DECRYPT_MODE, key);



        return new String(cipher.doFinal(toByte(data)));

    }


    private static byte[] toByte(String hexString) {

        int len = hexString.length()/2;



        byte[] result = new byte[len];



        for (int i = 0; i < len; i++)

            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();

        return result;

    }


    public static String toHex(byte[] stringBytes) {

        StringBuffer result = new StringBuffer(2*stringBytes.length);



        for (int i = 0; i < stringBytes.length; i++) {

            result.append(HEX.charAt((stringBytes[i]>>4)&0x0f)).append(HEX.charAt(stringBytes[i]&0x0f));

        }



        return result.toString();

    }
    private final static String HEX = "0123456789ABCDEF";
