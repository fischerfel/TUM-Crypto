public class HMACSHA1 {
static byte[] password;
static byte[] fileName = null;
static byte[] input = new byte[16];
static String File;
public static void main(String[] args) {
    Security.addProvider(Security.getProvider("BC")); 
    if(args.length != 2){
        System.out.println("Invalid input");
        System.exit(-1);
    }
    File = args[1];
    try {
        password = args[0].getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        System.out.println("Unable to read password");
        System.exit(-1);
    }
    InputStream inputstream = null;
    try {
             inputstream = new FileInputStream(File);
    } catch (FileNotFoundException e2) {
        e2.printStackTrace();
        System.out.println("No input found\n");
        System.exit(-1);
    }

    MessageDigest hash = null;
    MessageDigest key = null;
    try {
        hash = MessageDigest.getInstance("SHA-1", "BC");
        key = MessageDigest.getInstance("SHA-1", "BC");
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchProviderException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    key.update(password);
    byte[] HMACKey = key.digest();
    byte[] digest = null;
    int reader = 0;
    while (reader != -1){
        try {
            reader = inputstream.read(input);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        hash.update(input);
        digest = hash.digest();
        System.out.println(new String(Hex.encode(digest)));
    }

}
}
