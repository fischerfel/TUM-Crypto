// testing code to sign it myself
// read the data file in
FileInputStream dataStream = new FileInputStream(input);
byte[] dataBytes = new byte[dataStream.available()];
dataStream.read(dataBytes);
dataStream.close();

// hash the data file, like i do with openssl
// per Nikolay's comments, this is not needed
MessageDigest digest = MessageDigest.getInstance("SHA-1");
// digest.update(dataBytes, 0, dataBytes.length);
// dataBytes = digest.digest();
Log.i(TAG, "data from file: " + new String(dataBytes));

// read the private key in
FileInputStream fis = new FileInputStream(Environment.getExternalStorageDirectory() + "/phaero/private_key.pem");
byte[] keyBytes = new byte[fis.available()];
fis.read(keyBytes);
fis.close();

// clean up the private key and decode it
String temp = new String(keyBytes);
Log.i(TAG, "private key: " + temp);
String privKeyPEM = temp.replace("-----BEGIN RSA PRIVATE KEY-----\n", "");
privKeyPEM = privKeyPEM.replace("-----END RSA PRIVATE KEY-----", "");
byte[] decoded = Base64.decode(privKeyPEM.getBytes(), Base64.DEFAULT);

// create the private key object from the private key data
PKCS8EncodedKeySpec private_spec = new PKCS8EncodedKeySpec(decoded);
KeyFactory kf = KeyFactory.getInstance("RSA");
PrivateKey privateKey =  kf.generatePrivate(private_spec);

// set up for signing
Signature signer = Signature.getInstance(signAlgo);
signer.initSign(privateKey);
signer.update(dataBytes, 0, dataBytes.length);

// sign, and hash again so i can compare against openssl output
byte[] signed = signer.sign();
digest.update(signed, 0, signed.length);
Log.i(TAG, new BigInteger(1, digest.digest()).toString(16));
