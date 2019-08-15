byte[] bytes = new byte[32];
String message = "secret";
MessageDigest md = MessageDigest.getInstance("SHA-256");
bytes = md.digest(message.getBytes("UTF-8"));

JWSSigner signer = new MACSigner(bytes);

// Prepare JWT with claims set
JWTClaimsSet claimsSet = new JWTClaimsSet();
claimsSet.setSubject("alice");

SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

// Apply the HMAC
signedJWT.sign(signer);

// To serialize to compact form, produces something like
String s = signedJWT.serialize();
