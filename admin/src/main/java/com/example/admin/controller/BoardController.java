package com.example.admin.controller;

import com.example.admin.common.response.MapResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.domain.dto.board.AddBoardDto;
import com.example.admin.domain.dto.board.BoardDto;
import com.example.admin.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @GetMapping
    public MapResult<Integer, BoardDto> getAllBoards() {
        Map<Integer, BoardDto> boardDtoMap = boardService.getAllBoards();

        return new MapResult<>(true, boardDtoMap);
    }


    @PostMapping
    public StatusResult addNewBoard(@RequestBody AddBoardDto addBoardDto) {
        boardService.addBoard(addBoardDto);

        return new StatusResult(true);
    }
}
