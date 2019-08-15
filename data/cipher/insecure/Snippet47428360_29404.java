public static String getDecryptedDataKey(String instr, String type) throws Exception {
        String enc_key = ENC_KEY;   
        if (type.equalsIgnoreCase("KEYDEC")) {
            instr = prop.getProperty("Key");
            enc_key = VTransactConstant.DATA_KEY_ENC_KEY;
        }
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(HexfromString(enc_key), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(2, skeySpec);
            if (instr != null && !instr.equals("")) {
                byte encstr[] = cipher.doFinal(HexfromString(instr));
                return new String(encstr);
            } else {
                return "";
            }
        } catch (BadPaddingException nse) {
            log.doLog(LOGC.LTI, "SecurityUtil", "getDecryptedString", "Exception  :" + VTransactUti.getStackTrace(nse),
                    "", "", "");
            return "";
        }
    }
