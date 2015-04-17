package mass.knextgen1.util;

import android.app.Activity;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import mass.knextgen1.R;

/**
 * Created by Rob Tanniru on 1/24/2015.
 */
public class JSONServlet {

    public static interface TranslationServletListener {
        public void onTranslationResult(JSONArray jsonArray);
    }

    private static class TranslationServlet extends Thread {
        @Override
        public void run() {
            m_listener.onTranslationResult(getLoginResponse());
        }

        public TranslationServlet(TranslationServletListener _listener, Activity _callingActivity, String _languageSuffix, String _barcodeID) {
            m_listener = _listener;
            m_languageSuffix = _languageSuffix;
            m_barcodeID = _barcodeID;
            m_callingActivity = _callingActivity;
        }

        private TranslationServletListener m_listener;
        private String m_languageSuffix;
        private String m_barcodeID;
        private Activity m_callingActivity;

        public String getLoginResponseHttp() {
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            try {
                String name2A="?q=";
                String name2B="{\"barcode\":\"";
                String name3A="\"}";


                String URLstr = m_callingActivity.getString(R.string.url_1);
                URLstr += m_languageSuffix + name2A + URLEncoder.encode(name2B);
                URLstr += m_barcodeID + URLEncoder.encode(name3A) + "&" + m_callingActivity.getString(R.string.url_3B);

                HttpGet httpGet = new HttpGet(URLstr);
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                } else {
                    Log.e("getLoginResponse", "Failed to get response");
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return builder.toString();
        }

        public JSONArray getLoginResponse() {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(getLoginResponseHttp());
                Log.e("getLoginResponse", jsonArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonArray;
        }
    }

    public static void runTranslationServlet(TranslationServletListener listener, Activity callingActivity, String languageSuffix, String barcodeID) {
        (new TranslationServlet(listener, callingActivity, languageSuffix, barcodeID)).start();
    }
}
