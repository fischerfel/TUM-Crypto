    //Mode 1

    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec keyspec = new PBEKeySpec("password".toCharArray(), salt, 1000, 128);
    Key key = factory.generateSecret(keyspec);
    System.out.println(key.getClass().getName());
    System.out.println(Arrays.toString(key.getEncoded()));

    //Mode 2

    PBEParametersGenerator generator = new PKCS5S2ParametersGenerator();
    generator.init(PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(("password").toCharArray()), salt, 1000);
    KeyParameter params = (KeyParameter)generator.generateDerivedParameters(128);
    System.out.println(Arrays.toString(params.getKey()));

    //Mode 3

    SecretKeyFactory factorybc = SecretKeyFactory.getInstance("PBEWITHHMACSHA1", "BC");
    KeySpec keyspecbc = new PBEKeySpec("password".toCharArray(), salt, 1000, 128);
    Key keybc = factorybc.generateSecret(keyspecbc);
    System.out.println(keybc.getClass().getName());
    System.out.println(Arrays.toString(keybc.getEncoded()));
    System.out.println(keybc.getAlgorithm());
