package com.myudog.myulib.api.core.event;

public interface IFailableEvent extends IEvent {
    String getErrorMessage();

    void setErrorMessage(String errorMessage);
}
