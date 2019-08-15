public static String decrypt(String encryptedText, byte[] keyValue)
    {
        try
        {
            byte[] inputArr = Base64.getUrlDecoder().decode(encryptedText);
            SecretKeySpec skSpec = new SecretKeySpec(keyValue, "AES");
            Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
            int blockSize = cipher.getBlockSize();
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOf(inputArr, blockSize));
            byte[] dataToDecrypt = Arrays.copyOfRange(inputArr, blockSize, inputArr.length);
            cipher.init(Cipher.DECRYPT_MODE, skSpec, iv);
            byte[] result = cipher.doFinal(dataToDecrypt);
            return new String(result, StandardCharsets.UTF_8);
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
