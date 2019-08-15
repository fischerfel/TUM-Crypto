    String secret = "secret";  //config file, do not store in code
    Set existingMdBase64Alt = new HashSet(Arrays.asList("OouxiB5STt_v5nr_", "ouxiB5STt_v5nr_f"));
    String ipWithSecret = secret + "123.123.123.123";
    byte[] ipBytes = ipWithSecret.getBytes(StandardCharsets.UTF_8);
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] mdBytes = md.digest(ipBytes);
    String mdBase64 = Base64.encodeBase64String(mdBytes);
    String mdBase64Alt = mdBase64.replace("+","_").replace("/","_" );
    System.out.println("Debug: " + mdBase64Alt.substring(0, 16));
    while (mdBase64Alt.length() > 16 && existingMdBase64Alt.contains(mdBase64Alt.substring(0, 16))){
        mdBase64Alt = mdBase64Alt.substring(1);
        System.out.println("Debug: " + mdBase64Alt.substring(0, 16));
    }
    System.out.println("Final: " + mdBase64Alt.substring(0, 16));
