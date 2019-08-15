RSAKeyPairGenerator kpg = new RSAKeyPairGenerator();
kpg.initialize(1024);
KeyPair kp = kpg.generateKeyPair();
PublicKey pk = kp.getPublic();
PrivateKey pri = kp.getPrivate();
InputStream in = csocket.getInputStream();
OutputStream out = csocket.getOutputStream();
DataInputStream dis = new DataInputStream(in);
DataOutputStream dos = new DataOutputStream(out);

// write getEncoded().length
dos.writeInt(pk.getEncoded().length);

// write key
byte[] public_key = pk.getEncoded();
for (int x=0;x<public_key.length;x++) {
    dos.writeByte(public_key[x]);
}
dos.flush();

// read enc length
int len=dis.readInt();
byte[] data = new byte[len];

// read enc stuff
System.out.println("Read data:");
for (int x=0;x<len;x++) {
    data[x]=dis.readByte();
}
// decrypt
byte [] decrypted = null;
try { cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(Cipher.DECRYPT_MODE, pri);
    decrypted = cipher.doFinal(new String(data).getBytes());
} catch (Exception e) { e.printStackTrace(); }
