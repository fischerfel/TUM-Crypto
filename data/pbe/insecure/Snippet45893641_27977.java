public class NewPBKDF2 {

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String originalPassword = "A3E9907E59A6379DB6A9C2657D242A64886D5B21E3586B3D4C2B4E6329570A10";
        String generatedSecuredPasswordHash = generateStorngPasswordHash(originalPassword);
        System.out.println(generatedSecuredPasswordHash);
    }

    private static String generateStorngPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 901;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();
        String salt1 = Base64.getEncoder().encodeToString(salt);
        int length = 24;




        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, length * 8 );
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        String hash1 = bytesToHex(hash);

        try {
            hash1 = Base64.getEncoder().encodeToString(hash1.getBytes("utf-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(NewPBKDF2.class.getName()).log(Level.SEVERE, null, ex);
        }


        return "PBKDF2$sha256$"+ iterations +"$"+salt1+"$"+hash1;
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");

        byte[] salt = new byte[12];


        sr.nextBytes(salt);
        return salt;
    }

   static char[] hexArray = "0123456789ABCDEF".toCharArray();
     public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

     public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

}
