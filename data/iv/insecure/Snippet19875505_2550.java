public static String Encrypt(String text, String key)
    {
        String Encoded = "";
        try {

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] keyBytes = new byte[16];
            byte[] b = key.getBytes("UTF-8");
            int len = b.length;
            if (len > keyBytes.length)
            {
                len = keyBytes.length;
            }
            System.arraycopy(b, 0, keyBytes, 0, len);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            //IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
            final byte[] iv = new byte[16];
            Arrays.fill(iv, (byte) 0x00);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec,ivSpec);
            byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
            Encoded = Base64.encodeToString(results,Base64.URL_SAFE);

        } catch (NoSuchAlgorithmException e) {
            Log.v(TAG, e.getMessage());
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            Log.v(TAG, e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            Log.v(TAG, e.getMessage());
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            Log.v(TAG, e.getMessage());
            e.printStackTrace();
        } catch (BadPaddingException e) {
            Log.v(TAG, e.getMessage());
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            Log.v(TAG, e.getMessage());
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            Log.v(TAG, e.getMessage());
            e.printStackTrace();
        }
        return Encoded;
    }
