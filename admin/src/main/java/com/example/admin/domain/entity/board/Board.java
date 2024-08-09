package com.example.admin.domain.entity.board;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Board {
    private Integer boardId;
    private String boardName;
    private String categoryType; // DCB 종류 ( 미정 )
    private Integer boardDepth; // 메뉴 Depth
    private Integer parentBoardId;
}
