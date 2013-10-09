import org.eclipse.jetty.client.api.Request;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Set;

public class Util {
    static final String LINE_BREAKER = System.getProperty("line.separator");
    static final String CERTIFACATE_FILE = "/home/kunjan/test/javaMPNSNotification/src/ieSigning/talk.to_ie.pfx";//talk.to.p12";//emo/talk.to.p12";private static final java.lang.String CERTIFACATE_PASS = "directi";//private static final String TARGET_URL = "https://localhost:9999";//
    static final String TARGET_URL = "https://db3.notify.live.net/unthrottledthirdparty/01.00/AQH-O-JEL4gMSJFVsV8U1AaBAgAAAAADZw8DAAQUZm52OkYxRkFDRDhBQkFBNUI3RjcFBkxFR0FDWQ";
    static final String DATA = "<?xml version=\"1.0\" encoding=\"utf-8\"?><wp:Notification xmlns:wp=\"WPNotification\"><wp:Toast><wp:Text1>Title</wp:Text1><wp:Text2>Test message</wp:Text2><wp:Param>/InermediatePage.xaml</wp:Param></wp:Toast></wp:Notification>";
    private static final String CERTIFACATE_PASS = "directi";

    static void setHeaders(HttpsURLConnection connection) throws ProtocolException {
        connection.setRequestMethod("POST");
        HashMap<String, String> headers = getHeadersMap();
        Set<String> headerKeys = headers.keySet();
        for (String key : headerKeys) {
            connection.setRequestProperty(key, headers.get(key));
        }
    }

    static void setHeaders(Request request) {
        HashMap<String, String> headers = getHeadersMap();
        Set<String> headerKeys = headers.keySet();
        for (String key : headerKeys) {
            request.header(key, headers.get(key));
        }
    }

    static HashMap<String, String> getHeadersMap() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-windowsphone-target", "toast");
        headers.put("x-notificationclass", "2");
/*
        headers.put("content_type","text/xml");
*/
        return headers;
    }

    static SSLContext getSslContext() throws NoSuchAlgorithmException, KeyManagementException, UnrecoverableKeyException, CertificateException, KeyStoreException, IOException {
        KeyManager[] kms = getKeyManagers(new File(CERTIFACATE_FILE), CERTIFACATE_PASS);
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(kms, null, null);
        return context;
    }

    static KeyManager[] getKeyManagers(File pKeyFile, String pKeyPassword) throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        KeyStore keyStore = getKeyStore(pKeyFile, pKeyPassword);
        keyManagerFactory.init(keyStore, pKeyPassword.toCharArray());

        return keyManagerFactory.getKeyManagers();
    }

    static KeyStore getKeyStore(File pKeyFile, String pKeyPassword) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        InputStream keyInput = new FileInputStream(pKeyFile);
        keyStore.load(keyInput, pKeyPassword.toCharArray());
        keyInput.close();
        return keyStore;
    }
}