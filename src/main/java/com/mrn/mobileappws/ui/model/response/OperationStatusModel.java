package com.mrn.mobileappws.ui.model.response;

// Will return the operation status(success or fail) and operation name
// that we handle
public class OperationStatusModel {

    private String operationResult;
    private String operationName;

    public String getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(String operationResult) {
        this.operationResult = operationResult;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
}
