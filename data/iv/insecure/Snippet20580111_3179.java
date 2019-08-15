    public static void main(String[] args) throws Exception
 {
        String encstring = "JO0blEp+nEl5nNhgUqoZRJNecogM1XHIXUCatPOJycs=";           
        String salt1 = "1c4dd21d7ba43bdd";
        String keyStr = "8d6ea4d3e6f8c4f8641516baa5e42b85";


        byte[] keyBytes = Hex.decodeHex(keyStr.toCharArray());

        SecretKey secret2 = new SecretKeySpec(keyBytes, "AES");

        byte[] iv = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        AlgorithmParameterSpec params = new IvParameterSpec(iv);


        Cipher cipher2 = Cipher.getInstance("AES/CBC/ISO10126PADDING", "SunJCE");


        cipher2.init(Cipher.DECRYPT_MODE, secret2, params);  
        byte[] encryptedString = Base64.decodeBase64(encstring.getBytes());
        byte[] plaintext1 = cipher2.doFinal(encryptedString);

        System.out.println(new String(plaintext));   
        }
    }
