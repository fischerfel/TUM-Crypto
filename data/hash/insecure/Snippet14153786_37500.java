public class updateDemocracyService extends IntentService{
    public updateDemocracyService() {
        super("updateDemocracyService");
        // TODO Auto-generated constructor stub
    }
    private pollDataSource datasource;
     int mStartMode;       // indicates how to behave if the service is killed
        IBinder mBinder;      // interface for clients that bind
        boolean mAllowRebind; // indicates whether onRebind should be used
        // url to make request
        AccountManager am = AccountManager.get(this); // "this" references the current Context

        //cerco un account per identificare l'utente.
        Account[] accounts = am.getAccountsByType("com.google");
        String nomeaccount = md5(accounts[0].name);


        private static String url = "http://lnx.ggservice.com/democracy/domande.php?user=" ;
        private static String risposteUrl = "http://lnx.ggservice.com/democracy/domande.php?useranswer=" ;

        // JSON Node names
        private static final String TAG_DOMANDE = "domande";
        private static final String TAG_ID = "idWeb";
        private static final String TAG_CATEGORIA = "categoria";
        private static final String TAG_TESTODOMANDA = "testoDomanda";
        private static final String TAG_QUANTERISPOSTE = "quanteRisposte";
        private static final String TAG_TESTORISPOSTA = "testoRisposta";
        private static final String TAG_OK = "ok";


        // contacts JSONArray
        JSONArray domanda = null;
        JSONArray rispostescritte = null;


        @SuppressWarnings("deprecation")
        @Override
        protected void onHandleIntent(Intent intent) {
            datasource = new pollDataSource(this);
            datasource.open();


            // Creating JSON Parser instance
            JSONParser jParser = new JSONParser();

            // getting JSON string from URL
            JSONObject json = jParser.getJSONFromUrl(url + nomeaccount);


            try {

                // Getting Array of Contacts
                domanda = json.getJSONArray(TAG_DOMANDE);

                // looping through All Contacts
                for(int i = 0; i < domanda.length(); i++){
                    JSONObject c = domanda.getJSONObject(i);

                    // Storing each json item in variable
                    String id = c.getString(TAG_ID);
                    String testoDomanda = c.getString(TAG_TESTODOMANDA);
                    String categoria = c.getString(TAG_CATEGORIA);
                    int quanteRisposte = c.getInt(TAG_QUANTERISPOSTE);


                    datasource.createCategoria(categoria);
                    //TEOTODO inserimento della categoria, se necessario
                    //TEOTODO inserimento della domanda



                    for(int ua = 0; ua < quanteRisposte; ua++){
                    //TEOTODO inserimento dei testi risposte.   

                    }


                    //NOTIFICATION
                   NotificationManager notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);  

                   Notification note = new Notification(R.drawable.ic_launcher, "Democracy", System.currentTimeMillis());  

                   PendingIntent intent1 = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);  

                   note.setLatestEventInfo(this, "Nuovi sondaggi", "Ci sono nuovi sondaggi per te!!", intent1);  

                   int NOTIF_ID = 1;
                   notifManager.notify(NOTIF_ID, note);            
                   //NOTIFICATION    
                }
            } catch (JSONException e) {
              //  e.printStackTrace();
            }  

            //recupero tutte le risposte dal database, poi per ogni risposta:
            //JSONObject json1 = jParser.getJSONFromUrl(risposteUrl + nomeaccount); //+idweb+risposta

          //  try {

                // Getting Array of responses
               // rispostescritte = json.getJSONArray(TAG_OK);
          //      }   catch (JSONException e) {} 







             Intent i= new Intent(); 
             i.setAction("AWESOME_TRIGGER_WORD"); //the name which we will specify in our receiver 
             i.putExtra("counter",234); PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),
             1111, i,PendingIntent.FLAG_CANCEL_CURRENT);
             //getting current time and add 5 seconds in it 
              Calendar cal = Calendar.getInstance(); 
              cal.add(Calendar.MINUTE, 10); 
              //registering our pending intent with alarmmanager 
              AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE); 
              am.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(), pi);       


        }

        public String md5(String s) {
            try {
                // Create MD5 Hash
                MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
                digest.update(s.getBytes());
                byte messageDigest[] = digest.digest();

                // Create Hex String
                StringBuffer hexString = new StringBuffer();
                for (int i=0; i<messageDigest.length; i++)
                    hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
                return hexString.toString();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return "";
        }


}
