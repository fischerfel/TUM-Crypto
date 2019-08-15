    package p41.android.details;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import p41.android.facebookfeed.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;

public class ResultActivityList extends FragmentActivity implements
        OnItemClickListener, OnClickListener {
    private Fragment fragment;
    ArrayList<String> url = new ArrayList<String>();
    ArrayList<String> text1 = new ArrayList<String>();
    ArrayList<String> text2 = new ArrayList<String>();
    ArrayList<String> text3 = new ArrayList<String>();
    ArrayList<String> text4 = new ArrayList<String>();
    ListView list;
    String key = "";
    AlertDialog dialog;

    Button post, cancel, sampleM;
    View view;
    Bitmap image;
    Bundle bundle;
    String link, name, caption, picture;
    String icon, description;
    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {

        @Override
        public void call(Session session, SessionState state,
                Exception exception) {
            // TODO Auto-generated method stub

            onSessionStateChange(session, state, exception);
        }
    };

    MenuItem settings;
    private boolean isResumed;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_list_activity);
        //temp
         try {
                PackageInfo info = getPackageManager().getPackageInfo(
                        "com.facebook.samples.hellofacebook", 
                        PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                    }
            } catch (NameNotFoundException e) {

            } catch (NoSuchAlgorithmException e) {

            }
         //
        uiHelper = new UiLifecycleHelper(this, callback);

        FragmentManager fm = getSupportFragmentManager();
        fragment = (Fragment) fm.findFragmentById(R.id.settings);

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.hide(fragment);
        transaction.commit();

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.custom_dialog, null);

        post = (Button) view.findViewById(R.id.post);
        post.setText("Post");
        post.setOnClickListener(this);

        cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setText("Cancel");
        cancel.setOnClickListener(this);

        sampleM = (Button) view.findViewById(R.id.sampleM);
        sampleM.setText("Sample Music");
        sampleM.setOnClickListener(this);

        list = (ListView) findViewById(R.id.list);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        key = bundle.getString("Key");
        Log.d("Key", key);

        Log.d("Key", SearchContent.selectionType);
        if (SearchContent.selectionType.equals("albums")) {
            ResultAdapter adapter = new ResultAdapter(this, SearchContent.name,
                    SearchContent.artist, SearchContent.genre,
                    SearchContent.year, SearchContent.cover);

            list.setAdapter(adapter);
        }
        if (SearchContent.selectionType.equals("artists")) {
            ResultAdapter1 adapter = new ResultAdapter1(this,
                    SearchContent.name, SearchContent.genre,
                    SearchContent.year, SearchContent.cover);
            list.setAdapter(adapter);
        }
        if (SearchContent.selectionType.equals("songs")) {
            ResultAdapter1 adapter = new ResultAdapter1(this,
                    SearchContent.name, SearchContent.performer,
                    SearchContent.composer, SearchContent.cover);
            list.setAdapter(adapter);
        }

        list.setOnItemClickListener(this);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
        uiHelper.onActivityResult(arg0, arg1, arg2);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        uiHelper.onPause();
        isResumed = false;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        uiHelper.onResume();
        isResumed = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.equals(settings)) {
            showFragment();
        }
        return true;
    }

    public void showFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        trans.show(fragment);
        trans.addToBackStack(null);
        trans.commit();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub

        Session session = Session.getActiveSession();
        if (session.isOpened()) {
            settings = menu.add("Settings");
            return true;
        }
        menu.clear();
        settings = null;
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
        // TODO Auto-generated method stub
        bundle = new Bundle();
        AlertDialog.Builder builder = null;
        builder = new AlertDialog.Builder(this);
        /*
         * For Artists: “artist_name I like artist_name who is active since
         * year. Genre of Music is: genre. Look at details: here”
         * 
         * 
         * For Albums: “album_name I like album_name released in year. Artist:
         * artist Genre : genre Look at details: here”
         * 
         * 
         * For Songs: “song_title I like song_title composed by composer
         * Performer: performer Look at details: here”
         */
        builder.setTitle("Post to Facebook");

        if (SearchContent.selectionType.equals("albums")) {
            Log.d("Location Albums Details",
                    "Title : " + SearchContent.name.get(pos) + " \nArtists: "
                            + SearchContent.artist.get(pos) + " \nGenre: "
                            + SearchContent.genre.get(pos) + " \nYear: "
                            + SearchContent.year.get(pos) + " \nDetails: "
                            + SearchContent.details.get(pos) + " \nCover: "
                            + SearchContent.cover.get(pos));
            /*
             * name = SearchContent.name.get(pos); caption =
             * SearchContent.genre.get(pos); link =
             * SearchContent.details.get(pos); picture =
             * SearchContent.year.get(pos);
             */
            icon = SearchContent.cover.get(pos);
            description = "" + SearchContent.name.get(pos) + "\nI like "
                    + SearchContent.name.get(pos) + " \nreleased in "
                    + SearchContent.year.get(pos) + " \nArtist: "
                    + SearchContent.artist.get(pos) + "\nGenre of Music is:"
                    + SearchContent.genre.get(pos) + "\nLook at details: "
                    + SearchContent.details.get(pos);
        }
        if (SearchContent.selectionType.equals("artists")) {
            Log.d("Location Artists Details",
                    "Title : " + SearchContent.name.get(pos) + " \nGenre: "
                            + SearchContent.genre.get(pos) + " \nYear: "
                            + SearchContent.year.get(pos) + " \nDetails: "
                            + SearchContent.details.get(pos) + "\nCover: "
                            + SearchContent.cover.get(pos));

            /*
             * name = SearchContent.name.get(pos); caption =
             * SearchContent.genre.get(pos); link =
             * SearchContent.details.get(pos); picture =
             * SearchContent.year.get(pos);
             */

            icon = SearchContent.cover.get(pos);
            description = "" + SearchContent.name.get(pos) + "\nI like "
                    + SearchContent.name.get(pos) + " who is active since "
                    + SearchContent.year.get(pos) + "."
                    + "\nGenre of Music is:" + SearchContent.genre.get(pos)
                    + "\nLook at details: " + SearchContent.details.get(pos);
        }
        if (SearchContent.selectionType.equals("songs")) {
            Log.d("Location Artists Details",
                    "Title : " + SearchContent.name.get(pos) + " \nPerformer: "
                            + SearchContent.performer.get(pos)
                            + " \nComposer: " + SearchContent.composer.get(pos)
                            + " \nDetails: " + SearchContent.details.get(pos)
                            + "\nCover: " + SearchContent.cover.get(pos)
                            + " \nSongs Present: "
                            + SearchContent.songPresent.get(pos)
                            + "\nSong Link: " + SearchContent.songLink.get(pos));
            /*
             * sampleM.setVisibility(View.VISIBLE); name =
             * SearchContent.name.get(pos); caption =
             * SearchContent.performer.get(pos); link =
             * SearchContent.songLink.get(pos); picture =
             * SearchContent.year.get(pos);
             */
            icon = SearchContent.cover.get(pos);
            description = "" + SearchContent.songPresent.get(pos) + "\nI like "
                    + SearchContent.name.get(pos) + " composed by "
                    + SearchContent.composer.get(pos) + "." + "\nPerformer: "
                    + SearchContent.performer.get(pos) + "\nLook at details: "
                    + SearchContent.details.get(pos);
            sampleM.setVisibility(View.VISIBLE);
        }

        builder.setView(view);
        if (dialog == null) {
            dialog = builder.create();
        }
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        /*
         * Bundle parameters = new Bundle(); parameters.putString("app_id",
         * "xxxxxxx"); parameters.putString("link",
         * "https://play.google.com/store/apps/details?id=myappistasty");
         * parameters.putString("name",
         * "This is the name of the link set in app.");
         * parameters.putString("caption",
         * "This is Text that is specified in bt the aoo");
         * parameters.putString("picture", "www.urltoimage.com);
         * facebook.dialog(MainActivity.this, "feed", parameters, new
         * DialogListener() { etc...
         */
        /*
         * postParams.putString("message", "XXX");
         * postParams.putString("caption", "developers.facebook.com");
         * postParams.putString("description",
         * "A tool to help you learn and browse the Facebook Graph API.");
         * postParams.putString("actions", "[{ 'name':'Test a simple Graph API
         * call!',
         * 'link':'https://developers.facebook.com/tools/explorer?method=GET&path=me'
         * ^ This link must direct to the application's connect or canvas URL.
         * You'll get an error otherwise. }]" ); postParams.putString("type",
         * "photo"); postParams.putString("link",
         * "https://developers.facebook.com/tools/explorer/");
         * postParams.putString("picture",
         * "http://blog.programmableweb.com/wp-content/ishot-44.png");
         * 
         * Request request = new Request(Session.getActiveSession(), "me/feed",
         * postParams, HttpMethod.POST);
         */

        if (v.getId() == R.id.cancel) {
            ResultActivityList.this.dialog.dismiss();
        }
        if (v.getId() == R.id.post) {
            image = BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_launcher);

            /*
             * Session.openActiveSession(this, true, new
             * Session.StatusCallback() {
             * 
             * // callback when session changes state
             * 
             * @Override public void call(Session session, SessionState state,
             * Exception exception) { if (session.isOpened()) {
             * 
             * // make request to the /me API
             * 
             * Request request = Request.newUploadPhotoRequest(
             * Session.getActiveSession(), image, new Request.Callback() {
             * 
             * @Override public void onCompleted(Response response) { // TODO
             * Auto-generated method stub new AlertDialog.Builder(
             * ResultActivityList.this) .setTitle("Post")
             * .setMessage("Success.....!") .setPositiveButton(R.string.ok,
             * null).show(); } }); bundle = request.getParameters();
             * bundle.putString("icon", icon); bundle.putString("description",
             * description); request.setParameters(bundle);
             * request.executeAsync();
             * 
             * ResultActivityList.this.dialog.dismiss(); } } });
             */

            Session.openActiveSession(this, true, new StatusCallback() {

                @Override
                public void call(Session session, SessionState state,
                        Exception exception) {
                    // TODO Auto-generated method stub
                    Request request = Request.newStatusUpdateRequest(
                            Session.getActiveSession(), description,
                            new Request.Callback() {

                                @Override
                                public void onCompleted(Response response) {
                                    // TODO Auto-generated method stub

                                    showPublishResult("Post", response.getGraphObject(), response.getError());
                                }
                            });
                    bundle = request.getParameters();
                    //bundle.putString("icon", icon);
                    bundle.putString("link", icon);
                    request.setParameters(bundle);
                    request.executeAsync();
                    ResultActivityList.this.dialog.dismiss();
                }
            });

        }

    }

    private void onSessionStateChange(Session session, SessionState state,
            Exception exception) {

        if (isResumed) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            int backStackSize = fm.getBackStackEntryCount();
            for (int i = 0; i < backStackSize; i++) {
                fm.popBackStack();
            }
            transaction.commit();
        }
    }

    public void showPublishResult(String message, GraphObject result,
            FacebookRequestError error) {
        String title = null;
        String alertMessage = null;
        if (error == null) {
            title = getString(R.string.success);
            String id = result.cast(GraphObjectWithId.class).getId();
            alertMessage = getString(R.string.post_successful, message, id);
        } else {
            title = getString(R.string.error);
            alertMessage = error.getErrorMessage();
        }

        new AlertDialog.Builder(ResultActivityList.this).setTitle(title).setMessage(alertMessage)
                .setPositiveButton(R.string.ok, null).show();
    }
    private interface GraphObjectWithId extends GraphObject {
        String getId();
    }
}
