package com.postvan.models;

import com.postvan.models.base.RuntimeSafePOJO;
import lombok.Data;

@Data
public class PostmanSchemaExtension extends RuntimeSafePOJO {
    private PostVanPaginationProperties pagination;
}
