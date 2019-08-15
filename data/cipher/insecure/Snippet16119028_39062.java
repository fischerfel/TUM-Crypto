public class Cli_dec 
{
    private static Socket s;
     private static int read;
    public static void main(String args[]) throws UnknownHostException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException
    {

        s=new Socket("127.0.0.1",2001);

        PrintWriter out=new PrintWriter(s.getOutputStream());

        String fname=JOptionPane.showInputDialog(null);

        out.write(fname+"\n");
        out.flush();

        int count;
        byte[] buf=new byte[100000];
        System.out.println("Receiving packets");
        File f=new File("F:/files/source_files//decryped.mp3");
        FileOutputStream to_b_decf=new FileOutputStream(f);
        BufferedOutputStream bos=new BufferedOutputStream(to_b_decf);
        InputStream in1=s.getInputStream();

        while((count=in1.read(buf))>0)
        {
        bos.write(buf, 0,count);
        bos.flush();
        }

        File destfile =new File("F:/files/source_files//original.mp3");

        if(!destfile.exists())
        destfile.createNewFile();

        Cipher decipher = Cipher.getInstance("AES");//initiate a cipher for decryption
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecretKey skey = kgen.generateKey();//initiate key
        decipher.init(Cipher.DECRYPT_MODE, skey);//decrypt the file 

        FileInputStream decf=new FileInputStream(f);

        System.out.println("decrypting");

        CipherInputStream c_decf=new CipherInputStream(decf,decipher);

        FileOutputStream destf=new FileOutputStream(destfile);

        CipherOutputStream cout=new CipherOutputStream(destf,decipher);


        while((read=c_decf.read())!=-1)
        {
         cout.write(read);
         cout.flush();
         }
        c_decf.close();
        destf.close();
        cout.close();
        decf.close();
        s.close();
}

}
