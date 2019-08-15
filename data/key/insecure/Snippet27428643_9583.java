            file = new File("/sdcard/test.txt");
            SecretKey key64 = new SecretKeySpec( new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 }, "Blowfish" );
            Cipher cipher = Cipher.getInstance( "Blowfish" );

            //Code to write your object to file

            cipher.init( Cipher.ENCRYPT_MODE, key64 );

            SealedObject sealedObject = new SealedObject( (Serializable) "TEST", cipher);
            CipherOutputStream cipherOutputStream = new CipherOutputStream( new BufferedOutputStream( new FileOutputStream( file ) ), cipher );
            ObjectOutputStream outputStream = new ObjectOutputStream( cipherOutputStream );
            outputStream.writeObject( sealedObject );
            outputStream.flush();
            outputStream.close();

            //now try to read it again

            CipherInputStream cipherInputStream = new CipherInputStream( new BufferedInputStream( new FileInputStream( file ) ), cipher );

            ObjectInputStream inputStream = new ObjectInputStream( cipherInputStream ); //<== this line crashes with StreamCorruptedExecution
