        public static  byte[] encrypt(byte[] datasource, String password) {            
            try{
                    SecureRandom random = new SecureRandom();
                    DESKeySpec desKey = new DESKeySpec(password.getBytes());
                    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                    SecretKey securekey = keyFactory.generateSecret(desKey);
                    Cipher cipher = Cipher.getInstance("DES");
                    cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
                    return cipher.doFinal(datasource);
            }catch(Throwable e){
                e.printStackTrace();
            }
            return null;
        }

        private static void longToByteArray(long l, byte[] b) {
            b[7] = (byte) (l);
            l >>>= 8;
            b[6] = (byte) (l);
            l >>>= 8;
            b[5] = (byte) (l);
            l >>>= 8;
            b[4] = (byte) (l);
            l >>>= 8;
            b[3] = (byte) (l);
            l >>>= 8;
            b[2] = (byte) (l);
            l >>>= 8;
            b[1] = (byte) (l);
            l >>>= 8;
            b[0] = (byte) (l);
        }



        long aliveTime = Long.parseLong("13664547854160806");
        byte[] longAsBytes = new byte[8];
        longToByteArray(aliveTime, longAsBytes);
        byte[] result = DES.encrypt(longAsBytes, password);
        String en = REncrypt.base64Encode(result);
