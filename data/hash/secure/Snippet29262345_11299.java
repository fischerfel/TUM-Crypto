    Random generator = new Random();
    Boolean found = false;
    int i;
    String test="";
    int whatIWant = 169;


    while(found == false)
    {

        String x = "";
        i = 0;

    while(i<15000)
    {   //String x = "";


        int y = generator.nextInt(220)+20;
        x = x + Integer.toHexString(y);
        i++;
    }
    byte[] hexMessage = DatatypeConverter.parseHexBinary(x);
    MessageDigest cript = MessageDigest.getInstance("SHA-512");
    cript.reset();
    cript.update(hexMessage);
    byte[] hash = cript.digest();

    test = String.format("%02X ", hash[0]);

    if(test.equalsIgnoreCase(Integer.toHexString(whatIWant).toString()))
        found = true;
