public static String decrypt(String src) {
        String decrypted = "";
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            byte[] array= Base64.decode(src);
            byte[] ivData=Arrays.copyOfRange(array,0,16);
            byte[] encrypted = Arrays.copyOfRange(array, 16, array.length);
            // Init the cipher with decrypt mode, key, and IV bytes array (no more hardcoded)
            cipher.init(Cipher.DECRYPT_MODE, makeKey(), new IvParameterSpec(ivData));
            // Decrypt same old way
            decrypted = new String(cipher.doFinal(encrypted));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return decrypted;
    }
