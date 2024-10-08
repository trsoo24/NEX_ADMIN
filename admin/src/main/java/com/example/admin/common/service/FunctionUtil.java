package com.example.admin.common.service;

import jakarta.servlet.http.HttpServletResponse;
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

    public String transCtn(String ctn) {
        if (ctn.contains("-")) {
            ctn = ctn.replace("-", "");
        }

        if (ctn.length() == 12) {
            return ctn.replace(ctn.charAt(3) + "", "");
        } else {
            return ctn;
        }
    }
}
