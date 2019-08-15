try {

            SecretKeySpec secretKeySpec = new SecretKeySpec(byteKey, "DES");



            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding"); //Demande l'utilisation de l'algorithme DES, en utilisant le mode ECB (Electronic CodeBook) et le style de padding PKCS-5.
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] myCipherText = cipher.doFinal(plainText);
            byte[] test = (new String(myCipherText)).getBytes();


             cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
             byte[] newPlainText = cipher.doFinal(test);
             System.out.println(new String(newPlainText));

        } catch (Exception e) {
            e.printStackTrace();
        }
