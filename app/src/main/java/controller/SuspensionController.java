package controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dao.SuspensionDao;
import escola_x.escola_x.R;
import helper.HttpHandlerHelper;
import model.Suspension;

public class SuspensionController extends Activity{

    private ProgressDialog pDialog;

    SuspensionDao suspensionDao;
    TextView suspensionTextView;
    Button tryAgain;

    private String urlSuspension = "http://escolax.herokuapp.com/api/suspensions";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        suspensionDao = SuspensionDao.getInstance(getApplicationContext());

        String message = "\t Por Favor aguarde enquanto estamos atualizando seu banco de dados" +
                " em relação as suspensões. Pode ser que demore um pouco, então pedimos que " +
                "não feche o aplicativo.";

        suspensionTextView = (TextView) findViewById(R.id.jsonSMS);
        tryAgain = (Button) findViewById(R.id.tryAgain);

        suspensionTextView.setText(message);
        tryAgain.setVisibility(View.INVISIBLE);

        new GetSuspensions().execute();
    }

    private class GetSuspensions extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SuspensionController.this);
            pDialog.setMessage("Por favor aguarde...");
            pDialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandlerHelper httpHandlerHelper = new HttpHandlerHelper();

            String jsonSuspension = httpHandlerHelper.makeServiceCall(urlSuspension);

            if (jsonSuspension != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonSuspension);
                    JSONArray suspensions = jsonObj.getJSONArray("suspensions");

                    for (int aux = 0; aux < suspensions.length(); aux++) {
                        JSONObject suspensionsJSONObject = suspensions.getJSONObject(aux);
                        JSONObject alumnJSONObject = suspensionsJSONObject.getJSONObject("alumn");

                        if(alumnJSONObject.getString("id").equals("null")) {

                        } else {
                            Suspension suspension = new Suspension();

                            suspension.setIdSuspension(Integer.parseInt(
                                                suspensionsJSONObject.getString("id")));
                            suspension.setDescription(
                                                suspensionsJSONObject.getString("description"));
                            suspension.setQuantity_days(Integer.parseInt(
                                                suspensionsJSONObject.getString("quantity_days")));
                            suspension.setTitle(suspensionsJSONObject.getString("title"));
                            suspension.setDateSuspension(
                                                suspensionsJSONObject.getString("date_suspension"));

                            suspension.setIdAlumn(Integer.parseInt(alumnJSONObject.getString("id")));

                            suspensionDao.insertSuspension(suspension);
                        }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Problemas no JSON. Contate os responsáveis pelo app.",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Não foi encontrada internet. " +
                                "Não será baixado os dados dos alunos " +
                                "enquanto esse problema persistir.",Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Intent intent = new Intent(getApplicationContext(), StrikeController.class);
            startActivityForResult(intent, 0);
            finish();
        }
    }
}
