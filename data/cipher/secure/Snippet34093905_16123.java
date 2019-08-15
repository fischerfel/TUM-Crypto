// setup
Signature sigAlg = Signature.getInstance("SHA512withRSA");
sigAlg.initSign(keyPair.getPrivate());
byte[] signature = sigAlg.sign();

// check padding manually
Cipher rsaRaw = Cipher.getInstance("RSA/ECB/NoPadding");
// encrypt or decrypt is actually the same operation for raw RSA
rsaRaw.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
byte[] paddedSig = rsaRaw.doFinal(signature);
// using Bouncy Castle's hex encoder, you can use any encoder
System.out.println(Hex.toHexString(paddedSig));
