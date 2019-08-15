// load KeyStore

static byte[] salt = // for learning purposes
                     { (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
                       (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99 };

String alias = "aeskey";
char[] password = "password".toCharArray();
SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
SecretKey tmp = factory.generateSecret(spec);
SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES/CBC/PKCS5Padding");
Entry aesentry = new KeyStore.SecretKeyEntry(secret);
store.setEntry(alias, aesentry, protParam);
