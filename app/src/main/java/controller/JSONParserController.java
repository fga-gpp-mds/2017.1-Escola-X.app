package controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dao.AlumnDao;
import dao.NotificationDao;
import dao.ParentDao;
import dao.StrikeDao;
import dao.SuspensionDao;
import escola_x.escola_x.R;
import helper.HttpHandlerHelper;
import model.Alumn;
import model.Notification;
import model.Parent;
import model.Strike;
import model.Suspension;

public class JSONParserController extends Activity {

    private ProgressDialog pDialog;

    ParentDao parentDao;
    AlumnDao alumnDao;
    NotificationDao notificationDao;
    StrikeDao strikeDao;
    SuspensionDao suspensionDao;

    private static String urlParents = "http://murmuring-mountain-86195.herokuapp.com/api/parents";
    private static String urlAlumns = "http://murmuring-mountain-86195.herokuapp.com/api/alumns";
    private static String urlNotifications =
            "http://murmuring-mountain-86195.herokuapp.com/api/notifications";
    private static String urlStrike = "http://murmuring-mountain-86195.herokuapp.com/api/strikes";
    private static String urlSuspension =
            "http://murmuring-mountain-86195.herokuapp.com/api/suspensions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        parentDao = ParentDao.getInstance(getApplicationContext());
        alumnDao = AlumnDao.getInstance(getApplicationContext());
        notificationDao = NotificationDao.getInstance(getApplicationContext());
        strikeDao = StrikeDao.getInstance(getApplicationContext());
        suspensionDao = SuspensionDao.getInstance(getApplicationContext());

        new GetDatas().execute();
    }

    private class GetDatas extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(JSONParserController.this);
            pDialog.setMessage("Por favor aguarde...");
            pDialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            List<Parent> parentList = new ArrayList<Parent>();
            List<Alumn> alumnList = new ArrayList<Alumn>();

            HttpHandlerHelper httpHandlerHelper = new HttpHandlerHelper();

            String jsonParent = httpHandlerHelper.makeServiceCall(urlParents);
            String jsonAlumn = httpHandlerHelper.makeServiceCall(urlAlumns);
            String jsonNotification = httpHandlerHelper.makeServiceCall(urlNotifications);
            String jsonStrike = httpHandlerHelper.makeServiceCall(urlStrike);
            String jsonSuspension = httpHandlerHelper.makeServiceCall(urlSuspension);

            if (jsonParent != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonParent);
                    JSONArray parents = jsonObj.getJSONArray("parents");

                    for (int aux = 0; aux < parents.length(); aux++) {
                        JSONObject parentsJSONObject = parents.getJSONObject(aux);

                        Parent parent = new Parent();

                        parent.setIdParent(Integer.parseInt(parentsJSONObject.getString("id")));
                        parent.setName(parentsJSONObject.getString("name"));
                        parent.setPhone(parentsJSONObject.getString("phone"));

                        parentList.add(parent);
                        parentDao.syncronParent(parentList);
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

            if (jsonAlumn != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonAlumn);
                    JSONArray alumns = jsonObj.getJSONArray("alumns");

                    for (int aux = 0; aux < alumns.length(); aux++) {
                        JSONObject alumnsJSONObject = alumns.getJSONObject(aux);

                        Alumn alumn = new Alumn();

                        alumn.setIdAlumn(Integer.parseInt(alumnsJSONObject.getString("id")));
                        alumn.setName(alumnsJSONObject.getString("name"));
                        alumn.setRegistry(Integer.parseInt(alumnsJSONObject.getString("registry")));

                        JSONObject parentJSONObject = alumnsJSONObject.getJSONObject("parent");
                        alumn.setIdParent(Integer.parseInt(parentJSONObject.getString("id")));

                        alumnList.add(alumn);
                        alumnDao.syncronAlumn(alumnList);
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

            if (jsonNotification != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonNotification);
                    JSONArray notifications = jsonObj.getJSONArray("notifications");

                    for (int aux = 0; aux < notifications.length(); aux++) {
                        JSONObject notificationsJSONObject = notifications.getJSONObject(aux);

                        Notification notification = new Notification();

                        notification.setIdNotification(Integer.parseInt(
                                notificationsJSONObject.getString("id")));
                        notification.setTitle(notificationsJSONObject.getString("title"));
                        notification.setNotification_text(
                                notificationsJSONObject.getString("notification_text"));
                        notification.setNotificaton_date(
                                notificationsJSONObject.getString("notification_date"));
                        notification.setMotive(notificationsJSONObject.getString("motive"));

                        notificationDao.insertNotification(notification);
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

            if (jsonSuspension != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonSuspension);
                    JSONArray suspensions = jsonObj.getJSONArray("suspensions");

                    for (int aux = 0; aux < suspensions.length(); aux++) {
                        JSONObject suspensionsJSONObject = suspensions.getJSONObject(aux);

                        Suspension suspension = new Suspension();

                        suspension.setIdSuspension(Integer.parseInt(
                                suspensionsJSONObject.getString("id")));
                        suspension.setDescription(suspensionsJSONObject.getString("description"));
                        suspension.setQuantity_days(Integer.parseInt(
                                suspensionsJSONObject.getString("quantity_days")));
                        suspension.setTitle(suspensionsJSONObject.getString("title"));

                        JSONObject alumnJSONObject = suspensionsJSONObject.getJSONObject("alumn");
                        suspension.setIdAlumn(Integer.parseInt(alumnJSONObject.getString("id")));

                        suspensionDao.insertSuspension(suspension);
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

            Intent intent = new Intent(getApplicationContext(), SMSActivity.class);
            startActivityForResult(intent, 0);
            finish();
        }
    }
}
