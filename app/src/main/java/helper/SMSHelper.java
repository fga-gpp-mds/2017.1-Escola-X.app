package helper;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;

import java.util.ArrayList;
import java.util.List;

import dao.NotificationDao;
import dao.ParentDao;
import model.Parent;

public class SMSHelper extends Activity{

    NotificationDao notificationDao;
    ParentDao parentDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentDao = ParentDao.getInstance(getApplicationContext());
        notificationDao = NotificationDao.getInstance(getApplicationContext());
    }

    public void sendSMSNotification () {

        List<Parent> parentList = new ArrayList<Parent>();

        parentList = parentDao.getAllParents();
        for(int aux = 0;aux <parentList.size();aux ++) {
            SmsManager.getDefault().sendTextMessage(parentList.get(aux).getPhone(),null,
                    "Caro(a) Senhor(a) " + parentList.get(aux).getName()
                            + "\n" + "",null,null);
        }

        /*SmsManager.getDefault().sendTextMessage(numeroTelefone,null,
                "Leonardo Arthur" +mensagemEscrita,null,null);*/
    }
}