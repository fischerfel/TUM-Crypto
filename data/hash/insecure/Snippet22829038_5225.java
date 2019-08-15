private PackageInfo INFO = null; //global declaration
    public void generateHashKeyForFacebook(Context context) throws Exception {
            try {
                INFO = context.getPackageManager().getPackageInfo("com.bito1.Shoplu", PackageManager.GET_SIGNATURES);
                if (INFO == null) {
                    Toast.makeText(context.getApplicationContext(), "Invalid Package Name / Package not found", Toast.LENGTH_LONG).show();
                    return;
                }
                for (Signature signature : INFO.signatures) {
                    MessageDigest _md = MessageDigest.getInstance("SHA");
                    _md.update(signature.toByteArray());
                    Log.d("KeyHash: =>", Base64.encodeToString(_md.digest(), Base64.DEFAULT));
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
