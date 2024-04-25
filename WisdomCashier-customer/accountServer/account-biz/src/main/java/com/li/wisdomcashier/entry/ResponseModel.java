package com.li.wisdomcashier.entry;

import com.li.wisdomcashier.enums.RepCodeEnum;
import com.li.wisdomcashier.utils.CommonUtils;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseModel implements Serializable {
    private static final long serialVersionUID = 8445617032523881407L;
    private String repCode;
    private String repMsg;
    private Object repData;

    public ResponseModel() {
        this.repCode = RepCodeEnum.SUCCESS.getCode();
    }

    public ResponseModel(RepCodeEnum repCodeEnum) {
        this.setRepCodeEnum(repCodeEnum);
    }

    public static ResponseModel success() {
        return successMsg("成功");
    }

    public static ResponseModel successMsg(String message) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepMsg(message);
        return responseModel;
    }

    public static ResponseModel successData(Object data) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepCode(RepCodeEnum.SUCCESS.getCode());
        responseModel.setRepData(data);
        return responseModel;
    }

    public static ResponseModel errorMsg(RepCodeEnum message) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepCodeEnum(message);
        return responseModel;
    }

    public static ResponseModel errorMsg(String message) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepCode(RepCodeEnum.ERROR.getCode());
        responseModel.setRepMsg(message);
        return responseModel;
    }

    public static ResponseModel errorMsg(RepCodeEnum repCodeEnum, String message) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepCode(repCodeEnum.getCode());
        responseModel.setRepMsg(message);
        return responseModel;
    }

    public static ResponseModel exceptionMsg(String message) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRepCode(RepCodeEnum.EXCEPTION.getCode());
        responseModel.setRepMsg(RepCodeEnum.EXCEPTION.getDesc() + ": " + message);
        return responseModel;
    }

    public String toString() {
        return "ResponseModel{repCode='" + this.repCode + '\'' + ", repMsg='" + this.repMsg + '\'' + ", repData=" + this.repData + '}';
    }

    public boolean isSuccess() {
        return CommonUtils.compare(this.repCode, RepCodeEnum.SUCCESS.getCode());
    }

    public String getRepCode() {
        return this.repCode;
    }

    public void setRepCode(String repCode) {
        this.repCode = repCode;
    }

    public void setRepCodeEnum(RepCodeEnum repCodeEnum) {
        this.repCode = repCodeEnum.getCode();
        this.repMsg = repCodeEnum.getDesc();
    }

    public String getRepMsg() {
        return this.repMsg;
    }

    public void setRepMsg(String repMsg) {
        this.repMsg = repMsg;
    }

    public Object getRepData() {
        return this.repData;
    }

    public void setRepData(Object repData) {
        this.repData = repData;
    }
}