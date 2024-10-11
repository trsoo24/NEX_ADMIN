package com.example.admin.domain.dto.gdcb;

import com.example.admin.domain.entity.gdcb.AuthInfo;
import com.example.admin.domain.entity.gdcb.type.JobType;
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
