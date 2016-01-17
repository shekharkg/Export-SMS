package com.example.shekhar.exportsmstoexcelsheet;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.shekhar.exportsmstoexcelsheet.bean.SmsModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private ProgressDialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        progressDialog.show();
        String status = exportSmsToSheet(readAllSms());
        progressDialog.dismiss();
        Snackbar.make(view, status, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });

    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Exporting. Please wait...");
    progressDialog.setCancelable(false);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }



  private String exportSmsToSheet(List<SmsModel> smsModels) {
    if(smsModels.size() == 0)
      return "No SMS to export";



    return "";
  }

  private List<SmsModel> readAllSms() {
    List<SmsModel> lstSms = new ArrayList<SmsModel>();
    SmsModel smsModel = new SmsModel();
    Uri message = Uri.parse("content://sms/");
    ContentResolver cr = getContentResolver();

    Cursor c = cr.query(message, null, null, null, null);
    int totalSMS = c.getCount();
    if (c.moveToFirst()) {
      for (int i = 0; i < totalSMS; i++) {

        smsModel = new SmsModel();
        smsModel.set_id(c.getString(c.getColumnIndexOrThrow("_id")));
        smsModel.setAddress(c.getString(c
            .getColumnIndexOrThrow("address")));
        smsModel.setMessage(c.getString(c.getColumnIndexOrThrow("body")));
        smsModel.setReadState(c.getString(c.getColumnIndex("read")));
        smsModel.setTimeStamp(c.getString(c.getColumnIndexOrThrow("date")));
        lstSms.add(smsModel);
        c.moveToNext();
      }
    } else {
      Log.e("Retrieving SMS", "You have no SMS");
    }
    c.close();

    return lstSms;
  }
}
