package com.example.shekhar.exportsmstoexcelsheet.bean;

/**
 * Created by ShekharKG on 1/8/2016.
 */
public class SmsModel {

  private String address;
  private String message;
  private long timeStamp;


  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(long timeStamp) {
    this.timeStamp = timeStamp;
  }

}
