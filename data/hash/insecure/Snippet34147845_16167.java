package com.example.administrator.mosbeau;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.content.pm.Signature;

import com.facebook.appevents.AppEventsLogger;

@SuppressWarnings("deprecation")

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #//restoreActionBar()}.
     */

    private CharSequence mTitle;

    UserLocalStore userLocalStore;


    String customersid, countrycode, carttotal, stateid;

    SearchView searchView;

    RelativeLayout notifCount;
    TextView tv;

    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://shop.mosbeau.com.ph/android/";

    String myJSONcarttotal;
    JSONArray jsonarraycarttotal;

    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Connectivity connectivity=new Connectivity();
        if(connectivity.isConnected(MainActivity.this)) {

            // Add code to print out the key hash
            /*try {
                PackageInfo info = getPackageManager().getPackageInfo(
                        "com.administrator.mosbeau",
                        PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            } catch (PackageManager.NameNotFoundException e) {

            } catch (NoSuchAlgorithmException e) {

            }*/

        }else{
            nointernet();
        }


        userLocalStore = new UserLocalStore(this);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    public void onStart() {
        super.onStart();
        if(authenticate() == true){
            displayUserDetails();
        }else{
            Intent myIntent = new Intent(MainActivity.this, IndexActivity.class);
            startActivity(myIntent);
        }
    }

    private boolean authenticate() {
        if (userLocalStore.getLoggedInUser() == null) {
            Intent myIntent = new Intent(MainActivity.this, IndexActivity.class);
            startActivity(myIntent);
            return false;
        }
        return true;
    }

    private void displayUserDetails(){
        User user = userLocalStore.getLoggedInUser();

        customersid = user.customers_id;
        countrycode = user.customers_countryid;
        stateid = user.customers_stateid;

        if(customersid==""){
            Intent myIntent = new Intent(MainActivity.this, IndexActivity.class);
            startActivity(myIntent);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position,String id,String name,String image2) {
        // update the main content by replacing fragments

        FragmentManager fragmentManager = getFragmentManager();

        // NEW STUFF
        if(position == 0){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, HomeFragment.newInstance())
                    .commit();
        }
        else if (position == 1){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, CategoryFragment.newInstance(id,name,countrycode,image2,customersid,stateid))
                    .commit();
        }
        else if (position == 2){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, AccountFragment.newInstance(customersid))
                    .commit();
        }
        else if (position == 3){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ReferFragment.newInstance(customersid))
                    .commit();
        }
        else if (position == 4){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, AboutFragment.newInstance(customersid))
                    .commit();
        }
        else if (position == 5){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PolicyFragment.newInstance(customersid))
                    .commit();
        }
        else if (position == 6){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, TermsFragment.newInstance(customersid))
                    .commit();
        }
        else if (position == 7){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ContactusFragment.newInstance(customersid))
                    .commit();
        }
        else if (position == 8){
            userLocalStore.clearUserData();
            userLocalStore.setUserLoggedIn(false);
            Intent myIntent = new Intent(this, IndexActivity.class);
            startActivity(myIntent);
        }
        else if (position == 101){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, CartFragment.newInstance(customersid,countrycode,stateid,"a"))
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            //restoreActionBar();

            // HERE RETURNS NULL
            searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
            ImageView v = (ImageView) searchView.findViewById(searchImgId);
            v.setImageResource(R.drawable.action_searchm);
            if (searchView != null) {
                final Menu menu_block = menu;
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // collapse the view ?
                        //menu_block.findItem(R.id.action_search).collapseActionView();
                        SearchFragment searchFragment = SearchFragment.newInstance(query,countrycode,customersid,stateid);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, searchFragment)
                                .addToBackStack(null)
                                .commit();
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        // search goes here !!
                        // listAdapter.getFilter().filter(query);
                        return false;
                    }
                });
                //Log.i("sales module", "SearchView QWE");

            }else{
                //Log.i("sales module", "SearchView is null");
            }


            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

    }

    /*@Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }*/

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            Fragment fragment = getFragmentManager().findFragmentById(R.id.textHomeBack);
            if (fragment instanceof HomeFragment) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
            else {
                super.onBackPressed();
            }
        }
    }

    public void nointernet(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("There seems to be a problem with your connection.");
        dialogBuilder.setNegativeButton("Edit Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Stop the activity
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }

        });
        dialogBuilder.setPositiveButton("Reload", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Stop the activity
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }

        });
        AlertDialog dialog = dialogBuilder.show();
        TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

}
