if ((len = inputStream.read(mainBuffer)) > -1) {
                totalLength = len;
            }
if (totalLength > 0) {
                byteToAscii = function.byteToAscii(mainBuffer, totalLength);
            }
if (byteToAscii.length() > 0) {
                completeHexString = function.stringToHex(byteToAscii);               
                debugInfo = "FRAME RECV.=" + completeHexString;
/* FRAME RECV.=41ed34a41a9de6d270aa1e1464527e88c8bee66a00cfb308f60c105de81db0f1ce43d8c0b9bc4e8070b5ab8d4d3650b55d23223fc687bb1485945bc3228e9707a7aecda9f90657e0ac009571c6469c58a2cd9793cc433ccb5993f2*/
            }
byte[] key = new byte[]{31, 30, 31, 36, 32, 11, 11, 11, 22, 26, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30};
myKeySpec = new DESedeKeySpec(key);
mySecretKeyFactory = SecretKeyFactory.getInstance("TripleDES");
dekey = mySecretKeyFactory.generateSecret(myKeySpec);
byte[] zeros = {0, 0, 0, 0, 0, 0, 0, 0};
IvParameterSpec iv = new IvParameterSpec(zeros);
Cipher c = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
c.init(Cipher.DECRYPT_MODE, key, iv);
byte[] decordedValue = new BASE64Decoder().decodeBuffer(completeHexString);
byte[] decValue = c.doFinal(decordedValue);
String decryptedValue = new String(decValue);
System.out.println("decryptedValue= " + decryptedValue);
