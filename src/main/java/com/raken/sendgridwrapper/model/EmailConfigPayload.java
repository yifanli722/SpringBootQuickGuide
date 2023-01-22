package com.raken.sendgridwrapper.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EmailConfigPayload {
    private String To;
    private List<String> CC;
    private List<String> BCC;
    private String Subject;
    private String Body;

    @JsonCreator
    public EmailConfigPayload(@JsonProperty("To") String To, @JsonProperty("CC") List<String> CC,
                              @JsonProperty("BCC") List<String> BCC, @JsonProperty("Subject") String Subject,
                              @JsonProperty("Body") String Body) {
        this.To = To;
        this.CC = CC;
        this.BCC = BCC;
        this.Subject = Subject;
        this.Body = Body;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public List<String> getCC() {
        return CC;
    }

    public void setCC(List<String> CC) {
        this.CC = CC;
    }

    public List<String> getBCC() {
        return BCC;
    }

    public void setBCC(List<String> BCC) {
        this.BCC = BCC;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    @Override
    public String toString() {
        return "EmailConfigPayload{" +
                "To='" + To + '\'' +
                ", CC=" + CC +
                ", BCC=" + BCC +
                ", Subject='" + Subject + '\'' +
                ", Body='" + Body + '\'' +
                '}';
    }
}

