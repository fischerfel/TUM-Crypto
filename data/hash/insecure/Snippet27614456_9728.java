     try {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    byte[] array = md.digest(bytes);
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < array.length; ++i) {
                      sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
                   }
                    System.out.println( "0X"+sb.toString());
OUTPUT:0X95e5f0b21398asdas03e832172f70ce7dc7
                   String x= "0x"+sb.toString();
                    System.out.println(x.getBytes() );
OUTPUT:[B@1174b07
                    System.out.println("uno shriram is "+Arrays.toString(x.getBytes()));
OUTPUT:uno shriram is [48, 120, 57, 53, 101, 53, 102, 48, 98, 54, 57, 56, 56, 101, 99, 55, 48, 51, 101, 56, 51, 50, 49, 55, 50, 102, 55, 48, 99, 101, 55, 100, 99, 55]
                    try
                    {
                    byte[] bytesasd = x.getBytes("UTF-8");
                    MessageDigest m = MessageDigest.getInstance("MD5");

                    byte[] hashedbyte = m.digest(bytesasd);

                    System.out.println(hashedbyte);
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                } catch (java.security.NoSuchAlgorithmException e) {
                }
            try 
            {
                String doc2 = new String(bytes, "UTF-8");
                System.out.println(""+doc2);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
