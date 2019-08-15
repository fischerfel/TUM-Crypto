
kgen = KeyGenerator.getInstance("AES");
            kgen.init(128);
            raw = new byte[]{(byte)0x00,(byte)0x11,(byte)0x22,(byte)0x33,(byte)0x44,(byte)0x55,(byte)0x66,(byte)0x77,(byte)0x88,(byte)0x99,(byte)0xaa,(byte)0xbb,(byte)0xcc,(byte)0xdd,(byte)0xee,(byte)0xff};
            skeySpec = new SecretKeySpec(raw, "AES");
            cipher = Cipher.getInstance("AES");

            plainText=null;
            cipherText=null;

