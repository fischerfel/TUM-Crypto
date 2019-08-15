public static String Encrypt(String input, String passphrase)
    {
        if (input.equalsIgnoreCase("") || passphrase.equalsIgnoreCase(""))
            return "";
        else
        {
            byte[] key, iv;

            byte[] passphrasedata = null;
            try
            {
                passphrasedata = passphrase.getBytes("UTF-8");
            }
            catch (UnsupportedEncodingException e1)
            {
                e1.printStackTrace();
            }
            byte[] currentHash = new byte[0];
            MessageDigest md = null;
            try
            {
                md = MessageDigest.getInstance("SHA-256");
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
            currentHash = md.digest(passphrasedata);

            iv = new byte[16];
            return Base64.encodeToString(EncryptStringToBytes(input, currentHash, iv), Base64.NO_WRAP);
        }
    }

static byte[] EncryptStringToBytes(String plainText, byte[] Key, byte[] IV)
    {
        if (plainText == null || plainText.length() <= 0)
        {
            Log.e("error", "plain text empty");
        }
        if (Key == null || Key.length <= 0)
        {
            Log.e("error", "key is empty");
        }
        if (IV == null || IV.length <= 0)
        {
            Log.e("error", "IV key empty");
        }
        byte[] encrypted;

        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec myKey = new SecretKeySpec(Key, "AES");
            IvParameterSpec IVKey = new IvParameterSpec(IV);
            cipher.init(Cipher.ENCRYPT_MODE, myKey, IVKey);

            encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
            return encrypted;
        }
        catch (InvalidKeyException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e)
        {
            e.printStackTrace();
        }
        catch (InvalidAlgorithmParameterException e)
        {
            e.printStackTrace();
        }
        catch (IllegalBlockSizeException e)
        {
            e.printStackTrace();
        }
        catch (BadPaddingException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return null;
    }
