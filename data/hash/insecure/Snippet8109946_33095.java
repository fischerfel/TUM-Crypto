private static final String ENDPOINT_URL = " http://localhost/sugarcrm/service/v3/soap.php";

java.net.URL url = null;
        try {
            url = new URL(ENDPOINT_URL);
        } catch (MalformedURLException e1) {
            System.out.println("URL endpoing creation failed. Message: "+e1.getMessage());
            e1.printStackTrace();   
        }

> System.out.println("URL endpoint created successfully!");
        Sugarsoap service = new SugarsoapLocator();
        SugarsoapPortType port = service.getsugarsoapPort(url);

        Get_server_info_result result = port.get_server_info();
        System.out.println(result.getGmt_time());
        System.out.println(result.getVersion());
        //I am getting right answers

        User_auth userAuth=new User_auth();
        userAuth.setUser_name(USER_NAME);
        MessageDigest md =MessageDigest.getInstance("MD5");
        String password=convertToHex(md.digest(USER_PASSWORD.getBytes()));
        userAuth.setPassword(password);

        Name_value nameValueListLogin[] = null;

        Entry_value loginResponse = null;
        loginResponse=port.login (userAuth, "sugarcrm",nameValueListLogin);

        String sessionID = loginResponse.getId(); // <--- Get error on this one
