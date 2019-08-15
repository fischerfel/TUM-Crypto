public class Main 
{
    public static void main(String[] argv)
    {
        try
        {
            writeStringAsBytes();
            readStringAsBytes();
        }
        catch(Exception error)
        {
            System.out.println(error.toString());
        }
    }

    public static void readStringAsBytes() throws Exception
    {
        File file = new File("pathname");

        FileInputStream fis=new FileInputStream("pathname");

        byte[] by=new byte[(int)file.length()];
        int i;
        fis.read(by);
        fis.close();

        Encrypter encrypter = new Encrypter();

        System.out.println("decoded:\n" + encrypter.decrypt(by));
    }


    public static void writeStringAsBytes() throws Exception
    {
        String toEncrypt = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\nSed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur.";

        Encrypter encrypter = new Encrypter();

        byte[] encryptedByteArray = encrypter.encrypt(toEncrypt);
        System.out.println("encryptedByteArray:" + encryptedByteArray);

        FileOutputStream fos = new FileOutputStream(new File("pathname"));
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write(encryptedByteArray);
        bos.close();
    }
}

class Encrypter
{
    String alg = "AES";
    Key key;
    Cipher cipher;

    public Encrypter() throws Exception
    {
         key = KeyGenerator.getInstance(alg).generateKey();
         cipher = Cipher.getInstance(alg);
    }

    public byte[] encrypt(String str) throws Exception
    {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] inBytes = str.getBytes("UTF-16");

        return cipher.doFinal(inBytes);
    }

    public String decrypt(byte[] enBytes) throws Exception
    {
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] bytes = cipher.doFinal(enBytes);
        String rData = new String(bytes, "UTF-16");

        return rData;
    }
}
