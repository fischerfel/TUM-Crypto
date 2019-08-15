public class Hash 
{
    static MessageDigest sha1;
    private static final int unitLength = 160; // SHA-1 has 160-bit output.


    public static void main(String[] args)throws Exception
    {

        String s  = new String("hello");

        BigInteger b =new BigInteger(s.getBytes()); // Big integer conversion

        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");    
        sha1.reset();
        sha1.update(s.getBytes());
        byte[] hc = sha1.digest();  
        BigInteger hcB=new BigInteger(1,hc);    


        KeyGenerator keyRC=new KeyGenerator();  
        try {
            keyRC.initialize();//generating key 


           BigInteger HashClValueExp=hcB.modPow(keyRC.RC, keyRC.p);// exponentiate of hashed value
           System.out.println("HasheCldExp Value: "+HashClValueExp);

           //Inverse RC         
           BigInteger inv = keyRC.RC.modInverse(keyRC.q);
           System.out.println("Inverse RC: " + inv);

          // Hash value inverse computation 
           BigInteger hci = HashClValueExp.modPow(inv, keyRC.p);
           System.out.println("hci: " + hci); // prints in hex
           System.out.println("hcB: " + hcB);   
           System.out.println("Compare hci and hcB :" + hci.compareTo(hcB));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
