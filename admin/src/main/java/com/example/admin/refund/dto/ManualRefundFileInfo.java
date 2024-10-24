package com.example.admin.refund.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ManualRefundFileInfo {
    private String requestName;
    private String responseName;
    private Integer retry;
    private Date updateDt;
    private Date createDt;

    public void setResponseName(String responseName) {
        this.responseName = responseName;
    }

    public void retry() {
        this.retry++;
    }

    public void firstTry() {
        this.retry = 1;
    }
}