private static SecretKey generateKey() throws Exception
    {

        SecretKey  key = new SecretKeySpec(org.apache.commons.codec.binary.Hex.decodeHex(klucz.toCharArray()), "AES");         

        return key;
    }
