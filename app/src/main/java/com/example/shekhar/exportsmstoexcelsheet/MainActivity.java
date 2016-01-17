package com.example.shekhar.exportsmstoexcelsheet;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shekhar.exportsmstoexcelsheet.bean.SmsModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class MainActivity extends AppCompatActivity {

  private ProgressDialog progressDialog;
  private EditText fileNameEt;
  private FloatingActionButton fab;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        fab.setVisibility(View.GONE);
        progressDialog.show();
        String status = exportSmsToSheet(readAllSms());
        progressDialog.dismiss();
        Snackbar.make(view, status, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });

    fileNameEt = (EditText) findViewById(R.id.fileName);
    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Exporting. Please wait...");
    progressDialog.setCancelable(false);
  }

  @Override
  protected void onResume() {
    super.onResume();
    fab.setVisibility(View.GONE);
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
    if (smsModels.size() == 0)
      return "No SMS to export";

    String status = null;

    String fileName = fileNameEt.getText().toString().trim();
    if(fileName.isEmpty())
      fileName = String.valueOf(Calendar.getInstance().getTimeInMillis());
    File sdCard = Environment.getExternalStorageDirectory();
    File file = new File(sdCard, fileName + ".xls");
    WorkbookSettings wbSettings = new WorkbookSettings();
    wbSettings.setLocale(new Locale("en", "EN"));

    WritableWorkbook workbook;
    try {
      workbook = Workbook.createWorkbook(file, wbSettings);
      WritableSheet sheet = workbook.createSheet("Messages", 0);

      Calendar calendar = Calendar.getInstance();

      for (int i = 0; i < smsModels.size(); i++) {
        SmsModel smsModel = smsModels.get(i);
        Label address = new Label(0, i, smsModel.getAddress());
        Label message = new Label(1, i, smsModel.getMessage());
        calendar.setTimeInMillis(smsModel.getTimeStamp());
        Label timeStamp = new Label(2, i, getMonthNameFromIndex(calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR)));

        try {
          sheet.addCell(address);
          sheet.addCell(message);
          sheet.addCell(timeStamp);
        } catch (RowsExceededException e) {
          e.printStackTrace();
        } catch (WriteException e) {
          e.printStackTrace();
          status = e.toString();
        }
      }

      workbook.write();

      try {
        workbook.close();
      } catch (WriteException e) {
        e.printStackTrace();
        status = e.toString();
      }

    } catch (IOException e) {
      e.printStackTrace();
      status = e.toString();
    }

    return status == null ? smsModels.size() + " messages are exported to " + fileName : status;
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
        smsModel.setAddress(c.getString(c
            .getColumnIndexOrThrow("address")));
        smsModel.setMessage(c.getString(c.getColumnIndexOrThrow("body")));
        smsModel.setTimeStamp(Long.parseLong(c.getString(c.getColumnIndexOrThrow("date"))));
        lstSms.add(smsModel);
        c.moveToNext();
      }
    } else {
      Log.e("Retrieving SMS", "You have no SMS");
    }
    c.close();

    return lstSms;
  }

  private String getMonthNameFromIndex(int index, int date, int year) {
    switch (index) {
      case 0:
        return "Jan " + date + ", " + year;
      case 1:
        return "Feb " + date + ", " + year;
      case 2:
        return "Mar " + date + ", " + year;
      case 3:
        return "Apr " + date + ", " + year;
      case 4:
        return "May " + date + ", " + year;
      case 5:
        return "Jun " + date + ", " + year;
      case 6:
        return "Jul " + date + ", " + year;
      case 7:
        return "Aug " + date + ", " + year;
      case 8:
        return "Sep " + date + ", " + year;
      case 9:
        return "Oct " + date + ", " + year;
      case 10:
        return "Nov " + date + ", " + year;
      case 11:
        return "Dec " + date + ", " + year;
    }
    return "---";
  }
}
