import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.params.BasicHttpParams;

[...]

SchemeRegistry registry = new SchemeRegistry();
SSLSocketFactory sslSocketFactory = new SSLSocketFactory( keyStore, profile.getIdentityPassPhrase(), trustStore );
sslSocketFactory.setHostnameVerifier( new AllowAllHostnameVerifier() );
registry.register( new Scheme( "http", PlainSocketFactory.getSocketFactory(), 80) );
registry.register( new Scheme( "https", sslSocketFactory, 443 ) );

// Create a new HttpClient and Post Header
HttpParams httpParams = new BasicHttpParams();
httpParams.setBooleanParameter( "http.protocol.expect-continue", true );
HttpProtocolParams.setVersion( httpParams, HTTP_VERSION );
HttpProtocolParams.setContentCharset( httpParams, CONTENT_CHARSET );
HttpConnectionParams.setConnectionTimeout( httpParams, TIMEOUT_MILLISEC );
HttpConnectionParams.setSoTimeout( httpParams, TIMEOUT_MILLISEC );

httpClient = new DefaultHttpClient( new ThreadSafeClientConnManager( httpParams, registry ), httpParams );
