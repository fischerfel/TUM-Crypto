Provider p;
Cipher   c;

p = Security.getProvider("BC");
c = Cipher.getInstance("RSA/ECB/PKCS1Padding", p);
