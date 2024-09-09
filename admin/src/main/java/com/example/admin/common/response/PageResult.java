package com.example.admin.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> extends StatusResult {
    private Page<T> responsePage;

    public PageResult(Boolean success, Page<T> responsePage) {
        super(success);
        this.responsePage = responsePage;
    }
}
