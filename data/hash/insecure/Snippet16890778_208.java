package ecdsa.draft;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class ECDSADraft {

public static void main(String[] args) throws Exception {

ECDSA a=new ECDSA();
a.key_Gen();
a.signature_gen();
a.signature_veri();
//Point p=new Point(new BigInteger("16"),new BigInteger("5"));
//Point q=new Point(new BigInteger("9"),new BigInteger("7"));
//Point r=p.scalarMul(new BigInteger("9"));
//r.showPoint();
    //BigInteger k=new BigInteger(160, random);


        // TODO code application logic here
    }

}

class ECDSA
{
 private BigInteger order_n = new BigInteger("6277101735386680763835789423176059013767194773182842284081");
 public Point public_key=new Point();
 public BigInteger private_key;
 public BigInteger r=null;
 public BigInteger s=null;
 public  BigInteger basex = new BigInteger("602046282375688656758213480587526111916698976636884684818");
 public BigInteger basey = new BigInteger("174050332293622031404857552280219410364023488927386650641");
 Point Base=new Point(basex, basey);
 public SecureRandom random=new SecureRandom();

 public void key_Gen(){
        System.out.println("\n*****Key Generation Process******");
        //Private key generation
        private_key=new BigInteger(160, random);

        //Public key generation Pb=k*G
        public_key=Base.scalarMul(private_key);

        //Display keys
        System.out.println("Private key is:"+private_key);
        System.out.println("public key is:");
        public_key.showPoint();


    }

 public void signature_gen() throws Exception
 {
     //SecureRandom rand=new SecureRandom();
     BigInteger k=new BigInteger(160, random);
     if(k.compareTo(order_n)>=0)
         signature_gen();
     //BigInteger m=new BigInteger("1234567891");
     BigInteger e, inversek;
     Point p=Base.scalarMul(k);
     BigInteger x=p.getx();
     r=x.mod(order_n); //signature
     if(r.equals(BigInteger.ZERO))
          signature_gen();
     else
     {
        SHA1 h=new SHA1();
        e=h.hash("12345678");
        System.out.println("the hash in generation"+e);
       // inversek=k.modInverse(order_n);
       s=((e.add(private_key.multiply(r))).divide(k)).mod(order_n);

     }
     if(s.equals(BigInteger.ZERO))
         signature_gen();
     //System.out.println("the signature r is "+r);
      // System.out.println("the signature s is "+s);
  }

 public void signature_veri() throws Exception
 {
     System.out.println("The value of r and s in verification");
     System.out.println("the signature r is "+r);
     System.out.println("the signature s is "+s);
     if(r.compareTo(order_n)>=0 && s.compareTo(order_n)>=0)
     {
         System.out.println("the r and s are out of range");
         System.out.println("reject Signature");
     }
     else
     {
       SHA1 h=new SHA1();
       BigInteger e=h.hash("12345678");
       System.out.print("the hash in veri"+e);
       BigInteger w= (BigInteger.ONE.divide(s)).mod(order_n);
       BigInteger u1=e.multiply(w).mod(order_n);
       BigInteger u2=r.multiply(w).mod(order_n);
       Point p=Base.scalarMul(u1);
       Point X=public_key.scalarMul(u2);
       p.pointAdd(X);
       BigInteger x1=X.getx();
       BigInteger v=(x1).mod(order_n);

       System.out.println("the value of v is"+v);
       if(v.equals(r.mod(order_n)))
       {
           System.out.println("signature accepted");
       }
       else
       {
           BigInteger two=new BigInteger("2");
         //  System.out.println("mod inverse of 2 is "+two.modInverse(new BigInteger("5")));
           System.out.println("Signature Rejected");
       }
       }
 }

}


class Point{
    private BigInteger x;
    private BigInteger y;
    private BigInteger p = new BigInteger("6277101735386680763835789423207666416083908700390324961279");
    private BigInteger a=new BigInteger("-3");
    private BigInteger two=new BigInteger("2");
    private BigInteger three=new BigInteger("3");

    //Constructor with default arguments....
    Point(){
        x=BigInteger.ZERO;
        y=BigInteger.ZERO;
    }

    //Constructor with arguments for the object of point
    Point(BigInteger a, BigInteger b){
        x=a;
        y=b;
    }

    //Module for displaying the point
    public void showPoint(){
        System.out.println("("+x+","+y+")");
    }
    public BigInteger getx()
    {
        return x;
    }
    public BigInteger gety()
    {
        return y;
    }


    //Routine for doubling the point
    public Point doublePoint(){
        Point R=new Point();
        BigInteger lambda,u,v;
       // u =((three.multiply(x.pow(2))).add(a));
       // v =(two.multiply(y)).modInverse(p);
        lambda = (((three.multiply(x.pow(2))).add(a)).divide(two.multiply(y))).mod(p);
       // System.out.println("the value of lemda is"+lambda);
        R.x = ((lambda.pow(2)).subtract(two.multiply(x))).mod(p);
        R.y = ((lambda.multiply(x.subtract(R.x))).subtract(y)).mod(p);
        return R;
    }

    //Routine for point addition
    public Point pointAdd(Point Q){
        Point R=new Point();
        BigInteger u,v,lambda;
     //   u = (Q.y).subtract(y);
       // v = ((Q.x).subtract(x)).modInverse(p);
        lambda = (((Q.y).subtract(y)).divide((Q.x).subtract(x))).mod(p);
        R.x = ((lambda.pow(2)).subtract(x).subtract(Q.x)).mod(p);
        R.y = ((lambda.multiply(x.subtract(R.x))).subtract(y)).mod(p);
//        System.out.println("The addition");
//        R.showPoint();
        return R;
    }

    // Routine for negating point
    public Point negatePoint(){
        Point R= new Point();
        R.x=x;
        R.y=y.negate();
        return R;
    }

    public Point scalarMul(BigInteger k){
        Point P=new Point(x,y);
        System.out.println("x and y"+x+" "+y);
        Point R=new Point();
        String str=k.toString(2); //convert into string
        Point Q=new Point();
        R=P;
        BigInteger l1,l2;
        l1=P.x;
        l2=P.y;

        for(int i=1;i<str.length();i++)
        {
            P=P.doublePoint();
            l1=P.x;
            l2=P.y;
            if(str.charAt(i)=='1')
            {
                Q=R.pointAdd(new Point(l1,l2));
                l1=Q.x;
                l2=Q.y;
            }
        }

//        for(int i=str.length()-2;i>=0;i--){
//            P=P.doublePoint();
//            if(str.charAt(i)=='1')
//                Q=Q.pointAdd(P);

       Q=new Point(l1,l2);
        return Q;
    }
}


class SHA1 {

   public BigInteger hash(String s) throws Exception
  {

      // Create a Message Digest from a Factory method
      MessageDigest md = MessageDigest.getInstance("SHA1");

      byte[] dataBytes = s.getBytes();

        md.update(dataBytes);

        byte[] mdbytes = md.digest();

//        //convert the byte to hex format method 1
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < mdbytes.length; i++) {
//          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
//        }

       // System.out.println("Hex format : " + sb.toString());

       //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<mdbytes.length;i++) {
          hexString.append(Integer.toHexString(0xFF & mdbytes[i]));
        }

        //System.out.println("Hex format : " + hexString.toString());
        return (new BigInteger(hexString.toString(),16));
  }

}
