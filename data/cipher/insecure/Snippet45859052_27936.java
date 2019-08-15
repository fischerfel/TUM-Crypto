try
        {
            byte[] PinBytes = Base64.decodeBase64(encryptedText);
            byte[] VectorBytes = Base64.decodeBase64(vectorKey);
            byte[] SecretKeyBytes = Base64.decodeBase64(secretKey);
         // initialize the vector with the one you receive               
            IvParameterSpec spec = new IvParameterSpec(VectorBytes);

            // create the key. DESede should be correct, but if it doesn't work try also with DES
            Key key = new SecretKeySpec(SecretKeyBytes, "DESede");

            // Initialize the cipher
            Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");

            // decrypt the string
            c.init(Cipher.DECRYPT_MODE, key, spec);
            byte[] decodedDecryptedBytes = c.doFinal(PinBytes);
            return new String(decodedDecryptedBytes, "UTF-8");      
        }
        catch (java.security.InvalidAlgorithmParameterException e) { System.out.println("Invalid Algorithm"); }
        catch (javax.crypto.NoSuchPaddingException e) { System.out.println("No Such Padding"); }
        catch (java.security.NoSuchAlgorithmException e) { System.out.println("No Such Algorithm"); }
        catch (java.security.InvalidKeyException e) { System.out.println("InvalidKeyException : Invalid Key"); }
        catch (BadPaddingException e) { System.out.println("BadPaddingException : Invalid Key");}
        catch (IllegalBlockSizeException e) { System.out.println("IllegalBlockSizeException : Invalid Key");}
        catch (UnsupportedEncodingException e) { System.out.println("UnsupportedEncodingException : Invalid Key");}     
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
