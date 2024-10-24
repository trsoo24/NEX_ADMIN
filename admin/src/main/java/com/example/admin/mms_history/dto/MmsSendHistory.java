package com.example.admin.mms_history.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MmsSendHistory {
    private String createDt;
    private Integer seq;
    private Integer partitionId;
    private Integer tryCnt;
    private String sendYn;
    private String sendDt;
    private String sendResult;
    private String fromCtn; // default : "019-114"
    private String toCtn;
    private String requestId;
    private String content;
}
