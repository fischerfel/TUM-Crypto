InputStream in = socket.getInputStream();
OutputStream out = socket.getOutputStream();
DataInputStream dis = new DataInputStream(in);
DataOutputStream dos = new DataOutputStream(out);

// Read key length
int len = dis.readInt();

// Read key
byte[] public_key = new byte[len];
byte[] tmp = new byte[1];
for (int x = 0; x<len; x++) {
    public_key[x] = dis.readByte();
}

try { 
    keySpec = new X509EncodedKeySpec(public_key);
    keyFactory = keyFactory.getInstance("RSA");
    publicKey = keyFactory.generatePublic(keySpec);
} catch (Exception e) { e.printStackTrace(); }

try {
    cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "IBMJCE");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    // crypt string
    data = cipher.doFinal(new String("Encrypt this").getBytes());
} catch (Exception e) { e.printStackTrace(); }

// write data.length
dos.writeInt(data.length);

// write encrypted data
for (int x=0; x<data.length;x++) {
      dos.writeByte(data[x]);
}
dos.flush();
