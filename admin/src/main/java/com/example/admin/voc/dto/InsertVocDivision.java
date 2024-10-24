package com.example.admin.voc.dto;

import com.example.admin.voc.dto.type.Classification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertVocDivision {
    private Date regDate;
    private String ctn;
    private String dcb;
    private Classification division1;
    private Classification division2;
    private Classification division3;
    private String contents;
    private String answer1;
    private String answer2;
    private String answer3;
    private String writer;

    public void setCtn(String ctn) {
        this.ctn = ctn;
    }
}
