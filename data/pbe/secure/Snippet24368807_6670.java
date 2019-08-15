private static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }



public static String encrypt(String password,String key) throws NoSuchAlgorithmException, InvalidKeySpecException {

       int iterations = 4096;
        char[] chars = password.toCharArray();
        byte[] salt = key.getBytes();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 128 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return toHex(hash);
    }
