private String iv;
private String key;
private IvParameterSpec mIvParameterSpec;
private SecretKeySpec mSecretKeySpec;
private Cipher mCipher;

public MCrypt(String iv, String key) {
    Log.v(TAG, "IV Bytes.length=" + iv.getBytes().length);
    this.iv = cut(iv, 16);
    Log.i(TAG, "IV = " + this.iv + ", Bytelength=" + this.iv.getBytes().length);
    this.key = key;

    mIvParameterSpec = new IvParameterSpec(this.iv.getBytes());
    mSecretKeySpec = new SecretKeySpec(this.key.getBytes(), "AES");

    try {
        mCipher = Cipher.getInstance("AES/CBC/NoPadding");
    } catch (NoSuchAlgorithmException e) {
        Log.e(TAG, "Got Exception while initializing mCipher: " + e.toString(), e);
    } catch (NoSuchPaddingException e) {
        Log.e(TAG, "Got Exception while initializing mCipher: " + e.toString(), e);
    }
}

public String getIV() {
    return this.iv;
}

public byte[] decrypt(String code) throws Exception {
    if(code == null || code.length() == 0) {
        throw new Exception("Emtpy string given");
    }

    byte[] decrypted = null;

    try {
        mCipher.init(Cipher.DECRYPT_MODE, mSecretKeySpec, mIvParameterSpec);
        decrypted = mCipher.doFinal(hexToBytes(code));
    } catch(Exception e) {
        throw new Exception("[decrypt] " + e.getMessage());
    }

    return decrypted;
}

private byte[] hexToBytes(String str) {
    if (str==null) {
        return null;
    } else if (str.length() < 2) {
        return null;
    } else {
        int len = str.length() / 2;
        byte[] buffer = new byte[len];
        for (int i=0; i<len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i*2,i*2+2),16);
        }
        return buffer;
    }
}

private String cut(String s, int n) {
    byte[] sBytes = s.getBytes();

    if(sBytes.length < n) {
        n = sBytes.length;
    }

    int n16 = 0;
    boolean extraLong = false;

    int i = 0;

    while(i < n) {
        n16 += (extraLong) ? 2 : 1;
        extraLong = false;

        if((sBytes[i] & 0x80) == 0) {
            i += 1;
        }
        else if((sBytes[i] & 0xC0) == 0x80) {
            i += 2;
        }
        else if((sBytes[i] & 0xE0) == 0xC0) {
            i += 3;
        } else {
            i += 4;
            extraLong = true;
        }
    }
    return s.substring(0, n16);
}
}
