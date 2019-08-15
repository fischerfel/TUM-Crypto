         public class Safety {
public static final String algorithm = "PBKDF2WithHmacSHA1";
public static final int saltbytesize = 24;
public static final int hashbytesize = 24;
public static final int iterations = 1000;
public static final int iIndex = 0;
public static final int sIndex = 1;
public static final int pbkIndex = 2;
    public static Users passwordHash(Users user) throws NoSuchAlgorithmException, InvalidKeySpecException{
        SecureRandom sR=new SecureRandom();
        byte[] pws=new byte[saltbytesize];
        sR.nextBytes(pws);
        byte[] pwh=pbkdf2(user.getPassword().toCharArray(),pws,iterations,hashbytesize);
        user.setPassword(toHex(pwh));
        byte[] sas=new byte[saltbytesize];
        sR.nextBytes(sas);
        byte[] sah=pbkdf2(user.getsA().toCharArray(),sas,iterations,hashbytesize);
        user.setsA(toHex(sah));
        user.setUserhash(pws);
        user.setSahash(sas);
        return user;
    }

    public static boolean hashpassword(String username,String password,Users user) throws NoSuchAlgorithmException, InvalidKeySpecException{
        byte[] pws=user.getUserhash();
        byte[] pwh=pbkdf2(password.toCharArray(),pws,iterations,hashbytesize);
        String searcher=toHex(pwh)+username;
        String searched=user.getPassword()+user.getUsername();
        if(searcher.equals(searched)){
            return true;
        }
        return false;
     }
    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException
        {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
            return skf.generateSecret(spec).getEncoded();
        }
    private static String toHex(byte[] array)
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }



     }
