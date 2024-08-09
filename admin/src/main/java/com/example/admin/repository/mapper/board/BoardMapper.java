package com.example.admin.repository.mapper.board;

import com.example.admin.domain.dto.board.AddBoardDto;
import com.example.admin.domain.entity.board.Board;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<Board> getBoardList();

    void insertBoard(AddBoardDto addBoardDto);

    boolean duplicateBoard(AddBoardDto addBoardDto);
}
