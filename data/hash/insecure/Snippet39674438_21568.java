public class MyApplication extends MultiDexApplication {

public void onCreate(){
    super.onCreate();
    PrintKeyHash();

}

public void PrintKeyHash(){
    try{
        PackageInfo info = getPackageManager().getPackageInfo("com.bhunnu.nearveg", PackageManager.GET_SIGNATURES);
        for (Signature signature: info.signatures){
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.e("Your System KEYHASH : ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    }catch (PackageManager.NameNotFoundException e){

    }catch (NoSuchAlgorithmException e){

    }
}

@Override
protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
}

}
