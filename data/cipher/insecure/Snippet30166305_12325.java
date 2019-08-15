 byte[] iv1=new byte[]{(byte)0x00 , (byte)0x00 , (byte)0x00 , (byte)0x00 ,
                        (byte)0x00 , (byte)0x00 , (byte)0x00 , (byte)0x00 };
                byte[] newPICCKeyEnciphered = new byte[24];
                //..............................
                byte[] block1 = new byte[]{(byte)0x11, (byte)0x22, (byte)0x33, (byte)0x44, 
                        (byte)0x55, (byte)0x66, (byte)0x77 ,(byte)0x88};
                byte[] block2 = new byte[]{
                (byte)0x11, (byte)0x22, (byte)0x33 ,(byte)0x44 ,
                (byte)0x55, (byte)0x66, (byte)0x77 ,(byte)0x88};
                byte[] block3 = new byte[]{(byte)0x00 , (byte)0x00,
                        (byte)0x00 ,(byte)0x00 ,(byte)0x00 ,
                        (byte)0x00 ,(byte)0x00 ,(byte)0x00};
                block3[0] = newPICCKey_deciphered[16];
                block3[1] = newPICCKey_deciphered[17];

   try
                {
                    Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
                SecretKeyFactory desKeyFact = SecretKeyFactory.getInstance("DES");
                DESKeySpec desKeySpec = new DESKeySpec(sessionKey);  
                SecretKey s = desKeyFact.generateSecret(desKeySpec);
                cipher.init(Cipher.DECRYPT_MODE, s);

                byte[] r1 = new byte[8];
                r1 =Utils.doXorTwoByteArray(block1, iv1);

                byte[] r2 = new byte[8];
                r2 = cipher.doFinal(r1, 0, 8);
                //...............
                byte[] r3 = new byte[8];
                r3 =Utils.doXorTwoByteArray(block2, r2);

                byte[] r4 = new byte[8];
                r4 =cipher.doFinal(r3, 0, 8);
                //................
                byte[] r5 = new byte[8];
                r5 =Utils.doXorTwoByteArray(block3, r4);

                byte[] r6 = new byte[8];
                r6 =cipher.doFinal(r5, 0, 8);
                for(int i=0; i<8;i++)
                    newPICCKeyEnciphered[i] = r2[i];
                for(int i=8; i<16;i++)
                    newPICCKeyEnciphered[i] = r4[i-8];
                for(int i=16; i<24;i++)
                    newPICCKeyEnciphered[i] = r6[i-16];
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
