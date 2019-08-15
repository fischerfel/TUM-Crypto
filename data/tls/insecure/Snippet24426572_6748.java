public WhServer(int port, ChannelsManager manager) throws IOException {
    this.port = port;
    this.manager = manager;
    try {
        context = SSLContext.getInstance("SSL"); //pick the SSL protocol you need.
    } catch (Throwable t) { t.printStackTrace(); }

}
