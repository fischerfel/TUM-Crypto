try {
               PackageInfo info = getPackageManager().getPackageInfo("com.key", PackageManager.GET_SIGNATURES);
               for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());

                      TextView tvmyName = (TextView)findViewById(R.id.KeyText);
                      tvmyName.setText(Base64.encodeBytes(md.digest()));
                      Log.d("KEY_HASH", Base64.encodeBytes(md.digest()));


               }
            } catch (NameNotFoundException e) {

            } catch (NoSuchAlgorithmException e) {

            }
