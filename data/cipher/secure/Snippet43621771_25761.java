                Cipher c = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");
            c.init(Cipher.DECRYPT_MODE, secretKey, params);

            FileInputStream fis = new FileInputStream("repositorios/login.cif");
            FileOutputStream fos = new FileOutputStream("repositorios/login2.txt");
            CipherOutputStream cos = new CipherOutputStream(fos, c);

            byte[] b = new byte[16]; 
            int i = fis.read(b);
            while (i != -1) {
                cos.write(b, 0, i);
                i = fis.read(b);
            }
            cos.close();
            fis.close();
