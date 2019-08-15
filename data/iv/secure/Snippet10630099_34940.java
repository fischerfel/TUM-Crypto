c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(
                    myIV.getBytes()));  

            byte[] decryptedBytes = c.doFinal(encrypted ,0,encrypted .length);

            System.out.println("decrypted string is"+new String(decryptedBytes));
