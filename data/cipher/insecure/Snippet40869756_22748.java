public class UUDES {

    public static void main (String[] args) throws Exception
    {                                   
        String password = "xxxxxx";
        String pathToUUEencodedEncryptedFile = "C:\DES\path-to-decoded-and-encrypted-file";

        byte[] secretKey = passwordToKey(password);
        byte[] iv = new byte[8];        
        byte[] uuEncodedFile = Files.readAllBytes(Paths.get(pathToUUEencodedEncryptedFile));

        SecretKey key = new SecretKeySpec(secretKey, "DES");

        Cipher decryptor = Cipher.getInstance("DES/CBC/NoPadding");
        decryptor.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));          

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        InputStream in = null;      
        try {
            in = MimeUtility.decode(new ByteArrayInputStream(uuEncodedFile), "uuencode");
            byte[] buf = new byte[1024];
            int length;
            while (true) {
                length = in.read(buf);
                if (length == -1) {
                    break;
                }
                bout.write(buf, 0, length);
            }

            byte[] bytesDecrypted = decryptor.doFinal(bout.toByteArray());

            ByteArrayInputStream bais = new ByteArrayInputStream(bytesDecrypted);
            GZIPInputStream gzis = new GZIPInputStream(bais);
            InputStreamReader reader = new InputStreamReader(gzis);
            BufferedReader buffer = new BufferedReader(reader);

            String readed;
            while ((readed = buffer.readLine()) != null) {
                System.out.println(readed);
            }                              
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }                                   
    }

    static SecretKey generateSecretKey(byte[] key) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
        KeySpec keySpec = new DESKeySpec(key);
        SecretKey secretKey = factory.generateSecret(keySpec);
        return secretKey;
    }

    static byte[] passwordToKey(String password) throws Exception
    {
        if (password == null)
            throw new IllegalArgumentException("password");
        if (password == "")
            throw new IllegalArgumentException("password");

        byte[] key = new byte[8];

        for (int i = 0; i < password.length(); i++)
        {
            int c = (int)password.charAt(i);
            if ((i % 16) < 8)
            {
                key[i % 8] ^= (byte)(c << 1);
            }
            else
            {
                // reverse bits e.g. 11010010 -> 01001011
                c = (((c << 4) & 0xf0) | ((c >> 4) & 0x0f));
                c = (((c << 2) & 0xcc) | ((c >> 2) & 0x33));
                c = (((c << 1) & 0xaa) | ((c >> 1) & 0x55));
                key[7 - (i % 8)] ^= (byte)c;
            }
        }

        addOddParity(key);

        byte[] target = new byte[8];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        outputStream.write(password.getBytes("US-ASCII"));
        outputStream.write(new byte[8]);
        byte[] temp = outputStream.toByteArray();
        outputStream = new ByteArrayOutputStream( );
        for (int i = 0; i < (password.length() + (8 - (password.length() % 8)) % 8); ++i) {
            outputStream.write(temp[i]);
        }
        byte[] passwordBuffer = outputStream.toByteArray(); 

        Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
        byte[] iv = key;

        IvParameterSpec ivspec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, generateSecretKey(key), ivspec);
        for (int x = 0; x < passwordBuffer.length / 8; ++x)
        {
            cipher.update(passwordBuffer, 8 * x, 8, target, 0);
        }

        addOddParity(target);

        return target;
    }

    static void addOddParity(byte[] buffer)
    {
        for (int i = 0; i < buffer.length; ++i)
        {
            buffer[i] = _oddParityTable[buffer[i] & 0xFF];
        }
    }

    static byte[] _oddParityTable = {
            (byte)1,(byte)1,(byte)2,(byte)2,(byte)4,(byte)4,(byte)7,(byte)7,(byte)8,(byte)8,(byte)11,(byte)11,(byte)13,(byte)13,(byte)14,(byte)14,
            (byte)16,(byte)16,(byte)19,(byte)19,(byte)21,(byte)21,(byte)22,(byte)22,(byte)25,(byte)25,(byte)26,(byte)26,(byte)28,(byte)28,(byte)31,(byte)31,
           (byte)32,(byte)32,(byte)35,(byte)35,(byte)37,(byte)37,(byte)38,(byte)38,(byte)41,(byte)41,(byte)42,(byte)42,(byte)44,(byte)44,(byte)47,(byte)47,
           (byte)49,(byte)49,(byte)50,(byte)50,(byte)52,(byte)52,(byte)55,(byte)55,(byte)56,(byte)56,(byte)59,(byte)59,(byte)61,(byte)61,(byte)62,(byte)62,
           (byte)64,(byte)64,(byte)67,(byte)67,(byte)69,(byte)69,(byte)70,(byte)70,(byte)73,(byte)73,(byte)74,(byte)74,(byte)76,(byte)76,(byte)79,(byte)79,
           (byte)81,(byte)81,(byte)82,(byte)82,(byte)84,(byte)84,(byte)87,(byte)87,(byte)88,(byte)88,(byte)91,(byte)91,(byte)93,(byte)93,(byte)94,(byte)94,
           (byte)97,(byte)(byte)97,(byte)(byte)98,(byte)(byte)98,(byte)100,(byte)100,(byte)103,(byte)103,(byte)104,(byte)104,(byte)107,(byte)107,(byte)109,(byte)109,(byte)110,(byte)110,
           (byte)112,(byte)112,(byte)115,(byte)115,(byte)117,(byte)117,(byte)118,(byte)118,(byte)121,(byte)121,(byte)122,(byte)122,(byte)124,(byte)124,(byte)127,(byte)127,
           (byte)128,(byte)128,(byte)131,(byte)131,(byte)133,(byte)133,(byte)134,(byte)134,(byte)137,(byte)137,(byte)138,(byte)138,(byte)140,(byte)140,(byte)143,(byte)143,
           (byte)145,(byte)145,(byte)146,(byte)146,(byte)148,(byte)148,(byte)151,(byte)151,(byte)152,(byte)152,(byte)155,(byte)155,(byte)157,(byte)157,(byte)158,(byte)158,
           (byte)161,(byte)161,(byte)162,(byte)162,(byte)164,(byte)164,(byte)167,(byte)167,(byte)168,(byte)168,(byte)171,(byte)171,(byte)173,(byte)173,(byte)174,(byte)174,
           (byte)176,(byte)176,(byte)179,(byte)179,(byte)181,(byte)181,(byte)182,(byte)182,(byte)185,(byte)185,(byte)186,(byte)186,(byte)188,(byte)188,(byte)191,(byte)191,
           (byte)193,(byte)193,(byte)194,(byte)194,(byte)196,(byte)196,(byte)199,(byte)199,(byte)200,(byte)200,(byte)203,(byte)203,(byte)205,(byte)205,(byte)206,(byte)206,
           (byte)208,(byte)208,(byte)211,(byte)211,(byte)213,(byte)213,(byte)214,(byte)214,(byte)217,(byte)217,(byte)218,(byte)218,(byte)220,(byte)220,(byte)223,(byte)223,
           (byte)224,(byte)224,(byte)227,(byte)227,(byte)229,(byte)229,(byte)230,(byte)230,(byte)233,(byte)233,(byte)234,(byte)234,(byte)236,(byte)236,(byte)239,(byte)239,
           (byte)241,(byte)241,(byte)242,(byte)242,(byte)244,(byte)244,(byte)247,(byte)247,(byte)248,(byte)248,(byte)251,(byte)251,(byte)253,(byte)253,(byte)254,(byte)254};    
}
