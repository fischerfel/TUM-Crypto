public class A{

    long set_time,ext_time,ident_time;
    long set_time2,ext_time2,ident_time2;
    BigInteger p,q,phiN,e,d,modu;
    KeyPairGenerator keygen;
    KeyPair keypair;
    RSAPublicKey publicKey;
    RSAPrivateCrtKey privateKey;
    MessageDigest H,G;
    String TM;

    public void Setup(int k)
    {
         SecureRandom random = new SecureRandom();
         try
         {
            set_time2 = System.nanoTime();

            keygen = KeyPairGenerator.getInstance("RSA");
            keygen.initialize(k, random);
            keypair = keygen.genKeyPair();
            privateKey = (RSAPrivateCrtKey)keypair.getPrivate();
            publicKey = (RSAPublicKey)keypair.getPublic();

            p = privateKey.getPrimeP();
            q = privateKey.getPrimeQ();
            phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
            e = privateKey.getPublicExponent();
            modu = privateKey.getModulus();
            d = e.modInverse(phiN);

            set_time = System.nanoTime() - set_time2;  

         }
         catch(Exception e){
            System.out.println("\nError in Setup: " + e.getMessage());
        }

    }
    public BigInteger getModu()
    {
        System.out.println(modu);
        return modu;
    }


    public static void main(String[] args)throws Exception {
       A callpkg = new A();
       callpkg.Setup(2048);     
       callpkg.getModu();
       B callchild = new B();
       callchild.KeyDer();
    }
}

public class B extends A{

    long set_time,ext_time,ident_time;
    long set_time2,ext_time2,ident_time2;
    BigInteger id_hash,N,x,t,bigT,c,s;
    MessageDigest H,G;
    String TM;
    String ID = "email", M="Hello world";

    public void KeyDer()
    {
         try
         {                        
                SHIBS abc = new Function();
                ext_time2 = System.nanoTime();

                H = MessageDigest.getInstance("SHA-512");
                H.update(ID.getBytes());
                id_hash = new BigInteger(H.digest());
                A call = new A();
                BigInteger aaa = call.getModu();
                System.out.println(aaa);
                N = privateKey.getModulus();
                x = id_hash.modPow(d, N);

                ext_time = System.nanoTime() - ext_time2;
         }
         catch(Exception e){
            System.out.println("\nError in KeyDer: " + e.getMessage());
        }
    }
}
