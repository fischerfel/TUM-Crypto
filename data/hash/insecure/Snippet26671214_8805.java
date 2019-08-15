import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 *
 * @author Chidi
 */
public class Auth {
    private final Firebase usersRef;
    private boolean isNewUser = false;
    private String uid = null;
    private HashMap<String, Object> userHash;

    public Auth() {
        Firebase rootRef = new Firebase("https://tranzchat.firebaseio.com/");
        this.usersRef = rootRef.child("users");  
        userHash = new HashMap<>();
    }

    public void createUser(final String email, final String password, String name, String default_lang) {
        userHash.put("email", email);
        userHash.put("password", this.hashPassword(password));
        userHash.put("name", name);
        userHash.put("default_lang", default_lang);
        Vars.dbcon.createUser(email, password, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                System.out.println("Created the new user");
                // Sets isNewUser to true after creating the new user
                isNewUser = true;
                signIn(email, password);
                if(uid != null) {
                    // Try to store use information
                    Firebase newUserRef = usersRef.child(uid);
                    newUserRef.setValue(userHash, new Firebase.CompletionListener(){
                        @Override
                        public void onComplete(FirebaseError fe, Firebase frbs) {
                            System.out.println("Completed Database Insertion");
                        }

                    });
                } else {
                    System.out.println("Could not retrieve UID");
                }
            }

            @Override
            public void onError(FirebaseError fe) {
                System.out.println("An error occured while creating the user.");
            }
        });
    }

    public void signIn(String email, String password) {
        Vars.dbcon.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                uid = authData.getUid();
                if(!isNewUser) {
                    // Try to get new user information and store it in the user object
                    usersRef.child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            userHash = (HashMap<String, Object>) snapshot.getValue();
                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            System.out.println("The read failed: " + firebaseError.getMessage());
                        }
                    });
                }
            }
            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // there was an error
            }
        });
    }
    public String hashPassword(String password) {
        String md5 = null;
        if(password == null) return null;
        try {
            // Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");
            // Update input string in message digest
            digest.update(password.getBytes(), 0, password.length());
            // Converts message digest value in base 16 (hex)
            md5 = new BigInteger(1, digest.digest()).toString();
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }

    // Test Suite
    public static void main(String[] args) throws InterruptedException {
        Vars.init();
        Thread firebaseAuth = new Thread(new Runnable() {
            @Override
            public void run() {
                Auth au = new Auth();
                au.createUser("chidiebere.nnadi@gmail.com", "password", "Chidiebere Nnadi", "en");
            }
        });
        firebaseAuth.start();
        firebaseAuth.join();
    }
}
