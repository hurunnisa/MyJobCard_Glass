package com.ondevice.myjobcard_glass.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ComponentGlassEntity implements Serializable {

    private String WorkOrderNum;
    private int WithdrawalQty;
    private String Reservation;
    private int ReqmtQty;
    private String Item;
    private String Material;
    private String MaterialDescription;

    public String getWorkOrderNum() {
        return WorkOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        WorkOrderNum = workOrderNum;
    }
    public int getWithdrawalQty() {
        return WithdrawalQty;
    }

    public void setWithdrawalQty(int withdrawalQty) {
        WithdrawalQty = withdrawalQty;
    }

    public int getReqmtQty() {
        return ReqmtQty;
    }

    public void setReqmtQty(int  reqmtQty) {
        ReqmtQty = reqmtQty;
    }

    public String getReservation() {
        return Reservation;
    }

    public void setReservation(String reservation) {
        Reservation = reservation;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }

    public String getMaterialDescription() {
        return MaterialDescription;
    }

    public void setMaterialDescription(String materialDescription) {
        MaterialDescription = materialDescription;
    }

    public String getMaterial() {
        return Material;
    }

    public void setMaterial(String material) {
        Material = material;
    }

}
