Button getKeyHash = (Button) findViewById(R.id.button_key_hash);
    getKeyHash.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Key Hash
            try {
                PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(),
                        PackageManager.GET_SIGNATURES);
                for (Signature signature : packageInfo.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            } catch (PackageManager.NameNotFoundException e1) {
                Log.e("Name not found", e1.toString());
            } catch (NoSuchAlgorithmException e) {
                Log.e("No such an algorithm", e.toString());
            } catch (Exception e) {
                Log.e("Exception", e.toString());
            }
        }
    });
