package com.example.admin.block.item.controller;

import com.example.admin.common.response.PageResult;
import com.example.admin.common.response.StatusResult;
import com.example.admin.block.item.dto.DeleteBlockItemDto;
import com.example.admin.block.item.dto.InsertBlockItemDto;
import com.example.admin.block.item.dto.BlockItem;
import com.example.admin.block.item.service.BlockItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/block/item")
public class BlockItemController {
    private final BlockItemService blockItemService;

    @PostMapping
    public StatusResult insertBlockItem(@RequestBody @Valid InsertBlockItemDto dto) {
        blockItemService.insertBlockItem(dto);

        return new StatusResult(true);
    }

    @GetMapping
    public PageResult<BlockItem> getBlockItemList(@RequestParam("dcb") @Valid String dcb,
                                                  @RequestParam("item") @Valid String item,
                                                  @RequestParam("page") @Valid int page,
                                                  @RequestParam("pageSize") @Valid int pageSize) {
        Page<BlockItem> itemPage = blockItemService.getBlockItemList(dcb, item, page, pageSize);

        return new PageResult<>(true, itemPage);
    }

    @DeleteMapping
    public StatusResult deleteBlockItem(@RequestBody @Valid DeleteBlockItemDto dto) {
        blockItemService.deleteBlockItem(dto);

        return new StatusResult(true);
    }
}
