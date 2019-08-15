public String calcHmac(String src) throws Exception {

        String key = "PFhNTD48VUlEPlNYWUFOR0VUPC9VSUQ+PEVUUkFOQVVUSD48WFNQUk9MRT5BbGw8L1hTUFJPTEU+PFhTUFNlY3VyaXR5SUQ+MzMzPC9YU1BTZWN1cml0eUlEPjxYU1BTZWN1cml0eUlEPjk5NzwvWFNQU2VjdXJpdHlJRD48WFNQU2VjdXJpdHlJRD5HQjwvWFNQU2VjdXJpdHlJRD48WFNQU2VjdXJpdHlJRD5YMDM8L1hTUFNlY3VyaXR5SUQ+PFhTUFNlY3VyaXR5SUQ+WDAxPC9YU1BTZWN1cml0eUlEPjxYU1BSZWNpcGllbnQ+NDlBQ0NULVNZQU5HRVRSQU48L1hTUFJlY2lwaWVudD48L0VUUkFOQVVUSD48L1hNTD4=";
        SecretKeySpec keySpec = new SecretKeySpec(
                key.getBytes(),
                "HmacMD5");

        Mac mac = Mac.getInstance("HmacMD5");
        mac.init(keySpec);
        byte[] result = mac.doFinal(src.getBytes());
        return Hex.encodeHexString(result);   
    }
