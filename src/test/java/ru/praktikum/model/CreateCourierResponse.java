package ru.praktikum.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateCourierResponse {


    @JsonProperty("ok")
    private Boolean ok;


    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }
}