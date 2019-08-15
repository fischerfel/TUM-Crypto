package com.phinder.phinder;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.phinder.phinder.utils.ApiServices;
import com.phinder.phinder.utils.Constants;
import com.phinder.phinder.utils.MyLinks;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity /*implements View.OnClickListener*/ {
    private final int REQUEST_READ_WRITE_PERMISSION = 3, REQUEST_CAMERA_PERMISSION = 4;
    CallbackManager callbackManager;
    AccessToken accessToken;
    Retrofit retrofit;
    ApiServices apiServices;
    AppCompatActivity activity;
    SharedPreferences.Editor editor;
    LoginButton loginButton;
    Toolbar toolbar;
    private String profileUri = "", id = "", name = "", email = "";
    private Constants constants;
    private MyLinks myLinks;
    private ProgressDialog progressDialog;
    private boolean isReadWritePermitted = false, isCameraPermitted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        facebookSDKInitialize();
        setContentView(R.layout.activity_login);

        Constants.loginSharedPreferences = getSharedPreferences(Constants.LoginPref, MODE_PRIVATE);
        activity = LoginActivity.this;
        myLinks = new MyLinks(activity);
        constants = new Constants();
        editor = Constants.loginSharedPreferences.edit();

        toolbar = (Toolbar) findViewById(R.id.toolbar_activity_login);
        setSupportActionBar(toolbar);


        printKeyHash();
        List<String> permission = new ArrayList<String>();
        permission.add("email");
        permission.add("public_profile");
        permission.add("user_location");
        permission.add("user_birthday");
        permission.add("user_friends");


        progressDialog = new ProgressDialog(activity);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();


        retrofit = new Retrofit.Builder()
                .baseUrl(myLinks.DEFAULT_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiServices = retrofit.create(ApiServices.class);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(permission);


        getLoginDetails(loginButton);


    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {

            isReadWritePermitted = true;

        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_WRITE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_READ_WRITE_PERMISSION) {
            if (permissions.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isReadWritePermitted = true;
                    getLoginDetails(loginButton);
                } else {
                    isReadWritePermitted = false;
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void facebookSDKInitialize() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    private void getLoginDetails(final LoginButton loginButton) {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                loginButton.setVisibility(View.INVISIBLE);
                progressDialog.show();
                Log.d("msg", "Facebook");
                accessToken = AccessToken.getCurrentAccessToken();

                SharedPreferences.Editor editor = Constants.loginSharedPreferences
                        .edit();
                editor.putString(constants.LoginId, accessToken.getUserId());

                editor.apply();

                Log.d("msg", "fbid : " + accessToken.getUserId());

                onSuccessFullLogIn(accessToken);

                GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(
                        loginResult.getAccessToken(),
                        //AccessToken.getCurrentAccessToken(),
                        "/me/friends",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                try {
                                    JSONArray rawName = response.getJSONObject().getJSONArray("data");
                                    intent.putExtra("jsondata", rawName.toString());
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                ).executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    public void onSuccessFullLogIn(final AccessToken accessToken) {

        Bundle params = new Bundle();
        params.putString("fields", "id,name,location,email,picture.type(large)");

        new GraphRequest(accessToken, "me", params, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                if (response != null) {

                    try {
                        Log.d("msg", "Fb data1 : " + response.toString() + "");

                        JSONObject data = response.getJSONObject();

                        Log.d("msg", "Fb data : " + data.toString() + "");
                        SharedPreferences.Editor editor = Constants.loginSharedPreferences
                                .edit();

                        if (data.has("picture")) {
                            profileUri = data.getJSONObject("picture")
                                    .getJSONObject("data")
                                    .getString("url");

                            if (profileUri.startsWith("http://")
                                    || profileUri
                                    .startsWith("https://")) {
                                profileUri = profileUri.replace(
                                        "http://", "").trim();
                                profileUri = profileUri.replace(
                                        "https://", "").trim();
                            }

                            editor.putString(constants.LoginImage, profileUri);
                        }
                        if (data.has("name")) {
                            editor.putString(constants.LoginName, data.getString("name"));
                        }
                        //if (data.has("email")) {
                        //    Log.d("email", data.getString("email"));
                        //    editor.putString(constants.LoginEmail, data.getString("email"));
                        //}
                        //if (data.has("location")) {
                        //    editor.putString("city", data
                        //            .getJSONObject("location")
                        //            .getString("name"));

                        //}

                        editor.putBoolean(constants.LoginStatus, true);

                        editor.apply();

                        LoginManager.getInstance().logOut();

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {

                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        progressDialog.dismiss();

//                        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
//                        progressDialog.show();
//                        Call<ResponseBody> call = apiServices.socialCall(Constants.loginSharedPreferences.getString(constants.LoginName, ""),
//                                Constants.loginSharedPreferences.getString(constants.LoginId, ""),
//                                Constants.loginSharedPreferences.getString(constants.LoginEmail, ""),
//                                Constants.loginSharedPreferences.getString(constants.LoginImage, "")
//                        );
//
//                        call.enqueue(new Callback<ResponseBody>() {
//                            @Override
//                            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                                try {
//
//                                    String result = response.body().string();
//
//                                    Log.d("msg", "Result : " + result);
//
//
//                                    JSONObject jsonObject = new JSONObject(result);
////                                    progressDialog.dismiss();
//
//
//                                    if (jsonObject != null && jsonObject.length() > 0) {
//
//                                        JSONArray jsonArray = jsonObject.getJSONArray("response");
//
//                                        Log.d("msg", "Json : " + jsonArray.toString());
//
//                                        if (jsonArray != null && jsonArray.length() > 0) {
//
//                                            for (int i = 0; i < jsonArray.length(); i++) {
//
//                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//
//                                                String status = jsonObject1.getString("status");
//                                                String id = jsonObject1.getString("id");
//
//                                                SharedPreferences.Editor editor = Constants.loginSharedPreferences.edit();
//                                                editor.putString(constants.UserId, id);
//                                                editor.apply();
//
//                                                if (status.equals("one") || status.equals("zero")) {
//                                                    startActivity(new Intent(activity, MainActivity.class));
//                                                    finish();
//
//                                                } else {
//                                                    Toast.makeText(activity, "Invalid login", Toast.LENGTH_LONG).show();
//                                                }
//                                            }
//                                        }
//                                    }
//
//                                } catch (IOException e) {
//                                    e.getStackTrace();
//
//                                    Log.d("msg", "IO " + e.getMessage());
//                                    progressDialog.dismiss();
//                                } catch (JSONException e) {
//
//                                    e.printStackTrace();
//                                    Log.d("msg", "JSON " + e.getMessage());
//                                    progressDialog.dismiss();
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                                Log.d("msg", "JSON " + t.getMessage());
//
//                                progressDialog.dismiss();
//                            }
//                        });

                    }
                }
            }
        }).executeAsync();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Log.d("data",data.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(LoginActivity.this, "369622066719726");
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(LoginActivity.this, "369622066719726");
    }

    public String printKeyHash() {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.d("Package Name=", getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.d("Key Hash=", key);

//                ST4ot689mPBUodJzQxMIutXIkKw=
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.d("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }

        return key;
    }


}
