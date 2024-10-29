package com.example.admin.type_limit.controller;

import com.example.admin.common.response.ListResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.type_limit.dto.GetTypeLimitDto;
import com.example.admin.type_limit.dto.UpdateTypeLimitDto;
import com.example.admin.type_limit.service.TypeLimitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/limit")
@RequiredArgsConstructor
public class TypeLimitController {
    private final TypeLimitService typeLimitService;

    @GetMapping
    public ListResult<GetTypeLimitDto> getTypeLimit() {
        List<GetTypeLimitDto> typeLimitDtoList = typeLimitService.getTypeLimitDtoList();

        return new ListResult<>(true, typeLimitDtoList);
    }

    @PutMapping
    public StatusResult updateTypeLimit(@RequestBody @Valid UpdateTypeLimitDto typeLimitDto) {
        typeLimitService.updateTypeLimit(typeLimitDto);

        return new StatusResult(true);
    }
}
