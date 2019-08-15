public class md5 {
private static final Logger logger = Logger.getLogger(md5.class.getName());

public String md5gen(String a) throws NoSuchAlgorithmException
//public static void main(String[] args) throws NoSuchAlgorithmException, IOException
{
    //String a;
    /*BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


    System.out.println("Enter your Account number: ");
    a = br.readLine();

    */
    MessageDigest m= MessageDigest.getInstance("MD5");
    m.reset();
    m.update(a.getBytes());
    byte[] digest=m.digest();
    BigInteger bigInt = new BigInteger(1,digest);
    String hashtext = bigInt.toString(16);
    while(hashtext.length() < 32 ){
      hashtext = "0"+hashtext;
    }
    //System.out.println(hashtext);
    return hashtext;
}

public static String md5file(String file)
{

        String checksum = null;
        try {  
            checksum = DigestUtils.md5Hex(new FileInputStream(file));
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return checksum;
    }



}
