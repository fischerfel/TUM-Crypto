package com.tdi.api.brm.service;

import java.io.InputStream;
import java.security.KeyStore;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;

import com.tdi.api.brm.servlet.InitServlet;
import com.tdi.api.brm.util.TDI_Constants;
import com.tdi.api.brm.util.TDI_Utility;
import com.tdi.common.exceptions.TDI_CommonException;
import com.tdi.common.utility.TDI_CommonConstants;
import com.tdi.restService.model.tdiapis.ProccessAccountElements;

public class TDI_IGProcessAccountServiceImpl2 extends AbstractBaseService implements TDI_IGProcessAccountService{

static final Logger LOGGER = Logger.getLogger(TDI_IGProcessAccountServiceImpl2.class);

public TDI_IGProcessAccountServiceImpl2(){
    LOGGER.debug("TDI_IGProcessAccountServiceImpl: Contructor: Entered");

    LOGGER.debug("TDI_IGProcessAccountServiceImpl: Contructor: Leaving");
}

public ProccessAccountElements processAccount(String payload, String environment) throws Exception{
    LOGGER.debug("TDI_IGProcessAccountServiceImpl: processAccount: Entered");

    ProccessAccountElements serviceResp = new ProccessAccountElements();
    String xmlFilePath;
    String xmlString;
    String httpResponsexml;
    String httpPostUri;
    StringEntity stringEntity;
    HttpClient httpClient = null;
    HttpPost httpPost;
    HttpResponse httpResponse;
    HttpEntity httpEntity;

    InputStream keyStoreInputStream;
    String keyStoreFilePath;
    String keyStorePassword;
    KeyStore keyStore;
    SSLSocketFactory socketFactory;
    Scheme scheme;

    try{

            xmlFilePath = TDI_Constants.XML_TEMPLATE_DIR.concat(TDI_Constants.FORWARD_SLASH).concat(TDI_Constants.TDI_PROCESS_ACCOUNT_XML);


            xmlString = TDI_Utility.readFromFile(xmlFilePath);

            stringEntity = new StringEntity(payload, HTTP.UTF_8);
            stringEntity.setContentType(TDI_Constants.TEXT_XML);

            httpPostUri = InitServlet.getPropertyValue(environment.concat(TDI_Constants.DELIMITER_UNDERSCORE).concat(TDI_Constants.IG_PROCESS_ACCOUNT_URL1));

            httpPost = new HttpPost(httpPostUri);
            httpPost.setHeader(TDI_Constants.CONTENT_TYPE, TDI_Constants.CONTENT_TYPE_VALUE);
            httpPost.setEntity(stringEntity);

            keyStorePassword = TDI_Constants.KEY_STORE_PASSWORD;
            keyStoreFilePath = TDI_Constants.BRM_SHARE.concat(TDI_Constants.FORWARD_SLASH).concat(environment).concat(TDI_Constants.DELIMITER_UNDERSCORE).concat(TDI_Constants.PROCESS_ACCOUNT_CERTIFICATE);

            keyStoreInputStream = TDI_Utility.getResourceAsInputStream(keyStoreFilePath);

            keyStore  = KeyStore.getInstance(KeyStore.getDefaultType());

            try {
                keyStore.load(keyStoreInputStream, keyStorePassword.toCharArray());
                LOGGER.debug("TDI_IGProcessAccountServiceImpl: processAccount: Try: keyStore: Loaded");
            }finally{
                keyStoreInputStream.close();
                LOGGER.debug("TDI_IGProcessAccountServiceImpl: processAccount: Try: keyStoreInputStream: Closed");
            }

            socketFactory = new SSLSocketFactory(keyStore);
            socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            LOGGER.debug("TDI_IGProcessAccountServiceImpl: processAccount: Try: HostnameVerifier: Set");

            scheme = new Scheme(TDI_Constants.HTTPS, socketFactory, TDI_Constants.PORT_443);
            LOGGER.debug("TDI_IGProcessAccountServiceImpl: processAccount: Try: Scheme: Created");

            httpClient = new DefaultHttpClient();

            httpClient.getConnectionManager().getSchemeRegistry().register(scheme);
            LOGGER.debug("TDI_IGProcessAccountServiceImpl: processAccount: Try: HttpClient: Registered");

            httpResponse = (BasicHttpResponse) httpClient.execute(httpPost);

            //Capture the TDI-Service un available exception
            if(httpResponse != null &&  httpResponse.getStatusLine().toString().contains(TDI_Constants.WEBSERVICE_UNAVAIALBLE)){
                LOGGER.debug("TDI_IGProcessAccountServiceImpl: processAccount: URL : " + httpPostUri);
                LOGGER.debug("TDI_IGProcessAccountServiceImpl: processAccount: httpResponse.getStatusLine() " + httpResponse.getStatusLine().toString());
                throw new TDI_CommonException(TDI_Constants.WEBSERVICE_ERROR_CODE_404,TDI_Constants.IG_SERVICE_ERROR_MESSAGE_404);
            }

            if(httpResponse != null) {
                httpEntity = httpResponse.getEntity();
                httpResponsexml = TDI_Utility.getInputStreamAsString(httpEntity.getContent());
                serviceResp = parseResponseXML (httpEntity, httpResponsexml);       

            }else{
                httpResponsexml = TDI_Constants.COMMON_XML_RESPONSE_ERROR_NO_RESPONSE;
                throw new TDI_CommonException(TDI_CommonConstants.ERROR_CODE_932, httpResponsexml);
            }
            LOGGER.debug("TDI_IGProcessAccountServiceImpl: processAccount: Try: Final: HttpResponsexml=[" + httpResponsexml + "]");

        LOGGER.debug("TDI_IGProcessAccountServiceImpl: processAccount: Try: Leaving");
    }catch(java.net.SocketException soe){
        LOGGER.debug("TDI_IGProcessAccountServiceImpl: processAccount: Catch: Socket Exception Entered");
        LOGGER.debug("TDI_IGProcessAccountServiceImpl: processAccount: Catch: Socket Exception: " + soe.getMessage());
        LOGGER.debug("TDI_IGProcessAccountServiceImpl: processAccount: Catch: Socket Exception Leaving");
        throw new TDI_CommonException(TDI_Constants.WEBSERVICE_ERROR_CODE_404,TDI_Constants.IG_SERVICE_ERROR_MESSAGE_404);
    }catch(TDI_CommonException e){
        e.printStackTrace();
        LOGGER.debug("TDI_IGProcessAccountServiceImpl: processAccount: Catch: TDI_CommonException: Entered");
        LOGGER.error("TDI_IGProcessAccountServiceImpl: processAccount: Catch: TDI_CommonException: " + e);
        LOGGER.debug("TDI_IGProcessAccountServiceImpl: processAccount: Catch: TDI_CommonException: Leaving");
        serviceResp.setRespCode(TDI_Constants.WDR_WEBSERVICE_ERROR_CODE);
        serviceResp.setRespDesc(TDI_Constants.WDR_SERVICE_ERROR_MESSAGE);
        throw e;
    }catch(Exception e){
        LOGGER.debug("TDI_IGProcessAccountServiceImpl: processAccount: Catch: Exception: Entered");
        LOGGER.error("TDI_IGProcessAccountServiceImpl: processAccount: Catch: Exception: " + e);
        LOGGER.debug("TDI_IGProcessAccountServiceImpl: processAccount: Catch: Exception: Leaving");
        serviceResp.setRespCode(TDI_Constants.WDR_WEBSERVICE_ERROR_CODE);
        serviceResp.setRespDesc(TDI_Constants.WDR_SERVICE_ERROR_MESSAGE);
        throw e;
    }

    LOGGER.debug("TDI_IGProcessAccountServiceImpl: processAccount: Leaving");
    return serviceResp;
}
