package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from serialization
public class Response {
    private boolean isSuccess;

    private String msg;

    private String respCode;

    public Object respData;

    public Object getRespData() {
        return respData;
    }

    public void setRespData(Object respData) {
        this.respData = respData;
    }

    public Response() {

    }

    public Response(boolean isSuccess, String msg, String respCode, Object respData) {
        this.isSuccess = isSuccess;
        this.msg = msg;
        this.respCode = respCode;
        this.respData = respData;
    }

    public boolean isIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    //    @Override
//    public String toString() {
//        if (respData != null && respData instanceof List) {
//            return "Response [" + "isSuccess=" + isSuccess + ", message=" + msg + ", responseCode=" + respCode
//                    + ", responseData ListSize=" + ((List) respData).size() + "]";
//        } else {
//            return "Response [" + "isSuccess=" + isSuccess + ", message=" + msg + ", responseCode=" + respCode + "]";
//        }
//    }
    @Override
    public String toString() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // Returning the JSON string of the object (with pretty-printing)
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (Exception e) {
            return "Error converting to JSON";
        }
    }
}
