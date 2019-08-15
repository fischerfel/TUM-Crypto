String fileName = "result.dat"; //some result file

//You may use any combination, but you should use the same for writing and reading
SecretKey key64 = new SecretKeySpec( new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 }, "Blowfish" );
Cipher cipher = Cipher.getInstance( "Blowfish" );

//Code to write your object to file
cipher.init( Cipher.ENCRYPT_MODE, key64 );
Person person = new Person(); //some object to serialise
SealedObject sealedObject = new SealedObject( person, cipher);
CipherOutputStream cipherOutputStream = new CipherOutputStream( new BufferedOutputStream( new FileOutputStream( fileName ) ), cipher );
ObjectOutputStream outputStream = new ObjectOutputStream( cipherOutputStream );
outputStream.writeObject( sealedObject );
outputStream.close();

//Code to read your object from file
cipher.init( Cipher.DECRYPT_MODE, key64 );
CipherInputStream cipherInputStream = new CipherInputStream( new BufferedInputStream( new FileInputStream( fileName ) ), cipher );
ObjectInputStream inputStream = new ObjectInputStream( cipherInputStream );
SealedObject sealedObject = (SealedObject) inputStream.readObject();
Person person1 = (Person) sealedObject.getObject( cipher );
