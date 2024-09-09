package com.example.admin.common.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MapResult<K, V> extends StatusResult{
    private Map<K, V> mapResult;

    public MapResult(Boolean success, Map<K, V> mapResult) {
        super(success);
        this.mapResult = mapResult;
    }
}
