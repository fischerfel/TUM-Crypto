private static final int WARMUP_COUNT = 5;
private static final int FILE_LENGTH = 1024*512;
private static final int ITERATOR_COUNT = 1000;
private static final double TO_MSSECONDS = 1_000_000.0 * (ITERATOR_COUNT-WARMUP_COUNT);
static final private byte[] ivBytes = new byte[] { 0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
private static final IvParameterSpec ivSpec16bytes = new IvParameterSpec(ivBytes);
private static final IvParameterSpec ivSpec8bytes = new IvParameterSpec(Arrays.copyOfRange(ivBytes,0,8));

static String[] algosWithMode = {"AES/CBC/PKCS7Padding","Blowfish/CBC/PKCS7Padding","CAST5/CBC/PKCS7Padding","DES/CBC/PKCS7Padding","DESede/CBC/PKCS7Padding",  "IDEA/CBC/PKCS7Padding","ARC4", };
static String[] algos = {   "AES","Blowfish","CAST5","DES", "DESede","IDEA","ARC4"  };
static int[] keylenngth = {128,128,128,56,  168,128,128 };


@SuppressWarnings("unused")
public static void main(String[] args) throws Exception {

    if(ITERATOR_COUNT <= WARMUP_COUNT )
        throw new Exception("iterator count must be greater than warm up count iterator: "+ITERATOR_COUNT
                +" warmup count :" + WARMUP_COUNT);

    Security.addProvider(new BouncyCastleProvider());
    Key key = null;
    byte[] plainText=null;
    byte[] cipherText=null;
    byte[] decryptedText=null;
    long startTime;
    DecimalFormat df = new DecimalFormat("0.000"); 


    for (int k = 0; k < 7; k++) {

        long timeDec = 0,timeEnc = 0,timekey = 0;
        long maxtimeDec = 0,maxtimeEnc = 0,maxtimekey = 0;
        long mintimeDec = Long.MAX_VALUE,mintimeEnc = Long.MAX_VALUE,mintimekey = Long.MAX_VALUE;
        long topDec = 0,topEnc = 0,topkey = 0;



        for (int i = 0; i < ITERATOR_COUNT; i++) {



            SecureRandom random= new SecureRandom();
            plainText = random.generateSeed(FILE_LENGTH);


            startTime=System.nanoTime();
            KeyGenerator keyGen = KeyGenerator.getInstance(algos[k]);
            keyGen.init(keylenngth[k],random);
            key=keyGen.generateKey();
            timekey=System.nanoTime()-startTime;


            Cipher cipher=cipher.getInstance(algosWithMode[k]);
            if(k == 0){

                 cipher.init(Cipher.ENCRYPT_MODE, key,ivSpec16bytes); 
            }else if(k == 6){

                cipher.init(Cipher.ENCRYPT_MODE, key); 
            }else{

                cipher.init(Cipher.ENCRYPT_MODE, key,ivSpec8bytes);
            }

            startTime=System.nanoTime();
            cipherText = cipher.doFinal(plainText);
            timeEnc=System.nanoTime()-startTime;


            if(k == 0){

                 cipher.init(Cipher.DECRYPT_MODE, key,ivSpec16bytes); 
            }else if(k== 6){

                cipher.init(Cipher.DECRYPT_MODE, key); 
            }else {

                cipher.init(Cipher.DECRYPT_MODE, key,ivSpec8bytes);
            }

            startTime=System.nanoTime();
            cipher.doFinal(cipherText);
            timeDec=System.nanoTime()-startTime;

            if (i >= WARMUP_COUNT) {
                if (maxtimeEnc < timeEnc)
                    maxtimeEnc = timeEnc;
                if (maxtimeDec < timeDec)
                    maxtimeDec = timeDec;
                if (maxtimekey < timekey)
                    maxtimekey = timekey;
                if (mintimeEnc > timeEnc)
                    mintimeEnc = timeEnc;
                if (mintimeDec > timeDec)
                    mintimeDec = timeDec;
                if (mintimekey > timekey)
                    mintimekey = timekey;
                topEnc += timeEnc;
                topDec += timeDec;
                topkey += timekey;
            }



        }
        double avgEnc=topEnc/TO_MSSECONDS;
        double avgDec=topDec/TO_MSSECONDS;
        double avgKey=topkey/TO_MSSECONDS;
        System.out.println("********************************************************"+algos[k]+"*****************************************************************");
        System.out.println("Avg Enc :"+df.format(avgEnc)+" - "+" Avg Dec :"+df.format(avgDec)+"-"+" Avg Key :"+ df.format(avgKey));
        System.out.println("Max Enc :"+df.format(maxtimeEnc/1_000_000.0)+" - "+" Max Dec :"+df.format(maxtimeDec/1_000_000.0)+"-"+" Max Key :"+ df.format(maxtimekey/1_000_000.0));
        System.out.println("Min Enc :"+df.format(mintimeEnc/1_000_000.0)+" - "+" Min Dec :"+df.format(mintimeDec/1_000_000.0)+"-"+" Min Key :"+ df.format(mintimekey/1_000_000.0));

    }


}
