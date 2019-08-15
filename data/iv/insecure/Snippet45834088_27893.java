try
        {
            String encryptedText = "FN0hbSrVzkqhe+w2rQefAQ==";
            String vectorKey = "7EsBtzAJjMg=";
            //32 bit key
            String secretKey = "08061052989102040806105298910204";
            byte[] PinBytes = Base64.decodeBase64(encryptedText.getBytes("utf-8"));
            byte[] VectorBytes = Base64.decodeBase64(vectorKey.getBytes("utf-8"));
            byte[] SecretKeyBytes = Base64.decodeBase64(secretKey.getBytes("utf-8"));
            final MessageDigest md = MessageDigest.getInstance("md5");
            final byte[] digestOfPassword = md.digest(SecretKeyBytes);
            final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            for (int j = 0,  k = 16; j < 8;)
            {
                keyBytes[k++] = keyBytes[j++];
            }

            final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            final IvParameterSpec iv = new IvParameterSpec(VectorBytes);
            final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            decipher.init(Cipher.DECRYPT_MODE, key, iv);

            //final byte[] encData = new sun.misc.BASE64Decoder().decodeBuffer(message);
            final byte[] plainText = decipher.doFinal(PinBytes);

            System.out.println(plainText.toString());           
        }
        catch (java.security.InvalidAlgorithmParameterException e) { System.out.println("Invalid Algorithm"); }
        catch (javax.crypto.NoSuchPaddingException e) { System.out.println("No Such Padding"); }
        catch (java.security.NoSuchAlgorithmException e) { System.out.println("No Such Algorithm"); }
        catch (java.security.InvalidKeyException e) { System.out.println("Invalid Key"); }
        catch (BadPaddingException e) { System.out.println("Invalid Key");}
        catch (IllegalBlockSizeException e) { System.out.println("Invalid Key");}
        catch (UnsupportedEncodingException e) { System.out.println("Invalid Key");}     
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
