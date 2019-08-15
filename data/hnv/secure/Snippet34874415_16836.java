public class GlobalSearchRequestHandler extends JsonHttpResponseHandler {

private Context mContext;
//private Dialog mDialog;
private View progressBar;
private String encodedRequest;
private HttpCommunicationListener mHandler;
private boolean mShowProgress;
private static final String contentType = "application/x-www-form-urlencoded";

public GlobalSearchRequestHandler(Context mContext,
        HttpCommunicationListener handler, String userId, String keyword,
        String pageNumber, boolean canShowProgress,View pb) {
    this.mContext = mContext;
    this.encodedRequest = encodedRequest(userId, keyword, pageNumber);
    this.mHandler = handler;
    this.mShowProgress = canShowProgress;
    this.progressBar= pb;
}

private String encodedRequest(String userId, String keyword,
        String pageNumber) {

    JSONObject genInfoObj = new JSONObject();
    JSONObject globalSearch = new JSONObject();

    globalSearch.put("userId", userId);
    globalSearch.put("keyword", MysnlUtils.replaceSpecialCharacterEncodeRequest(keyword));
    globalSearch.put("pageNumber", pageNumber);

    genInfoObj.put("globalSearch", globalSearch);
    genInfoObj.put(getString(R.string.genInfo), MysnlUtils.getGenInfo());

    return genInfoObj.toString();
}

private String getString(int id) {
    return MysnlApplication.mAppContext.getResources().getString(id);
}

public void execute() {
    try {
        encodedRequest = "xmlrequest=" + encodedRequest;
        Logger.e(encodedRequest);
        StringEntity entity = new StringEntity(encodedRequest);
        AsyncHttpClient client = new AsyncHttpClient();

        KeyStore trustStore = KeyStore.getInstance(KeyStore
                .getDefaultType());
        trustStore.load(null, null);
        MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
        sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        client.setSSLSocketFactory(sf);
        /*
         * final int DEFAULT_TIMEOUT = 20 * 1000;
         * client.setTimeout(DEFAULT_TIMEOUT);
         */

        client.post(MysnlApplication.mAppContext,
                getString(R.string.php_server_url), entity, contentType,
                this);
    } catch (Exception e) {
        Logger.e(e.getMessage());
        ErrorBean errorBean = new ErrorBean();
        errorBean.setErrorCode(AppConstant.BAD_REQUEST);
        errorBean.setErrorMsg(e.getMessage());
        mHandler.onFail(errorBean);
    }
}

@Override
public void onSuccess(org.json.JSONObject response) {
    Logger.e("onSuccess : "+response.toString());       

    GlobalSearchBean bean = null;
    try {
        org.json.JSONObject headJsonObject = (org.json.JSONObject) response.get("globalSearch");
        Gson gson = new Gson();
        bean = gson.fromJson(headJsonObject.toString(), GlobalSearchBean.class);
        if (bean != null) {
            mHandler.onSuccess(bean);
        }
        else{
            mHandler.onFail(bean);
        }
    } catch (JSONException e) {
        e.printStackTrace();
    }
}

@Override
public void onFailure(Throwable error, String content) {
    Logger.e("onFailure : "+content.toString());
    ErrorBean errorBean=new ErrorBean();
    errorBean.setErrorCode(AppConstant.BAD_REQUEST);
    errorBean.setErrorMsg(content);
    mHandler.onFail(errorBean);
    try {
        progressBar.setVisibility(View.GONE);
        /*if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }*/
    } catch (Exception e) {
    }

    super.onFailure(error, content);
}

@Override
public void onStart() {
    try {
        if(mShowProgress){
            progressBar.setVisibility(View.VISIBLE);
            /*mDialog = MysnlUtils.getProgressDialog(mContext);
            mDialog.show();*/
        }
    } catch (Exception e) {
    }

    super.onStart();
}

@Override
public void onFinish() {
    try {
        progressBar.setVisibility(View.GONE);
        /*if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }*/
    } catch (Exception e) {
    }

    super.onFinish();
}
