try {
                {
                    String password = "test123";
                    MessageDigest digest = MessageDigest.getInstance( "MD5" ); 
                    byte[] passwordBytes = password.getBytes( ); 

                    digest.reset( );
                    digest.update( passwordBytes );
                    digest.update( passwordBytes );
                    byte[] message = digest.digest( );

                    StringBuffer hexString = new StringBuffer();
                    for ( int i=0; i < message.length; i++) 
                    {
                        hexString.append( Integer.toHexString(
                            0xFF & message[ i ] ) );
                    }
                    String encrypted = hexString.toString();
                    System.out.println(encrypted);
                  } } catch (NoSuchAlgorithmException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
