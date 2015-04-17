package mass.knextgen1.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mass.knextgen1.R;
import mass.knextgen1.util.GlobalPreferenceManager;
import mass.knextgen1.util.JSONServlet;
import mass.knextgen1.util.TranslatorUtility;

//sample code
public class LaunchActivity extends Activity implements
        Spinner.OnItemSelectedListener, Spinner.OnItemClickListener,
        JSONServlet.TranslationServletListener {

    final Handler resultHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_RESULT) {
                TextView txtResult = (TextView) findViewById(R.id.txt_result);

                txtResult.setText((String)msg.obj);
            }
        }
    };

    private final int UPDATE_RESULT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        PreferenceManager.setDefaultValues(getBaseContext(), R.xml.preferences, false);

        Spinner spnLanguageChoice = (Spinner) this.findViewById(R.id.spn_languageChoice);
        spnLanguageChoice.setOnItemSelectedListener(this);

        int languageIndex = GlobalPreferenceManager.getGlobalPreferenceInt(this, getString(R.string.language_choice_key), 0);
        spnLanguageChoice.setSelection(languageIndex);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.launch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void scanItemClicked(View view) {
        new IntentIntegrator(this).initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null && scanResult.getContents() != null) {
            int languageIndex = GlobalPreferenceManager.getGlobalPreferenceInt(this, getString(R.string.language_choice_key), 0);

            JSONServlet.runTranslationServlet(this, this, TranslatorUtility.getLanguageSuffix(languageIndex), scanResult.getContents());
        } else {
            int languageIndex = GlobalPreferenceManager.getGlobalPreferenceInt(this, getString(R.string.language_choice_key), 0);

            JSONServlet.runTranslationServlet(this, this, TranslatorUtility.getLanguageSuffix(languageIndex), "681131183925");
        }
    }

    @Override
    public void onTranslationResult(JSONArray jsonArray) {
        Message msg = new Message();
        msg.what = UPDATE_RESULT;
        try {
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String text = jsonObject.getString(getString(R.string.product_company)) + "\n";
                text += jsonObject.getString(getString(R.string.product_name)) + "\n\n";
                text += jsonObject.getString(getString(R.string.ingredients)) + "\n\n";
                text += jsonObject.getString(getString(R.string.warnings)) + "\n\n";
                text += jsonObject.getString(getString(R.string.directions)) + "\n\n";
                text += jsonObject.getString(getString(R.string.other_info)) + "\n\n";

                msg.obj = text;
                resultHandler.sendMessage(msg);
            }

        } catch (JSONException e) {
            msg.obj = "Error in reading barcode!";
            resultHandler.sendMessage(msg);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spn_languageChoice) {
            Spinner spnLanguageChoice = (Spinner) parent;
            setLanguage(spnLanguageChoice.getSelectedItemPosition());
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (parent.getId() == R.id.spn_languageChoice) {
            setLanguage(0);
        }
    }

    private void setLanguage(int languageIndex) {
        GlobalPreferenceManager.setGlobalPreferenceInt(this, getString(R.string.language_choice_key), languageIndex);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (view.getId() == R.id.spn_languageChoice) {
            Spinner spnLanguageChoice = (Spinner) view;
            setLanguage(spnLanguageChoice.getSelectedItemPosition());
        }
    }
}
