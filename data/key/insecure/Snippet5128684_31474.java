byte[] expectedResult = { /* Expected HMAC result from a prior run */
        96, 21, 116, 11, 4, -51, -115, -20, 104, 18, 117, -75, 3, -100, 126,
        -89, -22, 120, -120, 30, 102, 104, -125, -120, -62, 111, -75,
        24, 14, 62, 48, -65 };

byte[] secret = "your eyes only".getBytes();
String algorithm = "HmacSha256";

SecretKeySpec signingKey = new SecretKeySpec(secret, algorithm);

// Init HMAC usign secret
Mac hmac = Mac.getInstance(algorithm);
hmac.init(signingKey);

// Run message through HMAC and calculate result
byte[] message = "Don't tamper with me".getBytes();
byte[] macOutput = hmac.doFinal(message);

// Compare HMAC output to expected result
// A message that has been altered will not be equal
assertTrue(Arrays.equals(macOutput, expectedResult));
