package controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dao.StrikeDao;
import escola_x.escola_x.R;
import helper.HttpHandlerHelper;
import model.Strike;

public class StrikeController extends Activity {

    private ProgressDialog pDialog;

    StrikeDao strikeDao;
    TextView suspensionTextView;

    private String urlStrike = "http://escolax.herokuapp.com/api/strikes";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        strikeDao = StrikeDao.getInstance(getApplicationContext());

        String message = "\t Por Favor aguarde enquanto estamos atualizando seu banco de dados" +
                " em relação as advertências. Pode ser que demore um pouco, mas por favor" +
                " não feche o aplicativo";

        suspensionTextView = (TextView) findViewById(R.id.jsonSMS);
        suspensionTextView.setText(message);

        new GetStrike().execute();
    }

    private class GetStrike extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(StrikeController.this);
            pDialog.setMessage("Por favor aguarde...");
            pDialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandlerHelper httpHandlerHelper = new HttpHandlerHelper();

            String jsonStrike = httpHandlerHelper.makeServiceCall(urlStrike);

            if (jsonStrike != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStrike);
                    JSONArray strikes = jsonObj.getJSONArray("strikes");

                    for (int aux = 0; aux < strikes.length(); aux++) {
                        JSONObject strikesJSONObject = strikes.getJSONObject(aux);

                        Strike strike = new Strike();

                        strike.setIdStrike(Integer.parseInt(strikesJSONObject.getString("id")));
                        strike.setDescription_strike(strikesJSONObject.getString(
                                "description_strike"));
                        strike.setDate_strike(strikesJSONObject.getString("date_strike"));

                        JSONObject alumnJSONObject = strikesJSONObject.getJSONObject("alumn");
                        strike.setIdAlumn(Integer.parseInt(alumnJSONObject.getString("id")));

                        strikeDao.insertStrike(strike);
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Intent intent = new Intent(getApplicationContext(), SMSController.class);
            startActivityForResult(intent, 0);
            finish();
        }
    }
}