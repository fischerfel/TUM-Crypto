Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"));

Xprev = java.util.Arrays.copyOfRange(IV, 0, BlockSize);
Yprev = java.util.Arrays.copyOfRange(IV, BlockSize, IV.length);

Decripted = new byte[0];

for (int i = 0; i < Message.length; i += BlockSize) { 
    Y = java.util.Arrays.copyOfRange(Message, i, i+BlockSize); 
    X = XOR(cipher.doFinal(XOR(Y,Xprev)), Yprev);
    Xprev = X;
    Yprev = Y;

    Decripted = sumBytes(Decripted, X);
}
