public void connectToFb() throws XMPPException {

ConnectionConfiguration config = new ConnectionConfiguration("chat.facebook.com", 5222);
config.setSASLAuthenticationEnabled(true);
config.setSecurityMode(SecurityMode.required);
config.setRosterLoadedAtLogin(true);
config.setTruststorePath("/system/etc/security/cacerts.bks");
config.setTruststorePassword("changeit");
config.setTruststoreType("bks");
config.setSendPresence(false);
try {
    SSLContext sc = SSLContext.getInstance("TLS");
    sc.init(null, MemorizingTrustManager.getInstanceList(this), new java.security.SecureRandom());
    config.setCustomSSLContext(sc);
} catch (GeneralSecurityException e) {
    Log.w("TAG", "Unable to use MemorizingTrustManager", e);
}
XMPPConnection xmpp = new XMPPConnection(config);
try {
    xmpp.connect();
    xmpp.login("facebookusername", "****"); // Error on this line
    Roster roster = xmpp.getRoster();
    Collection<RosterEntry> entries = roster.getEntries();
    System.out.println("Connected!");
    System.out.println("\n\n" + entries.size() + " buddy(ies):");
    // shows first time onliners---->
    String temp[] = new String[50];
    int i = 0;
    for (RosterEntry entry : entries) {
        String user = entry.getUser();
        Log.i("TAG", user);
    }
} catch (XMPPException e) {
    xmpp.disconnect();
    e.printStackTrace();
}
}
