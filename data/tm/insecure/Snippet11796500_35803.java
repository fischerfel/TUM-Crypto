public static void addEventToExchange(String login, String password,
        TaskDTO task) {
    // TODO: iommi - finish
    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
        }
    } };
    // Install the all-trusting trust manager
    try {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (Exception e) {
        e.printStackTrace();
    }
    String sExchangeServerUrl = "http://url.which.pings.com";
    // Account name of an exchange user that has permission to see and modify
    // other user's mailboxes.
    String sApplicationUserAccountName = "login";
    String sApplicationUserPassword = "p@ssw0rd";

    // Connect to exchange.
    ExchangeBridge exchangeBridge = new ExchangeBridge(sExchangeServerUrl,
            sApplicationUserAccountName, sApplicationUserPassword);
    HashMap out = new HashMap();
    Vector ret = null;
    try {
        String mailbox = "iommi";
        String folder = ExchangeConstants.k_sInboxName;
        long lSince_TimeInMillisecs = System.currentTimeMillis()
                - (1000 * 60 * 60 * 24);
        /*
         * ret = exchangeBridge.getEmails(mailbox, folder, lSince_TimeInMillisecs,
         * null, null, 100);
         */
        java.util.Calendar c = java.util.Calendar.getInstance();

        ExchangeEvent a_exEvent = task.getDTOasExchangeEvent();
        // a_exEvent.setBusyStatus(ExchangeEvent.);
        // a_exEvent.setStartDate(c.getTime());
        // c.add(java.util.Calendar.HOUR, 1);
        // a_exEvent.setEndDate(c.getTime());
        // a_exEvent.setSubject(subject);
        // a_exEvent.setDescription(decription);
        // a_exEvent.setUniqueIdForUrl("i" + c.getTimeInMillis());
        String a_sPrefix = ExchangeConstants.k_sExchangeName;
        exchangeBridge.updateEvent(a_sPrefix, "iommi",
                ExchangeConstants.k_sCalendarName, a_exEvent);
        exchangeBridge.getEvents(a_sPrefix, "iommi",
                ExchangeConstants.k_sCalendarName);
    } catch (ExchangeException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    // exchangeBridge.getContacts(a_sPrefix, a_sMailboxName, a_sFolderName);
    // exchangeBridge.getEmails(a_sMailboxName, a_sFolderName,
    // a_lDateReceived, a_hmFromEmailFilter, a_hmToEmailFilter, a_iMaxAtATime);
}
