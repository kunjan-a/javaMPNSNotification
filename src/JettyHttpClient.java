import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Result;
import org.eclipse.jetty.client.util.BufferingResponseListener;
import org.eclipse.jetty.client.util.BytesContentProvider;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import javax.net.ssl.SSLContext;

/**
 * This example demonstrates how to create secure connections with a custom SSL
 * context.
 */
public class JettyHttpClient {

    public static void main(String[] args) throws Exception {
        final HttpClient _httpClient = getClient();
        try {
            _httpClient.start();
        } catch (Exception e) {
            System.out.println("Couldn't start jetty httpclient due to "+ e.toString());
        }

        Request request = getRequest(_httpClient);
        request.send(new BufferingResponseListener() {
            @Override
            public void onComplete(Result result) {
                parseResponse(result);
            }
        });
    }

    private static void parseResponse(Result result) {
        if (result.isFailed()) {
            System.out.println("result has isFailed set");
        } else {
            if (result.getResponse().getStatus() == 200) {
                HttpFields httpFields = result.getResponse().getHeaders();
                System.out.println("response headers from MPNS indicate success : \n" + httpFields.toString());
            } else {
                System.out.println("response headers from MPNS dont indicate success");
            }
        }
    }

    private static Request getRequest(HttpClient _httpClient) {
        Request request = _httpClient.POST(Util.TARGET_URL);
        Util.setHeaders(request);
        request.content(new BytesContentProvider(Util.DATA.getBytes()), "text/xml");
        return request;
    }

    private static HttpClient getClient() {
        SSLContext sslContext = null;
        try{
            sslContext = Util.getSslContext();
        }catch (Exception e){
            System.out.println("Couldn't load the sslcontext because"+ e.toString());
        }
        // Instantiate and configure the SslContextFactory
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setSslContext(sslContext);

/*
        try {
            sslContextFactory.setKeyStore(getKeyStore(new File(CERTIFACATE_FILE), CERTIFACATE_PASS));
            sslContextFactory.setKeyStorePassword(CERTIFACATE_PASS);
            sslContextFactory.setKeyStoreType("PKCS12");
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
*/
        final HttpClient _httpClient = new HttpClient(sslContextFactory);
        _httpClient.setMaxConnectionsPerDestination(25);
        return _httpClient;
    }

}
