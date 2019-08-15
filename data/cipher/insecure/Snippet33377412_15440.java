public String encrypt(String var1) throws BlowfishCipher.BCException {
    try {
        byte[] var2 = var1.getBytes("UTF-8");
        byte[] var3 = this.encrypt(var2);
        return bytesToHexString(var3);
    } catch (Exception var4) {
        if(var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
        } else if(var4 instanceof BlowfishCipher.BCException) {
            throw (BlowfishCipher.BCException)var4;
        } else {
            throw new BlowfishCipher.BCException(var4);
        }
    }
}

public byte[] encrypt(byte[] var1) throws BlowfishCipher.BCException {
    try {
        Cipher var2 = Cipher.getInstance("Blowfish");
        byte[] var3 = hexStringToBytes(this.sKey);
        SecretKeySpec var4 = new SecretKeySpec(var3, "Blowfish");
        var2.init(1, var4);
        return var2.doFinal(var1);
    } catch (Exception var5) {
        if(var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
        } else {
            throw new BlowfishCipher.BCException(var5);
        }
    }
}

private static String bytesToHexString(byte[] var0) {
    StringBuffer var1 = new StringBuffer();

    for(int var2 = 0; var2 < var0.length; ++var2) {
        String var3 = Integer.toHexString(255 & var0[var2]);
        if(var3.length() < 2) {
            var1.append("0");
        }

        var1.append(Integer.toHexString(255 & var0[var2]));
    }

    return var1.toString();
}
