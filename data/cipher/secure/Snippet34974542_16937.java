        try
        {
            byte[] pack = new byte[4];
            byte[] pack2;
            byte[] pack3;
            pack[0] = 1;
            pack[1] = 2;
            pack[2] = 3;
            pack[3] = 4;

            System.out.println("START!");
            for(int i = 0; i < 4; i++)
            {
                System.out.println(pack[i]);
            }

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(512);
            KeyPair keyPair = keyPairGenerator.genKeyPair();

            Cipher cip = Cipher.getInstance("RSA");
            cip.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
            pack2 = cip.doFinal(pack, 1, pack.length-1);
            for(int i = 0; i < 4; i++)
            {
                System.out.println(pack2[i]);
            }

            Cipher cip2 = Cipher.getInstance("RSA");
            cip2.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            pack3 = cip2.doFinal(pack2, 1, pack2.length-1);
            for(int i = 0; i < 4; i++)
            {
                System.out.println(pack3[i]);
            }

            System.out.println("END!");

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
