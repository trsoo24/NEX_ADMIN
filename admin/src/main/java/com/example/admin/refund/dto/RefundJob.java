package com.example.admin.refund.dto;

import com.example.admin.auth.dto.AuthInfo;
import com.example.admin.refund.dto.type.JobType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefundJob {
    private JobType currentType;
    private AuthInfo auth;
    private String path;
    private String requestFile;
    private String responseFile;
    private String billing;


    public RefundJob (JobType jobType) {
        this.currentType = jobType;
    }
}
