final Charset charset = Charset.forName("ASCII");
final String secret = "mykey";
final String data = "A message to be turned to gibberish";
final String algorithm = "HmacSHA512";

byte[] keyBytes = secret.getBytes(charset);
SecretKeySpec signingKey = new SecretKeySpec(keyBytes, algorithm);

Mac mac = Mac.getInstance(algorithm);
mac.init(signingKey);

byte[] hashed = mac.doFinal(data.getBytes(charset));
System.out.println(new String(hashed, charset));
