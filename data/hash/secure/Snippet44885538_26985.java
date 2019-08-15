long timestamp = 1499084258;
String nonce= "37822614634975090106662";
String httpMethod = "GET"; // HTTP Method of the resource that is being called
String encodedResourceUrl = "https://sandbox.interswitchng.com/api/v2/quickteller/categorys"; // put the resource URL here
String clientId = "IKIA9D981C53698A71925002C81E09104959B975G5C41E1"; // put your client Id here
String clientSecretKey = "d5uAr+U8QhSvYu0809vQtKop3kRslRBC5Q+SwIt+/r4nk+y0="; // put your client secret here
String signatureCipher = httpMethod + "&" + encodedResourceUrl + "&" + timestamp + "&" + nonce + "&" + clientId + "&" + clientSecretKey;
MessageDigest messageDigest = MessageDigest.getInstance(signatureMethod);
byte[] signatureBytes = messageDigest.digest(signatureCipher.getBytes());
String signature = new String(Base64.encodeBase64(signatureBytes));

signature = bqqFzzDe8jJOwjR9/0cY8Lh3Uik=
