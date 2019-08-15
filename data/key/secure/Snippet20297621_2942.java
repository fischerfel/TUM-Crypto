protected Cipher aes_Gen_with_Key(byte[] key)
    {
        Cipher cipher=null;
        try
        {
        byte[] key_hash = (key).toString().getBytes("UTF-8");
        key_hash = Arrays.copyOf(key_hash, 32); // use only first 256 bit
        SecretKeySpec secretKeySpec = new SecretKeySpec(key_hash, "AES"); 
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        } catch (Exception e) {
            System.out.println("Error Occured");
        }
        return cipher;
    }

    protected Cipher aes_Dec_with_Key(byte[] key,byte[] iv)
    {
        Cipher cipher=null;
        try
        {
        byte[] key_hash = (key).toString().getBytes("UTF-8");
        key_hash = Arrays.copyOf(key_hash, 32); // use only first 256 bit
        SecretKeySpec secretKeySpec = new SecretKeySpec(key_hash, "AES");
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE,secretKeySpec,new IvParameterSpec(iv));
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return cipher;
    }
