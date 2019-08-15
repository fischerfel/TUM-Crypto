  public static String printHashKey(Context context, String packagename)
        {

            String TAG = packagename;
            try
            {
                Log.d(TAG, "keyHash: start");
                PackageInfo info = context.getPackageManager().getPackageInfo(TAG,PackageManager.GET_SIGNATURES);
                for (Signature signature: info.signatures)
                {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    String keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                    Log.d(TAG, "keyHash: " + keyHash);
                    return keyHash;
                }
                Log.d(TAG, "keyHash: end");
            }
            catch (NameNotFoundException e)
            {
                Log.d(TAG, "keyHash: name:"+e);
            }
            catch (NoSuchAlgorithmException e)
            {
                Log.d(TAG, "keyHash: name:"+e);
            }

            return "error";
        }

        public static void updateLanguage(Context context, String code)
        {
            Locale locale = new Locale(code);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
