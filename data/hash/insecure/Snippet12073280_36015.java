 public String digest( ZipInputStream entry ) throws IOException{

            byte[] digest = null;
            MessageDigest md5 = null;
            String mdEnc = "";
            ZipEntry current;

            try {
                md5 = MessageDigest.getInstance( "MD5" );
                if( entry != null ) {
                    while(( current = entry.getNextEntry() ) != null ) {
                        if( current.isDirectory() ) {
                            digest = this.encodeUTF8( current.getName() );
                            md5.update( digest );
                        }
                        else{
                            int size = ( int )current.getSize();
                            if(size > 0){
                                digest = new byte[ size ];
                                entry.read( digest, 0, size );
                                md5.update( digest );
                            }
                        }
                    }
                    digest = md5.digest();
                    mdEnc = new BigInteger( 1, md5.digest() ).toString( 16 );
                    entry.close();
                }
            }
            catch ( NoSuchAlgorithmException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IllegalArgumentException ex){
                System.out.println("There is an illegal encoding.");
                //
                // The fix for Korean/Chinese/Japanese encodings goes here
                //
                Charset encoding = Charset.forName("utf-8");
                ZipInputStream zipinputstream = 
                        new ZipInputStream(new FileInputStream( this.filename ), encoding);
                digest = new byte[ 1024 ];
                current = zipinputstream.getNextEntry();
                while (current != null) { //for each entry to be extracted
                    String entryName = current.getName();
                    System.out.println("Processing: " + entryName);
                    int n;
                    FileOutputStream fileoutputstream = 
                            new FileOutputStream( this.filename );

                    while (( n = zipinputstream.read( digest, 0, 1024 )) > -1) {
                        fileoutputstream.write(digest, 0, n);
                    }

                    fileoutputstream.close(); 
                    zipinputstream.closeEntry();
                    current = zipinputstream.getNextEntry();
                }//while
                zipinputstream.close();
            }
            return mdEnc;
        }

        public byte[] encodeUTF8( String name ) {
            final Charset UTF8_CHARSET = Charset.forName( "UTF-8" );
            return name.getBytes( UTF8_CHARSET );
        }
