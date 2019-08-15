public class Storage 
{
    private final byte[] salt = {
            ( byte )0xf5, ( byte )0x33, ( byte )0x01, ( byte )0x2a,
            ( byte )0xb2, ( byte )0xcc, ( byte )0xe4, ( byte )0x7f
            };
        private int iterationCount = 100;

        public void encryptAndWriteToFile(String host,String user,String pw,boolean flag)
        {
            String usr=host+","+user+","+pw+","+Boolean.toString(flag);
            Cipher cipher = null;
            try 
            {
                PBEKeySpec keySpec =new PBEKeySpec(usr.toCharArray());
                SecretKeyFactory keyFactory =SecretKeyFactory.getInstance( "PBEWithMD5AndDES" );
                SecretKey secretKey = keyFactory.generateSecret( keySpec );
                PBEParameterSpec parameterSpec =new PBEParameterSpec( salt, iterationCount );
                cipher = Cipher.getInstance( "PBEWithMD5AndDES" );
                cipher.init( Cipher.ENCRYPT_MODE, secretKey,parameterSpec );
            }catch ( NoSuchAlgorithmException exception )
            {
                exception.printStackTrace();
                System.exit( 1 );
            }catch ( InvalidKeySpecException exception )
            {
                exception.printStackTrace();
                System.exit( 1 );
            }catch ( InvalidKeyException exception )
            {
                exception.printStackTrace();
                System.exit( 1 );
            }catch ( NoSuchPaddingException exception )
            {
                exception.printStackTrace();
                System.exit( 1 );
            }catch ( InvalidAlgorithmParameterException exception )
            {
                exception.printStackTrace();
                System.exit( 1 );
            }
            byte[] outputArray = null;
            try 
            {
                outputArray = usr.getBytes( "ISO-8859-1" );
            }catch (UnsupportedEncodingException ex) 
            {
                        Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
            }
            File sdCard=Environment.getExternalStorageDirectory();//+"/userData.txt";
            String user_credentails=sdCard.getName()+"/userData.txt";
            File file = new File(user_credentails);
            Log.d(user_credentails, "is input file ");
            FileOutputStream fileOutputStream = null;
            try
            {
                fileOutputStream = new FileOutputStream( file );
            }catch ( IOException exception )
            {
                exception.printStackTrace();
                System.exit( 1 );
            }
            CipherOutputStream out =new CipherOutputStream( fileOutputStream, cipher );
            try
            {
                out.write( outputArray );
                out.flush();
                out.close();
            }catch ( IOException exception )
            {
                exception.printStackTrace();
                System.exit( 1 );
            }
            Vector fileBytes = new Vector();
            try 
            {
                FileInputStream in = new FileInputStream( file );
                byte contents;
                while ( in.available() > 0 ) {
                contents = ( byte )in.read();
                fileBytes.add( new Byte( contents ) );
                }
                in.close();
            }catch ( IOException exception )
            {
                exception.printStackTrace();
                System.exit( 1 );
            }
            byte[] encryptedText = new byte[ fileBytes.size() ];
            for ( int i = 0; i < fileBytes.size(); i++ ) 
            {
                encryptedText[ i ] =
                ( ( Byte ) fileBytes.elementAt( i ) ).byteValue();
            }
        }
        public String[] readFromFileAndDecrypt(boolean state)
        {
            Vector fileBytes = new Vector();
            String pw = "123";
            String fileName = "security.txt";
            String usr="10.0.2.2"+","+"haider"+","+pw+","+Boolean.toString(state);
            Cipher cipher = null;
            try 
            {
                PBEKeySpec keySpec =new PBEKeySpec(usr.toCharArray());
                SecretKeyFactory keyFactory =SecretKeyFactory.getInstance( "PBEWithMD5AndDES" );
                SecretKey secretKey = keyFactory.generateSecret( keySpec );
                PBEParameterSpec parameterSpec =new PBEParameterSpec( salt, iterationCount );
                cipher = Cipher.getInstance( "PBEWithMD5AndDES" );

                cipher.init( Cipher.DECRYPT_MODE, secretKey,parameterSpec );
            }catch ( NoSuchAlgorithmException exception ) 
            {
                exception.printStackTrace();
                System.exit( 1 );
            }catch ( InvalidKeySpecException exception ) 
            {
                exception.printStackTrace();
                System.exit( 1 );
            }catch ( InvalidKeyException exception )
            {
                exception.printStackTrace();
                System.exit( 1 );
            }catch ( NoSuchPaddingException exception )
            {
                exception.printStackTrace();
                System.exit( 1 );
            }catch ( InvalidAlgorithmParameterException exception )
            {
                exception.printStackTrace();
                System.exit( 1 );
            }
            try 
            {
                File sdCard=Environment.getExternalStorageDirectory();
                String user_credentails=sdCard.getAbsolutePath()+"/userData.txt";
                Log.d(user_credentails, " is file name and path");
                File file = new File(user_credentails);
                FileInputStream fileInputStream =new FileInputStream( file );
                CipherInputStream in =new CipherInputStream( fileInputStream, cipher );
                byte contents = ( byte ) in.read();
                Log.d(Byte.toString(contents),"  is line");//.out.println("\n"+contents+"\n");
                while ( contents != -1 ) 
                {
                    fileBytes.add( new Byte( contents ) );
                    contents = ( byte ) in.read();
                }
                in.close();
            }catch ( IOException exception ) 
            {
                exception.printStackTrace();
                System.exit( 1 );
            }
            byte[] decryptedText = new byte[ fileBytes.size() ];
            for( int i = 0; i < fileBytes.size(); i++ ) 
            {
                 decryptedText[ i ] =( ( Byte )fileBytes.elementAt( i ) ).byteValue();
            }
            Log.d(new String(decryptedText)," is data");
            String uUser=new String( decryptedText );
            String[] delims=uUser.split(",");
            return delims;
        }

}
