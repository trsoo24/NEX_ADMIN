package com.example.admin.mms_history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MmsHistoryDto {
    private String createDt;
    private String ctn;
    private String content;
    private String sendResult;

    public void setCtnBlind() {
        switch (this.ctn.length()) {
            case 10 : replaceToStar(3, 5, this.ctn);
            break;
            case 11 : replaceToStar(3, 6, this.ctn);
            break;
            case 12 : replaceToStar(4, 7, this.ctn);
            break;
        }
    }

    private void replaceToStar(int startIdx, int endIdx, String ctn) {
        String head = ctn.substring(0, startIdx);
        String star = "*".repeat(endIdx - startIdx + 1);
        String tail = ctn.substring(endIdx + 1);

        this.ctn = head + star + tail;
    }
}
