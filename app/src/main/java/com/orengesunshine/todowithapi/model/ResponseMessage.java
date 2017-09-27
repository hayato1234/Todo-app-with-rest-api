package com.orengesunshine.todowithapi.model;

public class ResponseMessage {

    private boolean isError;
    private String message;

    public boolean isError() {
        return isError;
    }

    public String getMessage() {
        return message;
    }
}
