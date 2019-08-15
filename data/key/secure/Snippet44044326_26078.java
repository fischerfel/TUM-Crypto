     Map<String, String> requestHeaders = new HashMap<>();
     requestHeaders.put("x-ms-version", "2014-02-14");
     requestHeaders.put("x-ms-date", getStorageDate());


    getAuthorizationHeader("PUT","storageaccount","storagekey",new URI("https://storageaccount.blob.core.windows.net/vhds/8447a36f-43a3-4927-a23f-b8839f4c55e3.vhd?comp=snapshot"),requestHeaders);
}

  public static String getStorageDate()
    {
        Date d = new Date(System.currentTimeMillis());
        SimpleDateFormat f = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        f.setTimeZone(TimeZone.getTimeZone("GMT"));
        String dateString = f.format(d);
        return dateString;
    }


  public static String getAuthorizationHeader(String verb, String storageAccount, String storageKey, URI resourceURI,
          Map<String, String> requestHeaders) throws Exception
    {
        String authorizationStringToHash = getAuthorizationStringToHash(verb, storageAccount, requestHeaders, resourceURI);
        String authorizationHash = hashString(storageKey, authorizationStringToHash);
        System.out.println("StorageAccoutName::" + storageAccount);
        System.out.println("Storage Key::" + storageKey);
        System.out.println("authorizationStringToHash::" + authorizationStringToHash);
        System.out.println("authorizationHash:: " + authorizationHash);
      /*  System.out.println(HttpClientFactory.getInstance()
                .createHeader("Authorization", "SharedKey " + storageAccount + ":" + authorizationHash).getValue());*/
       System.out.println("Authorization SharedKey " + storageAccount + ":" + authorizationHash);
       return "";
    }

    public static String getAuthorizationStringToHash(String verb, String storageAccount,  Map<String, String> headers,
            URI resourceURI) throws Exception
    {

        Map<String, String> headersToCanonicalize = new HashMap<String, String>();

        String contentEncoding = "";
        String contentLanguage = "";
        String contentLength = "";
        String contentMD5 = "";
        String contentType = "";
        String date = "";
        String ifModifiedSince = "";
        String ifMatch = "";
        String ifNoneMatch = "";
        String ifUnModifiedSince = "";
        String range = "";
        Set<String> keySet = headers.keySet();
        for (String header : keySet)
        {
            if ("content-encoding".equals(header))
            {
                contentEncoding = headers.get(header);
            }
            else if ("content-language".equals(header))
            {
                contentLanguage = headers.get(header);
            }
            else if ("content-length".equals(header))
            {
                contentLength = headers.get(header);
            }
            else if ("content-md5".equals(header))
            {
                contentType = headers.get(header);
            }
            else if ("content-type".equals(header))
            {
                contentType = headers.get(header);
            }
            else if ("date".equals(header))
            {
                date = "";
            }
            else if ("if-modified-since".equals(header))
            {
                ifModifiedSince =headers.get(header);
            }
            else if ("if-match".equals(header))
            {
                ifMatch = headers.get(header);
            }
            else if ("if-none-match".equals(header))
            {
                ifNoneMatch =headers.get(header);
            }
            else if ("if-unmodified-since".equals(header))
            {
                ifUnModifiedSince =headers.get(header);
            }
            else if ("range".equals(header))
            {
                range = headers.get(header);
            }
            else
            {
                headersToCanonicalize.put(header, headers.get(header));
            }
        }

        StringBuffer hashBuffer = new StringBuffer(verb);
        hashBuffer.append("\n");
        hashBuffer.append(contentEncoding);
        hashBuffer.append("\n");
        hashBuffer.append(contentLanguage);
        hashBuffer.append("\n");
        hashBuffer.append(contentLength);
        hashBuffer.append("\n");
        hashBuffer.append(contentMD5);
        hashBuffer.append("\n");
        hashBuffer.append(contentType);
        hashBuffer.append("\n");
        hashBuffer.append(date);
        hashBuffer.append("\n");
        hashBuffer.append(ifModifiedSince);
        hashBuffer.append("\n");
        hashBuffer.append(ifMatch);
        hashBuffer.append("\n");
        hashBuffer.append(ifNoneMatch);
        hashBuffer.append("\n");
        hashBuffer.append(ifUnModifiedSince);
        hashBuffer.append("\n");
        hashBuffer.append(range);
        hashBuffer.append("\n");

        List<String> headerNames = new ArrayList<String>(headersToCanonicalize.keySet());
        Collections.sort(headerNames);
        for (String headerName : headerNames)
        {
            hashBuffer.append(canonicalizeHeader(headerName,headersToCanonicalize.get(headerName)));
            hashBuffer.append("\n");
        }

        hashBuffer.append(canonicalizeResource(storageAccount, resourceURI.getPath(), resourceURI.getQuery()));

        return hashBuffer.toString();
    }

    public static String hashString(String key, String stringToHash) throws Exception
    {
        byte[] keyBytes = DatatypeConverter.parseBase64Binary(key);

        SecretKey myKey = new SecretKeySpec(keyBytes, "HMACSHA256");
        Mac mac = Mac.getInstance("HMACSHA256");
        mac.init(myKey);

        byte[] stringUTF8Bytes = stringToHash.getBytes("UTF-8");
        byte[] macBytes = mac.doFinal(stringUTF8Bytes);

        return DatatypeConverter.printBase64Binary(macBytes);
    }

    public static String canonicalizeHeader(String headerName,String headervalue)
    {
        return headerName + ":" + headervalue;
    }

    // uri must not start with /
    public static String canonicalizeResource(String account, String resourcePath, String query)
    {
        StringBuffer canonicalResourceBuffer = new StringBuffer("/");
        canonicalResourceBuffer.append(account);
        canonicalResourceBuffer.append(resourcePath);
        List<String> sortedQueryParams = getOrderedQueryParams(query);
        for (String queryParam : sortedQueryParams)
        {
            canonicalResourceBuffer.append("\n");
            canonicalResourceBuffer.append(queryParam);
        }
        System.out.println("Cannonical Resource "+canonicalResourceBuffer.toString());
        return canonicalResourceBuffer.toString();
    }

    public static List<String> getOrderedQueryParams(String queryParamsString)
    {
        Map<String, String> queryParams = new HashMap<String, String>();
        if ((queryParamsString == null) || (queryParamsString.isEmpty()))
        {
            return Collections.EMPTY_LIST;
        }

        String[] queryParamsArray = queryParamsString.split("&");
        if ((queryParamsArray == null) || (queryParamsArray.length == 0))
        {
            return Collections.EMPTY_LIST;
        }

        Arrays.sort(queryParamsArray);

        for (String queryParam : queryParamsArray)
        {
            queryParam = queryParam.trim();
            queryParam = queryParam.replaceFirst("[ \t]*=[ \t]*", "=");
            String[] parts = queryParam.split("=");
            if (parts.length == 2)
            {
                String key = parts[0];
                String value = parts[1];
                String currentValue = queryParams.get(key);
                if (currentValue == null)
                {
                    queryParams.put(key, value);
                }
                else
                {
                    queryParams.put(key, currentValue + "," + value);
                }
            }
        }

        List<String> names = new ArrayList(queryParams.keySet());
        Collections.sort(names);
        List<String> sortedCanonicalQueryParams = new ArrayList<String>();
        for (String name : names)
        {
            sortedCanonicalQueryParams.add(name + ":" + queryParams.get(name));
        }
        return sortedCanonicalQueryParams;
    }
