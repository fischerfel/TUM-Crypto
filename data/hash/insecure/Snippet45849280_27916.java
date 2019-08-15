public class ZooLogin extends ParseFile {

private String userName;

private String[] passWord;
private MessageDigest md;
private String original;
private static boolean  isTrue;
Scanner sc = new Scanner(System.in);

public static void main(String[] args) {


    boolean authenticateMe;
    ZooLogin log1 = new ZooLogin();
    log1.loginAuthentication(isTrue);



}

private boolean loginAuthentication(boolean isTrue) {
    //Begin login prompt
    System.out.println("Username: ");
    userName = sc.nextLine();
    //Hash password
    System.out.println("Password: ");
    original = sc.nextLine();
    getMD5Hash(original);
    ParseFile login = new ParseFile();

    int i = 1;

    while (i <= 3) {
        if (login.equals(original)) {                
           isTrue = true;
        }
        else {
            i++;
        }      

}

    return isTrue;
}

public String getMD5Hash(String[] original) {

    try {
        md = MessageDigest.getInstance("MD5");
    }   catch (NoSuchAlgorithmException ex) { 
   }
    md.update(original.getBytes());
    byte[] digest = md.digest();
    StringBuffer sb = new StringBuffer();
    for (byte b : digest) {
        sb.append(String.format("%02x", b & 0xff));
    }

    return sb.toString();
}
