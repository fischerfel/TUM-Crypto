SSLContext ctx=null;
try {
    ctx = SSLContext.getInstance("SSL");
} catch (NoSuchAlgorithmException e1) {
    e1.printStackTrace();
}
ClientConfig config=new DefaultClientConfig();
config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(null,ctx));
WebResource service=Client.create(new DefaultClientConfig()).resource("https://localhost:9999/");

//Attempt to view the user's page.
try{
    service
        .path("user/"+username)
        .get(String.class);
}
