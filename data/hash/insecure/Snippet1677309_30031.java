public class EncryptedFileLoginModule implements LoginModule {

@Override
public void initialize(Subject subject, CallbackHandler callbackHandler,
        Map<String, ?> sharedState, Map<String, ?> options) {
    this.subject = subject;
    this.callbackHandler = callbackHandler;
}

public boolean login() throws LoginException {
    attemptLogin();
    if (username == null || password == null) {
        throw new LoginException("Either no username or no password specified");
    }
    MessageDigest instance = MessageDigest.getInstance("SHA-1");
    byte[] raw = new String(password).getBytes();
    byte[] crypted = instance.digest(raw);
    // TODO: Compare to the one stored locally
    if (!authenticated) throw new LoginException();
    return true;
}

private void attemptLogin() throws LoginException {
    Callback[] callbacks = new Callback[2];
    callbacks[0] = new NameCallback("username");
    callbacks[1] = new PasswordCallback("password", false);
        callbackHandler.handle(callbacks);
        username = ((NameCallback) callbacks[0]).getName();
        user = new JMXPrincipal(username);
        char[] tmpPassword = ((PasswordCallback) callbacks[1]).getPassword();
        password = new char[tmpPassword.length];
        System.arraycopy(tmpPassword, 0, password, 0, tmpPassword.length);
        ((PasswordCallback) callbacks[1]).clearPassword();
}
