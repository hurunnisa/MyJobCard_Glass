package com.ondevice.myjobcard_glass.entities;

import java.io.Serializable;

public class OperationGlassEntity implements Serializable {

    private String OperationNum;
    private String WorkOrderNum;
    private String ShortText;
    private String EarlSchStartExecDate;
    private String EarlSchStartExecTime;
    private String EarlSchFinishExecDate;
    private String EarlSchFinishExecTime;
    private String MobileStatus;

    public OperationGlassEntity() {
    }

    public String getOperationNum() {
        return OperationNum;
    }

    public void setOperationNum(String operationNum) {
        OperationNum = operationNum;
    }

    public String getWorkOrderNum() {
        return WorkOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        WorkOrderNum = workOrderNum;
    }

    public String getShortText() {
        return ShortText;
    }

    public void setShortText(String shortText) {
        ShortText = shortText;
    }

    public String getEarlSchStartExecDate() {
        return EarlSchStartExecDate;
    }

    public void setEarlSchStartExecDate(String earlSchStartExecDate) {
        EarlSchStartExecDate = earlSchStartExecDate;
    }

    public String getEarlSchStartExecTime() {
        return EarlSchStartExecTime;
    }

    public void setEarlSchStartExecTime(String earlSchStartExecTime) {
        EarlSchStartExecTime = earlSchStartExecTime;
    }

    public String getEarlSchFinishExecDate() {
        return EarlSchFinishExecDate;
    }

    public void setEarlSchFinishExecDate(String earlSchFinishExecDate) {
        EarlSchFinishExecDate = earlSchFinishExecDate;
    }

    public String getEarlSchFinishExecTime() {
        return EarlSchFinishExecTime;
    }

    public void setEarlSchFinishExecTime(String earlSchFinishExecTime) {
        EarlSchFinishExecTime = earlSchFinishExecTime;
    }
    public String getMobileStatus() {
        return MobileStatus;
    }

    public void setMobileStatus(String mobileStatus) {
        MobileStatus = mobileStatus;
    }
}