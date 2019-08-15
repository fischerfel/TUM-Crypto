import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.sun.jna.platform.win32.Crypt32Util;

public class Test {

    public static void main(String[] args) throws IOException {
        ChromeBrowser chrome = new ChromeBrowser();
        Set<Cookie> cookies = chrome.getCookiesForDomain("stackoverflow.com");
        for(Cookie cookie : cookies){
            System.out.println(cookie.toString());
        }
    }

    public static abstract class Cookie {

        protected String name;
        protected byte[] encryptedValue;
        protected Date expires;
        protected String path;
        protected String domain;
        protected boolean secure;
        protected boolean httpOnly;
        protected File cookieStore;

        public Cookie(String name, byte[] encryptedValue, Date expires, String path, String domain, boolean secure, boolean httpOnly, File cookieStore) {
            this.name = name;
            this.encryptedValue = encryptedValue;
            this.expires = expires;
            this.path = path;
            this.domain = domain;
            this.secure = secure;
            this.httpOnly = httpOnly;
            this.cookieStore = cookieStore;
        }

        public String getName() {
            return name;
        }

        public byte[] getEncryptedValue() {
            return encryptedValue;
        }

        public Date getExpires() {
            return expires;
        }

        public String getPath() {
            return path;
        }

        public String getDomain() {
            return domain;
        }

        public boolean isSecure() {
            return secure;
        }

        public boolean isHttpOnly() {
            return httpOnly;
        }

        public File getCookieStore(){
            return cookieStore;
        }

        public abstract boolean isDecrypted();

    }

    public static class DecryptedCookie extends Cookie {

        private String decryptedValue;

        public DecryptedCookie(String name, byte[] encryptedValue, String decryptedValue, Date expires, String path, String domain, boolean secure, boolean httpOnly, File cookieStore) {
            super(name, encryptedValue, expires, path, domain, secure, httpOnly, cookieStore);
            this.decryptedValue = decryptedValue;
        }

        public String getDecryptedValue(){
            return decryptedValue;
        }

        @Override
        public boolean isDecrypted() {
            return true;
        }

        @Override
        public String toString() {
            return "Cookie [name=" + name + ", value=" + decryptedValue + "]";
        }

    }

    public static class EncryptedCookie extends Cookie {

        public EncryptedCookie(String name, byte[] encryptedValue, Date expires, String path, String domain, boolean secure, boolean httpOnly, File cookieStore) {
            super(name, encryptedValue, expires, path, domain, secure, httpOnly, cookieStore);
        }

        @Override
        public boolean isDecrypted() {
            return false;
        }

        @Override
        public String toString() {
            return "Cookie [name=" + name + " (encrypted)]";
        }

    }

    public static class OS {

        public static String getOsArchitecture() {
            return System.getProperty("os.arch");
        }

        public static String getOperatingSystem() {
            return System.getProperty("os.name");
        }

        public static String getOperatingSystemVersion() {
            return System.getProperty("os.version");
        }

        public static String getIP() throws UnknownHostException {
            InetAddress ip = InetAddress.getLocalHost();
            return ip.getHostAddress();
        }

        public static String getHostname() throws UnknownHostException {
            return InetAddress.getLocalHost().getHostName(); 
        }

        public static boolean isWindows() {
            return (getOperatingSystem().toLowerCase().indexOf("win") >= 0);
        }

        public static boolean isMac() {
            return (getOperatingSystem().toLowerCase().indexOf("mac") >= 0);
        }

        public static boolean isLinux() {
            return (getOperatingSystem().toLowerCase().indexOf("nix") >= 0 || getOperatingSystem().toLowerCase().indexOf("nux") >= 0 || getOperatingSystem().toLowerCase().indexOf("aix") > 0 );
        }

        public static boolean isSolaris() {
            return (getOperatingSystem().toLowerCase().indexOf("sunos") >= 0);
        }

    }

    public static abstract class Browser {

        /**
         * A file that should be used to make a temporary copy of the browser's cookie store
         */
        protected File cookieStoreCopy = new File(".cookies.db");

        /**
         * Returns all cookies
         */
        public Set<Cookie> getCookies() {
            HashSet<Cookie> cookies = new HashSet<Cookie>();
            for(File cookieStore : getCookieStores()){
                cookies.addAll(processCookies(cookieStore, null));
            }
            return cookies;
        }

        /**
         * Returns cookies for a given domain
         */
        public Set<Cookie> getCookiesForDomain(String domain) {
            HashSet<Cookie> cookies = new HashSet<Cookie>();
            for(File cookieStore : getCookieStores()){
                cookies.addAll(processCookies(cookieStore, domain));
            }
            return cookies;
        }

        /**
         * Returns a set of cookie store locations
         * @return
         */
        protected abstract Set<File> getCookieStores();

        /**
         * Processes all cookies in the cookie store for a given domain or all
         * domains if domainFilter is null
         * 
         * @param cookieStore
         * @param domainFilter
         * @return
         */
        protected abstract Set<Cookie> processCookies(File cookieStore, String domainFilter);

        /**
         * Decrypts an encrypted cookie
         * @param encryptedCookie
         * @return
         */
        protected abstract DecryptedCookie decrypt(EncryptedCookie encryptedCookie);

    }

    /**
     * An implementation of Chrome cookie decryption logic for Mac, Windows, and Linux installs 
     * 
     * References: 
     * 1) http://n8henrie.com/2014/05/decrypt-chrome-cookies-with-python/
     * 2) https://github.com/markushuber/ssnoob
     * 
     * @author Ben Holland
     */
    public static class ChromeBrowser extends Browser {

        private String chromeKeyringPassword = null;

        /**
         * Returns a set of cookie store locations
         * @return
         */
        @Override
        protected Set<File> getCookieStores() {
            HashSet<File> cookieStores = new HashSet<File>();

            // pre Win7
            cookieStores.add(new File(System.getProperty("user.home") + "\\Application Data\\Google\\Chrome\\User Data\\Default\\Cookies"));

            // Win 7+
            cookieStores.add(new File(System.getProperty("user.home") + "\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Cookies"));

            // Mac
            cookieStores.add(new File(System.getProperty("user.home") + "/Library/Application Support/Google/Chrome/Default/Cookies"));

            // Linux
            cookieStores.add(new File(System.getProperty("user.home") + "/.config/chromium/Default/Cookies"));

            return cookieStores;
        }

        /**
         * Processes all cookies in the cookie store for a given domain or all
         * domains if domainFilter is null
         * 
         * @param cookieStore
         * @param domainFilter
         * @return
         */
        @Override
        protected Set<Cookie> processCookies(File cookieStore, String domainFilter) {
            HashSet<Cookie> cookies = new HashSet<Cookie>();
            if(cookieStore.exists()){
                Connection connection = null;
                try {
                    cookieStoreCopy.delete();
                    Files.copy(cookieStore.toPath(), cookieStoreCopy.toPath());
                    // load the sqlite-JDBC driver using the current class loader
                    Class.forName("org.sqlite.JDBC");
                    // create a database connection
                    connection = DriverManager.getConnection("jdbc:sqlite:" + cookieStoreCopy.getAbsolutePath());
                    Statement statement = connection.createStatement();
                    statement.setQueryTimeout(30); // set timeout to 30 seconds
                    ResultSet result = null;
                    if(domainFilter == null || domainFilter.isEmpty()){
                        result = statement.executeQuery("select * from cookies");
                    } else {
                        result = statement.executeQuery("select * from cookies where host_key like \"%" + domainFilter + "%\"");
                    }
                    while (result.next()) {
                        String name = result.getString("name");
                        byte[] encryptedBytes = result.getBytes("encrypted_value");
                        String path = result.getString("path");
                        String domain = result.getString("host_key");
                        boolean secure = result.getBoolean("secure");
                        boolean httpOnly = result.getBoolean("httponly");
                        Date expires = result.getDate("expires_utc");

                        EncryptedCookie encryptedCookie = new EncryptedCookie(name,
                                                                              encryptedBytes, 
                                                                              expires,
                                                                              path, 
                                                                              domain, 
                                                                              secure,
                                                                              httpOnly, 
                                                                              cookieStore);

                        DecryptedCookie decryptedCookie = decrypt(encryptedCookie);

                        if(decryptedCookie != null){
                            cookies.add(decryptedCookie);
                        } else {
                            cookies.add(encryptedCookie);
                        }
                        cookieStoreCopy.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // if the error message is "out of memory",
                    // it probably means no database file is found
                } finally {
                    try {
                        if (connection != null){
                            connection.close();
                        }
                    } catch (SQLException e) {
                        // connection close failed
                    }
                }
            }
            return cookies;
        }

        /**
         * Decrypts an encrypted cookie
         * @param encryptedCookie
         * @return
         */
        @Override
        protected DecryptedCookie decrypt(EncryptedCookie encryptedCookie) {
            byte[] decryptedBytes = null;
            if(OS.isWindows()){
                try {
                    decryptedBytes = Crypt32Util.cryptUnprotectData(encryptedCookie.getEncryptedValue());
                } catch (Exception e){
                    decryptedBytes = null;
                }
            } else if(OS.isLinux()){
                try {
                    byte[] salt = "saltysalt".getBytes();
                    char[] password = "peanuts".toCharArray();
                    char[] iv = new char[16];
                    Arrays.fill(iv, ' ');
                    int keyLength = 16;

                    int iterations = 1;

                    PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength * 8);
                    SecretKeyFactory pbkdf2 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

                    byte[] aesKey = pbkdf2.generateSecret(spec).getEncoded();

                    SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");

                    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(new String(iv).getBytes()));

                    // if cookies are encrypted "v10" is a the prefix (has to be removed before decryption)
                    byte[] encryptedBytes = encryptedCookie.getEncryptedValue();
                    if (new String(encryptedCookie.getEncryptedValue()).startsWith("v10")) {
                        encryptedBytes = Arrays.copyOfRange(encryptedBytes, 3, encryptedBytes.length);
                    }
                    decryptedBytes = cipher.doFinal(encryptedBytes);
                } catch (Exception e) {
                    decryptedBytes = null;
                }
            } else if(OS.isMac()){
                // access the decryption password from the keyring manager
                if(chromeKeyringPassword == null){
                    try {
                        chromeKeyringPassword = getMacKeyringPassword("Chrome Safe Storage");
                    } catch (IOException e) {
                        decryptedBytes = null;
                    }
                }
                try {
                    byte[] salt = "saltysalt".getBytes();
                    char[] password = chromeKeyringPassword.toCharArray();
                    char[] iv = new char[16];
                    Arrays.fill(iv, ' ');
                    int keyLength = 16;

                    int iterations = 1003;

                    PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength * 8);
                    SecretKeyFactory pbkdf2 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

                    byte[] aesKey = pbkdf2.generateSecret(spec).getEncoded();

                    SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");

                    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(new String(iv).getBytes()));

                    // if cookies are encrypted "v10" is a the prefix (has to be removed before decryption)
                    byte[] encryptedBytes = encryptedCookie.getEncryptedValue();
                    if (new String(encryptedCookie.getEncryptedValue()).startsWith("v10")) {
                        encryptedBytes = Arrays.copyOfRange(encryptedBytes, 3, encryptedBytes.length);
                    }
                    decryptedBytes = cipher.doFinal(encryptedBytes);
                } catch (Exception e) {
                    decryptedBytes = null;
                }
            }

            if(decryptedBytes == null){
                return null;
            } else {        
                return new DecryptedCookie(encryptedCookie.getName(),
                                           encryptedCookie.getEncryptedValue(),
                                           new String(decryptedBytes),
                                           encryptedCookie.getExpires(),
                                           encryptedCookie.getPath(),
                                           encryptedCookie.getDomain(),
                                           encryptedCookie.isSecure(),
                                           encryptedCookie.isHttpOnly(),
                                           encryptedCookie.getCookieStore());
            }
        }

        /**
         * Accesses the apple keyring to retrieve the Chrome decryption password
         * @param application
         * @return
         * @throws IOException
         */
        private static String getMacKeyringPassword(String application) throws IOException {
            Runtime rt = Runtime.getRuntime();
            String[] commands = {"security", "find-generic-password","-w", "-s", application};
            Process proc = rt.exec(commands);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String result = "";
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                result += s;
            }
            return result;
        }

    }

}
