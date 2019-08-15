tatic byte[] salt = new byte[16];
public static String password = "peachy";
public static String newpassword = "peachy";

public static byte []storedpassword;

public static void main(String[] args) throws Exception {

    generateSalt();
    System.out.println("salt1:"+salt.toString());
    storedpassword=hash(password,salt);
    System.out.println(storedpassword.toString());
    boolean answer = check(newpassword, storedpassword);
    System.out.println(answer);


}
public static void generateSalt()
{
     Random randomno = new Random();
     randomno.nextBytes(salt);

}

private static byte[] hash(String password, byte[] salt) throws Exception   
{  
    SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);

    return f.generateSecret(spec).getEncoded();

}
public static boolean check(String givenPassword, byte[] storedPassword)
   throws Exception{
        System.out.println("salt2:"+salt.toString());
        byte[] hashOfInput = hash(givenPassword,salt);
        System.out.println(hashOfInput.toString());
        return hashOfInput.equals(storedPassword);
   }

}
