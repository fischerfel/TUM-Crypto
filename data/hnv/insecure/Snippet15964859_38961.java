 public class ListViewAdapter extends ArrayAdapter {
private Filter filter;

public ListViewAdapter(Context context, int textViewResourceId) {
    super(context, textViewResourceId);
    // TODO Auto-generated constructor stub
}
@Override
public Filter getFilter()
{
    if (filter == null)
        filter = new PkmnNameFilter();

    return filter;
}
// url to make request
private static String url = "https://abc.net/api/api/employees/";
// JSON Node names
ArrayList<String> TAG_ID= new ArrayList<String>();
ArrayList<String> TAG_NAME= new ArrayList<String>();
ArrayList<String> TAG_EMAIL= new ArrayList<String>();
ArrayList<String> TAG_PHONE_MOBILE= new ArrayList<String>();
static Context context;
JSONArray employee = null;
String id,name,email,mobile;
ArrayList<Integer> close =new ArrayList<Integer>();
private Activity activity;
ViewHolder view;

//constructor
    public ListViewAdapter(Activity activity) 
    {
    super(context, 0);
    this.activity = activity;

    // Creating JSON Parser instance
    JSONParser jParser = new JSONParser();

    // getting JSON string from URL
    JSONObject json = jParser.getJSONFromUrl(url);



    try {
        // Getting Array of Employee
        employee = json.getJSONArray("Employee");

        // looping through All Employee
        for(int i = 0; i < employee.length(); i++)
        {
        JSONObject c = employee.getJSONObject(i);

            // Storing each json item in variable
            id = String.valueOf(c.getInt("Id"));
            name = c.getString("Name");
            email = c.getString("Email");
            mobile = c.getString("Mobile");

            //adding all get values into array
            if(name!="null"&&mobile!="null"){
            TAG_NAME.add(name);
            TAG_ID.add(id);
            TAG_EMAIL.add(email);
            TAG_PHONE_MOBILE.add(mobile);
            close.add(R.drawable.close);
            }


        }
    } catch (JSONException e) {
        e.printStackTrace();
    }



}

@Override
public int getCount() {
    // TODO Auto-generated method stub
    return TAG_NAME.size();
}

@Override
public Object getItem(int paramInt) {
    // TODO Auto-generated method stub
    return TAG_NAME.size();
}

@Override
public long getItemId(int paramInt) {
    // TODO Auto-generated method stub
    return 0;
}

public static class ViewHolder {
    public ImageView deleteButtonImg;
    public TextView name,email,mobile,id;



}

@Override
public View getView(final int paramInt, View paramView, final ViewGroup paramViewGroup) {
    // TODO Auto-generated method stub

    LayoutInflater inflator = activity.getLayoutInflater();
    if (paramView == null) {
        view = new ViewHolder();
        paramView = inflator.inflate(R.layout.list_item, null);

        view.name = (TextView) paramView.findViewById(R.id.name);
        view.email = (TextView) paramView.findViewById(R.id.email);
        view.mobile = (TextView) paramView.findViewById(R.id.mobile);
        view.id = (TextView) paramView.findViewById(R.id.id);
        view.deleteButtonImg = (ImageView) paramView.findViewById(R.id.ibclose);
        paramView.setTag(view);


    } else {
        view = (ViewHolder) paramView.getTag();
    }

    view.name.setText(TAG_NAME.get(paramInt));
    view.email.setText(TAG_EMAIL.get(paramInt));
    view.mobile.setText(TAG_PHONE_MOBILE.get(paramInt));
    view.deleteButtonImg.setImageResource(close.get(paramInt));
    view.id.setText(TAG_ID.get(paramInt));
    view.name.setFocusableInTouchMode(false);
    view.name.setFocusable(false);
    view.deleteButtonImg.setFocusableInTouchMode(false);
    view.deleteButtonImg.setFocusable(false);
    view.deleteButtonImg.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            DefaultHttpClient client = new DefaultHttpClient();
            SchemeRegistry registry = new SchemeRegistry();
            SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
            socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
            registry.register(new Scheme("https", socketFactory, 443));
            SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
                        HttpDelete httpDelete = new HttpDelete("https://abc.net/api/api/employees/"+TAG_ID.get(paramInt));

                        httpDelete.setHeader("content-type", "application/json");
                        JSONObject data = new JSONObject();

                        try {
                            data.put("Id", TAG_ID.get(paramInt));

                        /*StringEntity entity = new StringEntity(data.toString());
                        httpPost.setEntity(entity);*/

                        HttpResponse response = httpClient.execute(httpDelete);
                        String responseString = EntityUtils.toString(response.getEntity());
                        //int workoutId = responseJSON.getInt("id");
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (ClientProtocolException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                         TAG_NAME.remove(paramInt);
                         TAG_EMAIL.remove(paramInt);
                         TAG_PHONE_MOBILE.remove(paramInt);
                         TAG_ID.remove(paramInt);
                         close.remove(paramInt);

            notifyDataSetChanged();

        }
    });

    return paramView;
}
private class PkmnNameFilter extends Filter
{

@Override
protected FilterResults performFiltering(CharSequence constraint) {
    FilterResults results = new FilterResults();
    // We implement here the filter logic
    if (constraint == null || constraint.length() == 0) {
        // No filter implemented we return all the list
        results.values = TAG_NAME;
        results.count = TAG_NAME.size();
    }
    else {
        // We perform filtering operation
        List<Object> nPlanetList = new ArrayList<Object>(TAG_NAME);

        for (Object p : TAG_NAME) {
            if (((Scheme) p).getName().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                nPlanetList.add(p);
        }

        results.values = nPlanetList;
        results.count = nPlanetList.size();

    }
    return results;
}
  @Override
  protected void publishResults(CharSequence constraint, FilterResults results) {
       final ArrayList<String> localItems = (ArrayList<String>) results.values;
          notifyDataSetChanged();
          clear();
          for (Iterator<String> iterator = localItems.iterator(); iterator
                  .hasNext();) {
              String gi = (String) iterator.next();
              add(gi);
          }
  }
}

}
