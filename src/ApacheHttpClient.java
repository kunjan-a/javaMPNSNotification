import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Set;

/**
 * This example demonstrates how to create secure connections with a custom SSL
 * context.
 */
public class ApacheHttpClient {

    public static void main(String[] args) throws Exception {
        CloseableHttpClient httpclient = getClient();

        try {

            HttpPost httpPost = getRequest();

            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                parseResponse(response);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

    private static CloseableHttpClient getClient() throws NoSuchAlgorithmException, KeyManagementException, UnrecoverableKeyException, CertificateException, KeyStoreException, IOException {
        SSLContext sslContext = Util.getSslContext();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);//BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        return HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
    }

    private static void parseResponse(CloseableHttpResponse response) throws IOException {
        Header[] allHeaders = response.getAllHeaders();
        System.out.println("Response headers");
        for (Header header : allHeaders) {
            System.out.println(header.getName() + ":" + header.getValue());
        }

        HttpEntity entity = response.getEntity();

        System.out.println("----------------------------------------");
        System.out.println("Status code: "+response.getStatusLine().getStatusCode());
        System.out.println(response.getStatusLine());
        if (entity != null) {
            System.out.println("Response content length: " + entity.getContentLength());
        }
        EntityUtils.consume(entity);
    }

    private static HttpPost getRequest() {
        HttpPost httpPost = new HttpPost(Util.TARGET_URL);

        System.out.println("executing request " + httpPost.getRequestLine());
        HashMap<String,String> headers = Util.getHeadersMap();
        Set<String> headerKeys = headers.keySet();
        for (String key : headerKeys) {
            httpPost.setHeader(key, headers.get(key));
        }

        httpPost.setEntity(new StringEntity(Util.DATA, ContentType.TEXT_XML));
        return httpPost;
    }

}
