package com.example.admin.voc.dto;

import com.example.admin.voc.dto.type.Classification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVocHistoryDto {
    private Integer vocId;
    private Classification classification1;
    private Classification classification2;
    private Classification classification3;
    private String content;
    private String answer1;
    private String answer2;
    private String answer3;
}
