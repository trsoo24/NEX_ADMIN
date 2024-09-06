package com.example.admin.controller;

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
    public ResponseEntity<Map<Integer, BoardDto>> getAllBoards() {
        log.info("가보자고");
        return ResponseEntity.ok(boardService.getAllBoards());
    }


    @PostMapping
    public String addNewBoard(@RequestBody AddBoardDto addBoardDto) {
        return boardService.addBoard(addBoardDto);
    }
}
