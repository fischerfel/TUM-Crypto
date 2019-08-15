public class Crypto implements java.io.Serializable
{

public Crypto(String filename)
{

Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
SecretKeySpec secretkey = new SecretKeySpec(key(), "AES");
cipher.init(Cipher.ENCRYPT_MODE, secretkey);
CipherInputStream cipt = new CipherInputStream(new FileInputStream(new File(filename)), cipher)

ByteArrayOutputStream baos = new ByteArrayOutputStream();

  ObjectOutputStream obj = null;

           try
           {
                obj =   new ObjectOutputStream(baos);
                obj.writeObject(cipt);
                byte[] bv = baos.toByteArray();
                System.out.println(bv);

           }
           catch(Exception b)
           {
           b.printStackTrace();
           }
           finally
           {
            obj.close();
            baos.close();
           }
      }
     }
