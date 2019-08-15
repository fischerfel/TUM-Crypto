in Java they are generating 'SecretKeySpec' like : 

private static final String ALGO = "AES";
    private static final byte[] keyValue = 
            new byte[] { 'a', '/', '5', '0', '0', '2', '*', 'l', '+', 'O', '&','@', 'b', '~', '_', '$' };

//Generate Secret Key
    private static 

 generateKey() throws Exception {
        SecretKeySpec key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }
