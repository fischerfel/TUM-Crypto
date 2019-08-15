static boolean isPrime(long n)
{
    if (n%2 == 0)
    {
        return false;
    }

    for(int i = 3 ; i*i<=n;i+=2)
    {
        if(n%i==0)
            return false;
    }
    return true;
}


public static void main(String [] args) throws Exception
{

    Random randomGenerator = new Random();

    long pValue = randomGenerator.nextInt(1000000);
    long gValue = randomGenerator.nextInt(100000);
    long correctPValue;

    boolean checkPrime = isPrime(pValue);
    System.out.println("the number generated is "+pValue);
    System.out.println(checkPrime);

    while(checkPrime == false)

    {
        long pValue2 = randomGenerator.nextInt(1000000);
        boolean checkPrimeInLoop = isPrime(pValue2);
        //System.out.println("value in loop is "+pValue2);
        if(checkPrimeInLoop == true)
        {
            pValue=pValue2;
            break;
        }
    }


    long checkSP = (pValue*2)+1;
    boolean checkSafePrime = isPrime(checkSP);
    //System.out.println(checkSafePrime);
    while(checkSafePrime==false)
    {
        long pValue3=randomGenerator.nextInt(1000000);
        boolean checkPrimeInLoop = isPrime(pValue3);
        long pValue5=(pValue3*2)+1;
        //boolean checkSafePrimeInLoop = isPrime(pValue4);
        boolean checkSafePrime2InLoop = isPrime(pValue5);

        if(checkSafePrime2InLoop == true && checkPrimeInLoop == true)
        {
            pValue=pValue3;
            break;
        }

    }

    //System.out.println("the safe prime is"+pValue);//safe prime

    while(gValue>pValue)
    {
        long gValue2=randomGenerator.nextInt(100000);

        if(gValue2<pValue)
        {
            gValue=gValue2;
            break;
        }
    }

    long getDivisor = (pValue-1)/2;
    BigInteger bi1,bi2,bi3,bi4,bi10,bi11,bi12,bi13;

    bi1=BigInteger.valueOf(getDivisor);

    bi2 = BigInteger.valueOf(pValue);

    bi3 = BigInteger.valueOf(gValue);

    bi4= bi3.modPow(bi1,bi2);

    String getBi1 = bi1.toString();

    String getBi2 = bi2.toString();

    String getBi3 = bi3.toString();

    String getBi4 = bi4.toString();


    //bi10 = new BigInteger(getBi1,64); // divisor
    //bi11 = new BigInteger(getBi2,64); // safe prime value
    //bi12 = new BigInteger(getBi3,64); // generator value
    //bi13 = new BigInteger(getBi4,64); // modular value




    long calculatedValue = bi4.longValue();


    while(calculatedValue == 1)
    {
        long gValue3=randomGenerator.nextInt(100000);
        long getDivisorInLoop = (pValue-1)/2;
        BigInteger bi5,bi6,bi7,bi8,bi14,bi15,bi16,bi17,bi18;

        bi5=BigInteger.valueOf(getDivisorInLoop);

        bi6 = BigInteger.valueOf(pValue);

        bi7 = BigInteger.valueOf(gValue3);

        bi8= bi7.modPow(bi5,bi6);


        String getBi5 = bi5.toString();

        String getBi6 = bi6.toString();

        String getBi7 = bi7.toString();

        String getBi8 = bi8.toString();


        //bi14 = new BigInteger(getBi1,64); // divisor
        //bi15 = new BigInteger(getBi2,64); // safe prime value
        //bi16 = new BigInteger(getBi3,64); // generator value
        //bi17 = new BigInteger(getBi4,64); // modular value


        long calculatedValueInLoop = bi8.longValue();
        System.out.println("the proof that it is  a generator is "+calculatedValueInLoop);
        if(calculatedValueInLoop!=1)
        {
            gValue=gValue3;
            break;
        }
    }

    BigInteger generatorValue,primeValue,biA,biB,skA,skB,sharedKeyA,sharedKeyB;

    generatorValue = BigInteger.valueOf(gValue);
    primeValue = BigInteger.valueOf(pValue);
    long SecretKeyA=generateSKA();
    long SecretKeyB=generateSKB();
    skA = BigInteger.valueOf(SecretKeyA);
    skB = BigInteger.valueOf(SecretKeyB);
    biA=generatePkA(generatorValue,primeValue,SecretKeyA);
    biB=generatePkB(generatorValue,primeValue,SecretKeyB);
    sharedKeyA = calculateSharedKey(biB,skA,primeValue);
    sharedKeyB = calculateSharedKey(biA,skB,primeValue);

    System.out.println("the safe prime is"+primeValue);
    System.out.println("the generator of the safe prime is "+generatorValue);
    System.out.println("the public key of A is "+generatePkA(generatorValue,primeValue,SecretKeyA));
    System.out.println("the public key of B is "+generatePkB(generatorValue,primeValue,SecretKeyB));
    System.out.println("the shared key for A is"+calculateSharedKey(biB,skA,primeValue));
    System.out.println("the shared key for B is"+calculateSharedKey(biA,skB,primeValue));
    System.out.println("The secret key for A is"+generateSKA());

    //createKey();
    String getAValue=sharedKeyA.toString();
    String getBValue=sharedKeyB.toString();

    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update(getAValue.getBytes());

    byte byteData[] = md.digest();
    StringBuffer sb = new StringBuffer();

    for(int i=0;i<byteData.length;i++)
    {
        sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
    }

    String getHexValue = sb.toString();
    System.out.println("hex format in SHA-256 is "+getHexValue);

    //createSpecificKey(biG,biP);

    byte [] key = getAValue.getBytes("UTF-8");

    MessageDigest sha = MessageDigest.getInstance("SHA-256");

    key =  sha.digest(key);
    key = Arrays.copyOf(key, 16);
    SecretKeySpec secretKeySpec =  new SecretKeySpec(key,"AES");

    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

    CipherInputStream cipt = new CipherInputStream(new FileInputStream(new File("C:\\Users\\Larry\\Desktop\\Java\\diffie hellman\\src\\jessica.jpg")),cipher); // enter your filename here
    FileOutputStream fop=new FileOutputStream(new File("C:\\Users\\Larry\\Desktop\\Java\\diffie hellman\\src\\testEncrypt.jpg"));



    int i;
    while((i=cipt.read())!= -1)
    {
        fop.write(i);
    }

    cipher.init(Cipher.DECRYPT_MODE,secretKeySpec);

    CipherInputStream cipt2 = new CipherInputStream(new FileInputStream(new File("C:\\Users\\Larry\\Desktop\\Java\\diffie hellman\\src\\testEncrypt.jpg")),cipher); // encryption of image
    FileOutputStream fop2 = new FileOutputStream(new File("C:\\Users\\Larry\\Desktop\\Java\\diffie hellman\\src\\testDecrypt.jpg"));//decryption of images

    int j;
    while((j=cipt2.read())!=-1)
    {
        fop2.write(j);
    }





}

public static BigInteger calculateSharedKey(BigInteger pk , BigInteger sk, BigInteger safePrime)
{
    BigInteger sharedKey;

    sharedKey = pk.modPow(sk, safePrime);

    return sharedKey;
}


public static long generateSKA()
{
    Random randomGenerator2=new Random();
    long SKa = randomGenerator2.nextInt(1000000000);

    return SKa;
}

public static long generateSKB()
{
    Random randomGenerator3=new Random();
    long SKb = randomGenerator3.nextInt(10000000);

    return SKb;
}


public static BigInteger generatePkA(BigInteger g,BigInteger p,long skA)
{
    BigInteger Pka,SK;
    long secretKeyA = skA;

    SK = BigInteger.valueOf(secretKeyA);

    Pka=g.modPow(SK, p);

    return Pka;
}


public static BigInteger generatePkB(BigInteger g,BigInteger p,long skB)
{
    BigInteger Pkb,SK;
    long secretKeyB = skB;
    SK = BigInteger.valueOf(secretKeyB);
    Pkb=g.modPow(SK, p);
    return Pkb;
}
