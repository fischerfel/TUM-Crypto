    public class Ser_enc 
    {
    private static int packet_count;
    private static int packet_size=1024;
    public static void main(String args[]) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException
    {
    System.out.println("Hi iam server");
    ServerSocket ss=new ServerSocket(2001);
    Socket s=ss.accept();

    BufferedReader in=new BufferedReader(new InputStreamReader(s.getInputStream()));//sockin

    OutputStream pw= s.getOutputStream();


    String filename=in.readLine();
    System.out.println("The file requested is " +filename);

    String loc="F://files//source_files//"+filename;

    File file=new File(loc);

    if(file.exists())
    System.out.println("File found");

    File to_b_encf =new File("F:/files/source_files//encryped.mp3");

    if(!to_b_encf.exists())
    to_b_encf.createNewFile();

    System.out.println("encrypting");

    Cipher encipher = Cipher.getInstance("AES");

    KeyGenerator kgen = KeyGenerator.getInstance("AES");

    SecretKey skey = kgen.generateKey();//initiate key

    encipher.init(Cipher.ENCRYPT_MODE, skey);

    FileInputStream fsrc=new FileInputStream(loc);

    FileOutputStream encfile=new FileOutputStream(to_b_encf);

    CipherInputStream cis = new CipherInputStream(fsrc, encipher);

    int read;
    while((read=cis.read())!=-1)
    {
      encfile.write(read);
      encfile.flush();
    }

     BufferedInputStream fsrcread=new BufferedInputStream(new   FileInputStream(to_b_encf));

    packet_count = (int) Math.ceil((to_b_encf.length()/packet_size));
    System.out.println("The number of packets to send is :" +packet_count);
    for(int i=0;i<=packet_count;i++)
    {
    byte[] packet=new byte[packet_size];

    fsrcread.read(packet, 0, packet_size);

    int per=(int)((i*100)/(packet_count));

    System.out.println("Transfer " +per +"% done");

    pw.write(packet);
    pw.flush();

    }
    s.close();
   pw.close();
   cis.close();
   encfile.close();
   }
   }
