String secret = "LSC@SD2017@ps";
        byte[] input = new byte[0];
        String query = null;
        try {
            input = cipherText1.getBytes();

            byte[] cipherData = Base64.decode(cipherText1.getBytes(), Base64.NO_WRAP);
            byte[] saltData = Arrays.copyOfRange(cipherData, 8, 16);

            MessageDigest md5 = MessageDigest.getInstance("MD5");

            final byte[][] keyAndIV = GenerateKeyAndIV(32, 16, 1, saltData, secret.getBytes("UTF-8"), md5);


            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(secret.getBytes("UTF-8"));
            SecretKeySpec skc = new SecretKeySpec(thedigest, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skc);

            byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
            int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
            ctLength += cipher.doFinal(cipherText, ctLength);

             query = Base64.encodeToString(cipherText, Base64.NO_WRAP);

            System.out.println("NEw code\n" + query);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (ShortBufferException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
