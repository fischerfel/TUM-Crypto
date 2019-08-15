    /**
     * This class abstracts the functionality of the OGONE direct link payment api making use of Springs {@link RestTemplate}
     * 
     * 
     * @author Kai Grabfelder (nospam@kaigrabfelder.de)
     *
     */
    public class DirectLinkTemplateImpl implements DirectLinkTemplate {

    private RestOperations restTemplate;

    private String shaPassphrase; 

    private String url;
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * affiliation name in ogone.
     */
    private String pspId;
    /**
     * Name of ogone application (API) user.
     */
    private String userId;
    /**
     * Password of the API user (USERID).
     */
    private String pswd;

    public RestOperations getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestOperations restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }



    /* (non-Javadoc)
     * @see DirectLinkTemplate#executeDirectLinkRequest(int, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void executeDirectLinkRequest(int amount, String currency,
            String orderId, String alias) {


        Map<String, String> request = new HashMap<String, String>();
        request.put("PSPID", getPspId());
        request.put("USERID", getUserId());
        request.put("PSWD", getPswd());
        request.put("AMOUNT", String.valueOf(amount));
        request.put("CURRENCY", currency);
        request.put("ORDERID", orderId);
        request.put("ALIAS", alias);
        request.put("ECI", "9"); //set the ECI parameter to 9 (recurring payment) - necessary according to ogone support

        request = cleanupRequestParameters(request);

        String shaSign = composeSHASIGNParameter(request);
        request.put("SHASIGN", shaSign);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String,String>>(createMultiValueMap(request), headers );


        String response = restTemplate.postForObject(getUrl(), entity, String.class);

        //TODO validate response
        System.out.println(response);



    }

    private MultiValueMap<String, String> createMultiValueMap(Map<String, String> map){
        map = new TreeMap<String, String>(map);
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<String, String>();
        Set<String> keys = map.keySet();
        for (String key : keys) {
            multiValueMap.add(key, map.get(key));
        }
        return multiValueMap;
    }


    /**
     * cleanup the request parameters by removing all parameters with an empty value and converting all parameter names to upper case
     * 
     * @param request
     * @return 
     */
    protected Map<String, String> cleanupRequestParameters(Map<String, String> request){

        Map<String, String> map = new HashMap<String, String>();

        Set<String> keys = request.keySet();
        for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            String value = request.get(key);
            if (StringUtils.hasText(value)) map.put(key.toUpperCase(), value);
        }

        return map ;
    }



    /**
     * create the SHASign parameter according to the specification of ogone: sorts all keys alphabetically,
     * @param request
     * @return
     */
    protected String composeSHASIGNParameter(Map<String, String> request){

        //create a map with alphabetically sorted keys
        TreeMap<String, String> sortedMap = new TreeMap<String, String>(request);

        Set<String> keys = sortedMap.keySet();
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(key).append("=").append(request.get(key));
            sb.append(getShaPassphrase());
        }

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] digest = md.digest(sb.toString().getBytes());

        String encodedString = new String(Hex.encodeHex(digest));
        return encodedString;
    }

    public String getShaPassphrase() {
        return shaPassphrase;
    }

    public void setShaPassphrase(String shaPassphrase) {
        this.shaPassphrase = shaPassphrase;
    }

    public String getPspId() {
        return pspId;
    }

    public void setPspId(String pspId) {
        this.pspId = pspId;
    }

}
