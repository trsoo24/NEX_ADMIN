package com.example.admin.domain.entity.gdcb.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JobType {
    /** DB에 존재하는 경우 */
    DB_Type,
    /** TLO 파일에만 존재하는 경우 */
    File_Type
}
