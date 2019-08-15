IvParameterSpec = createObject("java", "javax.crypto.spec.IvParameterSpec");
Cipher = createObject("java", "javax.crypto.Cipher");
SecretKeySpec = createObject("java", "javax.crypto.spec.SecretKeySpec");
BASE64Decoder = createObject("java", "sun.misc.BASE64Decoder");
Str = createObject("java", "java.lang.String");
MessageDigest = createObject("java", "java.security.MessageDigest");

input  = "<xml><PanNumber>6280390027626871</PanNumber><Req_Currency_Code>826</Req_Currency_Code><Card_Pin>1234</Card_Pin><Till_Amount></Till_Amount><Auth_Code></Auth_Code></xml>";
key = "06098140901984F95E139F29B479D952CB6545C177D21456";

md = MessageDigest.getInstance("MD5");
md.update(key.getBytes("UTF-8"), 0, key.length());
keyBytes = md.digest();
newKey = tobase64(keyBytes);
keyBytes2  = binaryDecode(newKey, "base64");
keyBytes2  = arrayMerge(keyBytes, arraySlice(keyBytes, 1, 8));
allnewKey = binaryEncode(javacast("byte[]", keyBytes2), "base64");

encrypted = encrypt(input, allnewKey, "desede", "base64");
WriteDump("encrypted (CF): "& encrypted);`
