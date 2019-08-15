    String secretCodeString = "Mk9m98IfEblmPfrpsawt7BmxObt98Jev";
    String Ds_Merchant_Order = "1442772645";
    String Ds_MerchantParameters = "eyJEU19NRVJDSEFOVF9BTU9VTlQiOiIxNDUiLCJEU19NRVJDSEFOVF9PUkRFUiI6IjE0NDI3NzI2NDUiLCJEU19NRVJDSEFOVF9NRVJDSEFOVENPREUiOiI5OTkwMDg4ODEiLCJEU19NRVJDSEFOVF9DVVJSRU5DWSI6Ijk3OCIsIkRTX01FUkNIQU5UX1RSQU5TQUNUSU9OVFlQRSI6IjAiLCJEU19NRVJDSEFOVF9URVJNSU5BTCI6Ijg3MSIsIkRTX01FUkNIQU5UX01FUkNIQU5UVVJMIjoiaHR0cHM6XC9cL2VqZW1wbG9cL2VqZW1wbG9fVVJMX05vdGlmLnBocCIsIkRTX01FUkNIQU5UX1VSTE9LIjoiaHR0cHM6XC9cL2VqZW1wbG9cL2VqZW1wbG9fVVJMX09LX0tPLnBocCIsIkRTX01FUkNIQU5UX1VSTEtPIjoiaHR0cHM6XC9cL2VqZW1wbG9cL2VqZW1wbG9fVVJMX09LX0tPLnBocCJ9";

    byte [] secretCode = decodeB64(secretCodeString.getBytes("UTF-8"));
    String secretKc = toHexadecimal(secretCode, secretCode.length);
    byte [] Ds_Merchant_Order_encrypt3DES = encrypt_3DES(secretKc, Ds_Merchant_Order);
    byte [] hash = mac256(Ds_MerchantParameters, Ds_Merchant_Order_encrypt3DES);
    byte [] res = encodeB64UrlSafe(hash);
    String Ds_Signature = new String(res, "UTF-8");
    //Ds_Signature: hueCwD/cbvrCi+9IDY86WteMpXulIl0IDNXNlYgcZHM=


public byte [] encrypt_3DES(final String claveHex, final String datos) {
        byte [] ciphertext = null;
        try {
            DESedeKeySpec desKeySpec = new DESedeKeySpec(toByteArray(claveHex));
            SecretKey desKey = new SecretKeySpec(desKeySpec.getKey(), "DESede");
            Cipher desCipher = Cipher.getInstance("DESede/CBC/NoPadding");
            byte [] IV = {0, 0, 0, 0, 0, 0, 0, 0};

            desCipher.init(Cipher.ENCRYPT_MODE, desKey, new IvParameterSpec(IV));

            int numeroCerosNecesarios = 8 - (datos.length() % 8);
            if (numeroCerosNecesarios == 8) {
                numeroCerosNecesarios = 0;
            }
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            array.write(datos.getBytes("UTF-8"), 0, datos.length());
            for (int i = 0; i < numeroCerosNecesarios; i++) {
                array.write(0);
            }
            byte [] cleartext = array.toByteArray();

            ciphertext = desCipher.doFinal(cleartext);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return ciphertext;
    }
