package com.example.admin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FunctionUtil {
    public <T> Page<T> toPage(List<T> list, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());

        if (start >= end) {
            end = list.size();
            start = (list.size() / pageSize) * pageSize;
            pageable = PageRequest.of(list.size() / pageSize, pageSize);
        }

        List<T> ts = list.subList(start, end);
        return new PageImpl<>(ts, pageable, list.size());
    }
}
