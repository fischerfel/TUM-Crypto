sslContext = SSLContext.getInstance("TLS");
/*   some code to initialize ssl context  */
SSLSocketFactory sslSocketfactory =  sslContext.getSocketFactory();
sock = (SSLSocket) sslSocketfactory.createSocket(host,port1);
sock.setSoTimeout(12000);

is = new DataInputStream(new BufferedInputStream(sock.getInputStream(),
                                 16384));
os = sock.getOutputStream();
/*  some more code using above mentioned is and os to recieve rc4 key and 
    write it into a byte array   */

SecretKey key2 = new SecretKeySpec(reverserRc4InBytes, 0, reverserRc4InBytes.length, "RC4");
Cipher cipherOS = Cipher.getInstance("RC4");
cipherOS.init(Cipher.ENCRYPT_MODE, key2);


Cipher cipherIS = Cipher.getInstance("RC4");
cipherIS.init(Cipher.DECRYPT_MODE, key2);
cos = new CipherOutputStream(os,cipherOS);
cis = new CipherInputStream(is,cipherIS);
