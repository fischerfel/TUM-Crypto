SecretKeySpec keySpec = new SecretKeySpec(
        "CnZ3QvfIjYLL0FWDQeY9L+1XLQKv0jtufAqUcXYP9krzAjhYJvOuiAdBZqt9Ogw7".getBytes(),
        "HmacSha1");

Mac mac = Mac.getInstance("HmacSha1");
mac.init(keySpec);
byte[] result = mac.doFinal("pesho".getBytes());

String decoded = bytesToHex(result).toLowerCase();
System.out.println(decoded);
BASE64Encoder encoder = new BASE64Encoder();
System.out.println(encoder.encode(decoded.getBytes(StandardCharsets.ISO_8859_1)));
awsAccessKey
