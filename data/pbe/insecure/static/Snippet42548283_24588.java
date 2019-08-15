try {
        String encryptKey = "abc123";
        byte[] salt = new byte[]{0x49, 0x76, 0x61, 0x6e, 0x20, 0x4d, 0x65, 0x64, 0x76, 0x65, 0x64, 0x65, 0x76};
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(encryptKey.toCharArray(), salt, 1024, 128);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        System.out.println("Key:" + Base64.encodeToString(secret.getEncoded(), Base64.DEFAULT));


        String cleartext = "12345";
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] ciphertext = cipher.doFinal(cleartext.getBytes("UTF-8"));
        System.out.println("IV:" + Base64.encodeToString(iv, Base64.DEFAULT));
        System.out.println("Cipher text:" + Base64.encodeToString(ciphertext, Base64.DEFAULT));;
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (InvalidKeySpecException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (InvalidParameterSpecException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
