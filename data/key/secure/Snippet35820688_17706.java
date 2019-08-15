public class EncryptAsync extends AsyncTask<Void, Void, Void> {
    //ProgressDialog progressDialog;

    //declare other objects as per your need
    @Override
    protected void onPreExecute() {
        //   progressDialog = ProgressDialog.show(EncryptFile.this, "Progress Dialog Title Text", "Process Description Text", true);


        if (password.getText().toString().equals(confirmPassword.getText().toString())) {

            correctPassword = password.getText().toString();
            //Toast.makeText(this,correctPassword,Toast.LENGTH_LONG).show();

            //copies Plain Text to String
            fileEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            returnFile = fileEditText.getText().toString();
            Toast.makeText(EncryptFile.this, returnFile, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(EncryptFile.this, "Passwords do not match", Toast.LENGTH_LONG).show();

        }

    }



    @Override
    protected Void doInBackground(Void... params) {


        if (spinnerValue.equals("AES")) {
            Toast.makeText(EncryptFile.this, returnFile, Toast.LENGTH_LONG).show();

            try {
                // Here you read the cleartext.
                FileInputStream fis = new FileInputStream(returnFile);
                // This stream write the encrypted text. This stream will be wrapped by another stream.
                FileOutputStream fos = new FileOutputStream(returnFile + ".aes");

                // hash password with SHA-256 and crop the output to 128-bit for key
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                digest.update(correctPassword.getBytes());

                // copys hashed password to key
                System.arraycopy(digest.digest(), 0, key, 0, key.length);


                SecretKeySpec sks = new SecretKeySpec(key, "AES");
                // Create cipher
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, sks);
                // Wrap the output stream
                CipherOutputStream cos = new CipherOutputStream(fos, cipher);
                // Write bytes
                int b;
                byte[] d = new byte[8];
                while ((b = fis.read(d)) != -1) {
                    cos.write(d, 0, b);
                }
                // Flush and close streams.
                cos.flush();
                cos.close();
                fis.close();
            } catch (Exception ex) {
                Toast.makeText(EncryptFile.this, "Error with Exception", Toast.LENGTH_LONG).show();
            } catch(Throwable t){
                Toast.makeText(EncryptFile.this, "Error with throwable", Toast.LENGTH_LONG).show();
            }

        } else if (spinnerValue.equals("Blowfish")) {
//code for blowfish
        }



         return null;
    }
@Override
    protected void onPostExecute(Void result) {
        Toast.makeText(EncryptFile.this, "Finished Encryption", Toast.LENGTH_LONG).show();
        // super.onPostExecute(result);
        // progressDialog.dismiss();
    }
