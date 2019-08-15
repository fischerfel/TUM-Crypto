/**
 * Initialize SSL
 * @param mContext
 */
public static void initializeSSLContext(Context mContext){
    try {
        SSLContext.getInstance("TLSv1.2");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    try {
        ProviderInstaller.installIfNeeded(mContext.getApplicationContext());
    } catch (GooglePlayServicesRepairableException e) {
        e.printStackTrace();
    } catch (GooglePlayServicesNotAvailableException e) {
        e.printStackTrace();
    }
}
