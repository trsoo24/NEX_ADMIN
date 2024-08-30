package com.example.admin.service.item;

import com.example.admin.repository.mapper.item.ItemStatsMonthlyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemStatsMonthlyService {
    private final ItemStatsMonthlyMapper itemStatsMonthlyMapper;
}
