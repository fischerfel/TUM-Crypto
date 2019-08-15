public static String encrypt(String strToEncrypt,  String key)
    {
        try
        {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
            int blockSize = cipher.getBlockSize();
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOf(strToEncrypt.getBytes(), blockSize));
            byte[] dataToDecrypt = Arrays.copyOfRange(strToEncrypt.getBytes(), blockSize, strToEncrypt.getBytes().length);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(dataToDecrypt);
            return Base64.getEncoder().encodeToString(encrypted);
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
