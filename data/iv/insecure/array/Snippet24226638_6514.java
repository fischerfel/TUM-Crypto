public void sendKey(SecretKey k) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec("i15646dont6321wanna".toCharArray(),"ahhalkdjfslk3205jlk3m4ljdfa85l".getBytes("UTF8"),65536,256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey key = new SecretKeySpec(tmp.getEncoded(),"AES");
            Cipher ec = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            ec.init(Cipher.ENCRYPT_MODE, key, ivspec);

            byte[] ekey = ec.doFinal(k.getEncoded());
            os.write(ekey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
