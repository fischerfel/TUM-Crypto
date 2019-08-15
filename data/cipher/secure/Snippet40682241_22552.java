public static byte[] AESDecryption(byte[] key, byte[] data)
    {
        byte[] retval = null;
        try 
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");


            SecretKeySpec thekey = new SecretKeySpec(key,"AES");

            cipher.init(Cipher.DECRYPT_MODE, thekey);

            retval = cipher.doFinal(data);


        } 
        catch (NoSuchAlgorithmException e) 
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        catch (NoSuchProviderException e) 
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e) 
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return retval;
    }
