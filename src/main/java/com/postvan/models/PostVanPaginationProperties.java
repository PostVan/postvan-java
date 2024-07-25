package com.postvan.models;

import com.postvan.models.base.RuntimeSafePOJO;
import lombok.Data;

@Data
public class PostVanPaginationProperties extends RuntimeSafePOJO {
    private String nextProperty;
}
