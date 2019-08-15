c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(
                    myIV.getBytes()));  

            byte[] decryptedBytes = c.doFinal(newbytearray ,0,newbytearray .length);

            System.out.println("decrypted string is"+new String(decryptedBytes));
