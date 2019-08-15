        private String charset = "ABCDEFGHJKLMNPQRSTUVWXYZ123456789";

               private char[] charArray;

              // Random generator = new Random();

               private byte[] passwd;

                public String Generate(String password)
                {

                        passwd = toByteArray(password);


                      charArray = strToChar(charset);

                        byte[] data = new byte[15];

                         //generator.nextBytes(data);

                        byte[] tohash = new byte[5+ passwd.length];

                      //System.arraycopy(data, 0, tohash, 0, 5);

                      System.arraycopy(passwd, 0, tohash, 5, passwd.length);

                        try {

                              byte[] hash = getHash(tohash);

                              System.arraycopy(hash, 0, data, 5, 10);

                        } catch (NoSuchAlgorithmException e) {

                                e.printStackTrace();

                        } catch (UnsupportedEncodingException e) {

                                e.printStackTrace();

                        }

                        int num=0;//17

                        for (int i = 0; i < tohash.length; i++) num += tohash[i];

                        String serial = Encode(data) + charArray[num & 31];

                        String ret = "";

                        for (int i = 0; i < 5; i++)

                        {

                                ret += serial.substring((i*5),(i*5)+5);

                                if (i < 4) ret += "-";

                        }

                        return ret;


            }

                private String Encode(byte[] data){

                        String ret="" ;

                        for (int i = 0; i < data.length; i += 5){

                                ret += charArray[data[i] >> 3 & 0x1f];

                                ret += charArray[(data[i] << 2 | data[i + 1] >> 6) & 0x1f];

                                ret += charArray[(data[i + 1] >> 1) & 0x1f];

                                ret += charArray[(data[i + 1] << 4 | data[i + 2] >> 4) & 0x1f];

                                ret += charArray[(data[i + 2] << 1 | data[i + 3] >> 7) & 0x1f];

                                ret += charArray[data[i + 3] >> 2 & 0x1f];

                                ret += charArray[(data[i + 3] << 3 | data[i + 4] >> 5) & 0x1f];

                                ret += charArray[data[i + 4] & 0x1f];
                                //
                                System.out.println(ret);

                        }

                        return ret;

                }

               private byte[] Decode(String serial){

                        char[] x = strToChar(serial);

                        byte[] table = new byte[256];

                        for (int i = 0; i < charArray.length; i++)

                        {

                                table[charArray[i]] = (byte)i;

                        }

                        byte[] ret = new byte[x.length * 5 / 8];

                        int pos = 0;

                        for (int i = 0; i <= x.length - 8; )

                        {

                                byte b1 = table[x[i++]];

                                byte b2 = table[x[i++]];

                                byte b3 = table[x[i++]];

                                byte b4 = table[x[i++]];

                                byte b5 = table[x[i++]];

                                byte b6 = table[x[i++]];

                                byte b7 = table[x[i++]];

                                byte b8 = table[x[i++]];



                                ret[pos++] = (byte)(b1 << 3 | b2 >> 2);

                                ret[pos++] = (byte)(b2 << 6 | b3 << 1 | b4 >> 4);

                                ret[pos++] = (byte)(b4 << 4 | b5 >> 1);

                                ret[pos++] = (byte)(b5 << 7 | b6 << 2 | b7 >> 3);

                                ret[pos++] = (byte)(b7 << 5 | b8);


                        }

                        return ret;

                }



        public int byteArrayToInt(byte[] b, int offset) {

        int value = 0;
        for (int i = 0; i < 4; i++) {

            int shift = (4 - 1 - i) * 8;

            value += (b[i + offset] & 0x000000FF) << shift;


        }

        return value;

    }

        private char[] strToChar(String str){

                char[] c = str.toCharArray();

                return c;

        }

        public byte[] toByteArray(String p) {

                String stringToConvert = p;

                byte[] theByteArray = stringToConvert.getBytes();

                return theByteArray;

        }

       public byte[] getHash(byte[] toHash) throws NoSuchAlgorithmException, UnsupportedEncodingException {

                MessageDigest digest = MessageDigest.getInstance("SHA-256");

                digest.reset();

                return digest.digest(toHash);

        }
