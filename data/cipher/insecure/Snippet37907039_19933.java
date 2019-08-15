public static GPKeySet diversify(GPKeySet keys, byte[] diversification_data, Diversification mode, int scp) throws GPException {
    try {
        GPKeySet result = new GPKeySet();
        Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
        for (KeyType v : KeyType.values()) {
            if (v == KeyType.RMAC)
                continue;
            byte [] kv = null;
            // shift around and fill initialize update data as required.
            if (mode == Diversification.VISA2) {
                kv = fillVisa(diversification_data, v);
            } else if (mode == Diversification.EMV) {
                kv = fillEmv(diversification_data, v);
            }

            // Encrypt with current master key
            cipher.init(Cipher.ENCRYPT_MODE, keys.getKey(v).getKey(Type.DES3));

            byte [] keybytes = cipher.doFinal(kv);
            // Replace the key, possibly changing type. G&D SCE 6.0 uses EMV 3DES and resulting keys
            // must be interpreted as AES-128
            GPKey nk = new GPKey(keybytes, scp == 3 ? Type.AES : Type.DES3);
            result.setKey(v, nk);
        }
        return result;
    } catch (BadPaddingException |InvalidKeyException | IllegalBlockSizeException e) {
        throw new GPException("Diversification failed.", e);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
        throw new RuntimeException("Diversification failed.", e);
    }
}

public static byte[] fillVisa(byte[] init_update_response, KeyType key) {
    byte[] data = new byte[16];
    System.arraycopy(init_update_response, 0, data, 0, 2);
    System.arraycopy(init_update_response, 4, data, 2, 4);
    data[6] = (byte) 0xF0;
    data[7] = key.getValue();
    System.arraycopy(init_update_response, 0, data, 8, 2);
    System.arraycopy(init_update_response, 4, data, 10, 4);
    data[14] = (byte) 0x0F;
    data[15] = key.getValue();
    return data;
}
