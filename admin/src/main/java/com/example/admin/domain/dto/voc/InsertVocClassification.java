package com.example.admin.domain.dto.voc;

import com.example.admin.domain.entity.voc.type.Classification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertVocClassification {
    private Date regDate;
    private String ctn;
    private String dcb;
    private Classification classification1;
    private Classification classification2;
    private Classification classification3;
    private String content;
    private String answer1;
    private String answer2;
    private String answer3;

    public void setCtn(String ctn) {
        this.ctn = ctn;
    }
}