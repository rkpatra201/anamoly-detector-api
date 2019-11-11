package com.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class EventResponse extends Event {
    private Status status;
    private String cause;
    private String message;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
