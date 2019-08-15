final String password = "test";
int pswdIterations = 65536  ;
int keySize = 256;
byte[] ivBytes;
byte[] saltBytes = {0,1,2,3,4,5,6};

SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(), 
                    saltBytes, 
                    pswdIterations, 
                    keySize
                    );

SecretKey secretKey = factory.generateSecret(spec);

SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(),"AES");
