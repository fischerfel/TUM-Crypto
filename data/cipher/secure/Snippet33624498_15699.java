Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5padding");
byte[] keyBuf= new byte[16];

byte[] b= key.getBytes("UTF-8");
int len= b.length;
if (len > keyBuf.length) len = keyBuf.length;

System.arraycopy(b, 0, keyBuf, 0, len);
SecretKeySpec keySpec = new SecretKeySpec(keyBuf, "AES256");


byte[] ivBuf= new byte[16];
            //IvParameterSpec ivSpec = new IvParameterSpec(ivBuf);
IvParameterSpec ivSpec=null; 

cipher.init(Cipher.ENCRYPT_MODE, keySpec);

byte[] results = cipher.doFinal(text.getBytes("UTF-8"));

String result = Base64.encodeBase64String(results);
return result;
