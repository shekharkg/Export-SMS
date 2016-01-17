package com.example.shekhar.exportsmstoexcelsheet.bean;

/**
 * Created by ShekharKG on 1/8/2016.
 */
public class SmsModel {

  private String _id;
  private String address;
  private String message;
  private String readState;
  private String timeStamp;

  public String get_id() {
    return _id;
  }

  public void set_id(String _id) {
    this._id = _id;
  }

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

  public String getReadState() {
    return readState;
  }

  public void setReadState(String readState) {
    this.readState = readState;
  }

  public String getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(String timeStamp) {
    this.timeStamp = timeStamp;
  }

}
