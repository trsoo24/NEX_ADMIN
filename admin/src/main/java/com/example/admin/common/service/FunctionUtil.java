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

    public static String maskingCtn(String ctn) {
        // 숫자만 추출
        String cleanCtn = ctn.replaceAll("[^0-9]", "");

        if (cleanCtn.length() == 10) { // 010-123-4567 형식
            // 원래 하이픈이 있었는지 확인하고, 하이픈을 추가한 마스킹 처리
            return ctn.contains("-") ? cleanCtn.replaceAll("(\\d{3})(\\d{3})(\\d{4})", "$1-***-$3")
                    : cleanCtn.replaceAll("(\\d{3})(\\d{3})(\\d{4})", "$1***$3");
        } else if (cleanCtn.length() == 11) { // 010-1234-5678 형식
            return ctn.contains("-") ? cleanCtn.replaceAll("(\\d{3})(\\d{4})(\\d{4})", "$1-****-$3")
                    : cleanCtn.replaceAll("(\\d{3})(\\d{4})(\\d{4})", "$1****$3");
        } else if (cleanCtn.length() == 12) { // 0100-1234-5678 형식
            return ctn.contains("-")
                    ? cleanCtn.replaceAll("(\\d{4})(\\d{4})(\\d{4})", "$1-****-$3")
                    : cleanCtn.replaceAll("(\\d{4})(\\d{4})(\\d{4})", "$1****$3");
        }
        return ctn;
    }
}
