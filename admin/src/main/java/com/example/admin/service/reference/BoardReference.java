package com.example.admin.service.reference;

import com.example.admin.domain.dto.board.AddBoardDto;
import com.example.admin.repository.mapper.board.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardReference {
    private final BoardMapper boardMapper;

    public boolean checkDuplicatedBoard(AddBoardDto addBoardDto) {
        return boardMapper.duplicateBoard(addBoardDto);
    }
}
