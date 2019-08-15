 try {
        String content = "hello";
        String key = "57f4dad48e7a4f7cd171c654226feb5a";
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128, new SecureRandom(key.getBytes("utf-8")));
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();


        SecretKeySpec key1 = new SecretKeySpec(enCodeFormat, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] byteContent = content.getBytes("utf-8");


        cipher.init(Cipher.ENCRYPT_MODE, key1,new IvParameterSpec("1234567812345678".getBytes("UTF-8")));

        byte[] result = cipher.doFinal(byteContent);
        System.out.println(new String(result).equals("QtzDsbCgmA9+XBVEsEm70w=="));

    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
        e.printStackTrace();
    }
