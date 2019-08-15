byte[] input = "6KzVyH0s3RyliBBAkcIrKOmripAUrDgBrm3VZR0VKkDlBTsHdOm/ONN1st/PBhquynxOjIPvgTfXJKx3aYDaARvCBmJwm1a0kfqFUcdpho+QSqpnqlDaBelL3taPKy2XPNmbOWlYUQtircPwcTrbOEUrQeUjEKqtataHw1tIp5c=".getBytes();


            String key1="12345891456";

           // byte[] keyBytes = new byte[] { 1234567891123456 };

            byte[] keyBytes = null;
            try {
                keyBytes = key1.getBytes("UTF-16LE");               

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

            //key.init(128);

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

            System.out.println(new String(input));

            // encryption pass
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] cipherText = new byte[cipher.getOutputSize(input.length)];           
            int ctLength = cipher.update(input, 0, input.length, cipherText, 0);       

            // decryption pass
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] plainText = new byte[cipher.getOutputSize(ctLength)];

            int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);

            ptLength += cipher.doFinal(plainText, ptLength);
            System.out.println(new String(plainText));
            System.out.println(ptLength);
          }
