package com.prj666.recycling_vision.user;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class User implements Parcelable {

    // Class variables
    private String userName;
    private String phoneNum;
    private String email;
    private String password;
    private String postalCode;
    private Date dateOfBirth;
    private Boolean validationStatus;

    //Constructor

    public User(String userName, String phoneNum, String email, String password,
                String postalCode, String dateOfBirth, Boolean validationStatus) throws ParseException {

        this.userName = userName;
        this.phoneNum = phoneNum;
        this.email = email;
        this.password = password;
        this.postalCode = postalCode;
        this.dateOfBirth = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).parse(dateOfBirth);
        this.validationStatus = validationStatus;
    }

    public User(String userName, String phoneNum, String email, String password, String postalCode, Date dateOfBirth) {
        this.userName = userName;
        this.phoneNum = phoneNum;
        this.email = email;
        this.password = password;
        this.postalCode = postalCode;
        this.dateOfBirth = dateOfBirth;
    }


    protected User(Parcel in) {

        userName = in.readString();
        phoneNum = in.readString();
        email = in.readString();
        password = in.readString();
        postalCode = in.readString();
        byte tmpValidationStatus = in.readByte();
        dateOfBirth = new Date();
        dateOfBirth.setTime(in.readLong());
        validationStatus = tmpValidationStatus == 0 ? null : tmpValidationStatus == 1;
    }




    // Setters and Getters

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean getValidationStatus() {

        return validationStatus;
    }

    public void setValidationStatus(Boolean validationStatus) {
        this.validationStatus = validationStatus;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", validationStatus=" + validationStatus +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(phoneNum);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(postalCode);
        dest.writeByte((byte) (validationStatus == null ? 0 : validationStatus ? 1 : 2));
        dest.writeLong(dateOfBirth.getTime());
    }
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}

