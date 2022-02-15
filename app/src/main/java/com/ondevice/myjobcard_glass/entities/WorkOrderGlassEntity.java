package com.ondevice.myjobcard_glass.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WorkOrderGlassEntity implements Serializable {

    private String WorkOrderNum;
    private String OrderType;
    private String ShortText;
    private String Priority;
    private String EquipNum;
    private String FuncLocation;
    private String MobileObjStatus;
    private String AddressNumber;
    private String WOAddressNumber;
    private String StatusFlag;
    private String UserStatus;

    List<OperationGlassEntity> oprentities;
    List<ComponentGlassEntity> componentGlassEntities;

    public String getWorkOrderNum() {
        return WorkOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        WorkOrderNum = workOrderNum;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getShortText() {
        return ShortText;
    }

    public void setShortText(String shortText) {
        ShortText = shortText;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }

    public String getEquipNum() {
        return EquipNum;
    }

    public void setEquipNum(String equipNum) {
        EquipNum = equipNum;
    }

    public String getFuncLocation() {
        return FuncLocation;
    }

    public void setFuncLocation(String funcLocation) {
        FuncLocation = funcLocation;
    }

    public String getMobileObjStatus() {
        return MobileObjStatus;
    }

    public void setMobileObjStatus(String mobileObjStatus) {
        MobileObjStatus = mobileObjStatus;
    }

    public String getAddressNumber() {
        return AddressNumber;
    }

    public void setAddressNumber(String addressNumber) {
        AddressNumber = addressNumber;
    }

    public String getWOAddressNumber() {
        return WOAddressNumber;
    }

    public void setWOAddressNumber(String WOAddressNumber) {
        this.WOAddressNumber = WOAddressNumber;
    }

    public String getStatusFlag() {
        return StatusFlag;
    }

    public void setStatusFlag(String statusFlag) {
        StatusFlag = statusFlag;
    }

    public String getUserStatus() {
        return UserStatus;
    }

    public void setUserStatus(String userStatus) {
        UserStatus = userStatus;
    }

    public List<OperationGlassEntity> getOprentities() {
        return oprentities;
    }

    public void setOprentities(List<OperationGlassEntity> oprentities) {
        this.oprentities = oprentities;
    }

    public List<ComponentGlassEntity> getComponentGlassEntities() {
        return componentGlassEntities;
    }

    public void setComponentGlassEntities(List<ComponentGlassEntity> componentGlassEntities) {
        this.componentGlassEntities = componentGlassEntities;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}