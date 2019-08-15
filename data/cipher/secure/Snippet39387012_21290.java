    Cipher cipher = null;
    try {

        byte[] input = Utils.hexStringToByteArray("d9313225f88406e5a55909c5aff5269a");
        byte[] keyByte = Utils.hexStringToByteArray("cfa2b0719afe65b60b1461cdc6a7f7e3");

        SecretKeySpec key = new SecretKeySpec(keyByte, "AES");
        cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(32, Utils.hexStringToByteArray("000000000000000000000000"));
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        /*byte[] aad = Utils.hexStringToByteArray("000000");
        cipher.updateAAD(aad);*/

        final byte[] encrypted = new byte[cipher.getOutputSize(0)];
        cipher.update(input, 0, input.length, encrypted, 0);  //Not being updated for current data.

        //Tag output
        byte[] tag = new byte[cipher.getOutputSize(0)];

        cipher.doFinal(tag, 0);

        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        //cipher.updateAAD(aad);
        final byte[] data1 = new byte[16];

        int offset = cipher.update(encrypted, 0, encrypted.length, data1, 0);

        cipher.update(tag, 0, tag.length, data1, offset);
        cipher.doFinal(data1,offset);


    }
    catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();

    } catch (BadPaddingException e) {
        e.printStackTrace();

    } catch (InvalidKeyException e) {
        e.printStackTrace();

    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();

    } catch (InvalidAlgorithmParameterException e) {
        e.printStackTrace();
    } catch (ShortBufferException e) {
        e.printStackTrace();
    }

} 
