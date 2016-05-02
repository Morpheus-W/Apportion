package com.unbelievable.tangweny.apportion.entity;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tangweny on 2016/2/24.
 */
public class Check implements Parcelable {

    private int parId;
    private String parTime;
    private String parName;
    private double consume;
    private int isCheck;
    private String remark;

    public int getParId() {
        return parId;
    }

    public void setParId(int parId) {
        this.parId = parId;
    }

    public String getParTime() {
        return parTime;
    }

    public void setParTime(String parTime) {
        this.parTime = parTime;
    }

    public String getParName() {
        return parName;
    }

    public void setParName(String parName) {
        this.parName = parName;
    }

    public double getConsume() {
        return consume;
    }

    public void setConsume(double consume) {
        this.consume = consume;
    }

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(parId);
        dest.writeString(parTime);
        dest.writeString(parName);
        dest.writeDouble(consume);
        dest.writeInt(isCheck);
        dest.writeString(remark);
    }
    public static final Creator<Check> CREATOR = new Creator<Check>(){
        public Check createFromParcel(Parcel in)
        {
            return new Check(in);
        }

        public Check[] newArray(int size)
        {
            return new Check[size];
        }
    };
    private Check(Parcel in)
    {
        this.parId = in.readInt();
        this.parTime = in.readString();
        this.parName = in.readString();
        this.consume = in.readDouble();
        this.isCheck = in.readInt();
        this.remark = in.readString();
    }
    public Check(){

    }
}
