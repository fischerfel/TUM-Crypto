Cipher cipher = Cipher.getInstance("DESede");
cipher.init(Cipher.ENCRYPT_MODE, key);

// Create a special output stream to do the work for us
CipherOutputStream cos = new CipherOutputStream(out, cipher);

// Read from the input and write to the encrypting output stream
byte[] buffer = new byte[2048];
int bytesRead;
while ((bytesRead = in.read(buffer)) != -1) {
    cos.write(buffer, 0, bytesRead);
}
cos.close();
