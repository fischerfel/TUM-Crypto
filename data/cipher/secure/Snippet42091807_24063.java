public static boolean writeObj(OutputStream out,IntIODetail o) throws IOException{
      synchronized(out){

           try(ObjectOutputStream objOut=new ObjectOutputStream(out)){
                objOut.writeObject(o);
           }catch(InvalidClassException | NotSerializableException e){
                return false; 
           }
           return true;
      }
 }
public static IntIODetail readObj(InputStream in) throws IOException{
      synchronized(in){

           try(ObjectInputStream objIn=new ObjectInputStream(in)){
                IntIODetail ob=(IntIODetail)objIn.readObject();
                return ob;
           } catch(ClassNotFoundException | InvalidClassException 
                     | StreamCorruptedException | OptionalDataException e) {
                return null;
           }
      }
 }
public static boolean writeCompObj(OutputStream out,IntIODetail o)
           throws IOException{

      try {        
           byte[] keyBytes = "1234123412341234".getBytes();  //example
           final byte[] ivBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 
                0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f }; //example

           final SecretKey key = new SecretKeySpec(keyBytes, "AES");
           final IvParameterSpec IV = new IvParameterSpec(ivBytes);
           final Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
           cipher.init(Cipher.ENCRYPT_MODE, key, IV);
           try(CipherOutputStream cstream = new CipherOutputStream(out, cipher)){
                return writeObj(cstream,o);
           }
      } catch(Exception ex) {
           return false;
      }
 }
public static IntIODetail readCompObj(InputStream in)
           throws IOException{
      try {
           byte[] keyBytes = "1234123412341234".getBytes();
           final byte[] ivBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
                       0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };

           final SecretKey secretkey = new SecretKeySpec(keyBytes, "AES");
           final IvParameterSpec IV = new IvParameterSpec(ivBytes);
           final Cipher decipher = Cipher.getInstance("AES/CBC/NoPadding");
                decipher.init(Cipher.DECRYPT_MODE, secretkey, IV);
           try(CipherInputStream cin=new CipherInputStream(in,decipher)){
                return readObj(cin);
           }
      } catch(InvalidKeyException | InvalidAlgorithmParameterException
                | NoSuchPaddingException |NoSuchAlgorithmException ex) {
           return null;
      }
 }
