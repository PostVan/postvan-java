package com.postvan.models;

import com.postvan.models.base.RuntimeSafePOJO;
import lombok.Data;

@Data
public class PostVanPaginationProperties extends RuntimeSafePOJO {
    private String nextProperty;
    private String totalCountProperty;
    private Long offsetSize;
    private String offsetProperty;
    private Long pageSize;
    private String pageSizeProperty;
}
