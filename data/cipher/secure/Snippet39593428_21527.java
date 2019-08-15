private byte[] decrypt_chunk(byte[] data, ByteString chunk_encryption_key) {
        SecretKeySpec skeySpec = new SecretKeySpec(chunk_encryption_key.toByteArray(), 1, 16, "AES");
        Cipher cipher;
        byte[] decrypted = new byte[0];
        try {
            cipher = Cipher.getInstance("AES/CFB/NoPadding");

            cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(getIV(0)));

            decrypted = cipher.doFinal(data);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }  catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (ShortBufferException e) {
            e.printStackTrace();
        }

        return decrypted;
    }
