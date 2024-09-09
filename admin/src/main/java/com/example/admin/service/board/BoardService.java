package com.example.admin.service.board;

import com.example.admin.domain.dto.board.AddBoardDto;
import com.example.admin.domain.dto.board.BoardDto;
import com.example.admin.domain.entity.board.Board;
import com.example.admin.repository.mapper.board.BoardMapper;
import com.example.admin.service.reference.BoardReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardService {
    private static final Map<Integer, BoardDto> boardMap = new HashMap<>();
    private final BoardMapper boardMapper;
    private final BoardReference boardReference;

    public Map<Integer, BoardDto> getAllBoards() {
        List<Board> boardList = boardMapper.getBoardList();

        while (!boardList.isEmpty()) {
            Board board = boardList.get(0);
            BoardDto boardDto = BoardDto.toDto(board);

            addBoardMap(boardDto);

            boardList.remove(0);
        }
        return boardMap;
    }

    private void addBoardMap(BoardDto boardDto) {
        if (!boardDto.hasParentId()) {
            BoardDto parentBoardDto = boardMap.get(boardDto.getParentBoardId());
            checkChildListIsEmpty(parentBoardDto);

            parentBoardDto.addChild(boardDto);
        }
        boardMap.put(boardDto.getBoardId(), boardDto);
    }

    private void checkChildListIsEmpty(BoardDto boardDto) {
        if (boardDto.hasChildBoardList()) {
            boardDto.addNewChildBoardList();
        }
    }

    public void addBoard(AddBoardDto addBoardDto) {
        if (!boardReference.checkDuplicatedBoard(addBoardDto)) {
            boardMapper.insertBoard(addBoardDto);
        }
    }
}
