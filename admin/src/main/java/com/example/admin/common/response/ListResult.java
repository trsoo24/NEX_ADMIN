package com.example.admin.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListResult<T> extends StatusResult {
    private List<T> dataList;

    public ListResult(Boolean success, List<T> dataList) {
        super(success);
        this.dataList = dataList;
    }
}

