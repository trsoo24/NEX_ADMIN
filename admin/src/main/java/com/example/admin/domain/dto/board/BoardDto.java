package com.example.admin.domain.dto.board;

import com.example.admin.domain.entity.board.Board;
import lombok.*;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDto {
    private Integer boardId;
    private String boardName;
    private Integer depth;
    private Integer parentBoardId;
    private List<BoardDto> childBoardList;

    public static BoardDto toDto(Board board) {
        return BoardDto.builder()
                .boardId(board.getBoardId())
                .boardName(board.getBoardName())
                .parentBoardId(board.getParentBoardId())
                .depth(board.getBoardDepth())
                .build();
    }

    public void addNewChildBoardList() {
        this.childBoardList = new ArrayList<>();
    }

    public void addChild(BoardDto boardDto) {
        this.getChildBoardList().add(boardDto);
    }

    public boolean hasChildBoardList() {
        return ObjectUtils.isEmpty(this.childBoardList);
    }

    public boolean hasParentId() {
        return this.parentBoardId == null;
    }
}
