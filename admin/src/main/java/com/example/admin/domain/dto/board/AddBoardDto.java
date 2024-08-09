package com.example.admin.domain.dto.board;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddBoardDto {
    private String boardName;
    private String categoryType;
    private Integer boardDepth;
    private Integer parentBoardId;
}
