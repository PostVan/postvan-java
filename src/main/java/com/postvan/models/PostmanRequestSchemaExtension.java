package com.postvan.models;

import com.postvan.models.base.RuntimeSafePOJO;
import lombok.Data;

import java.util.List;

@Data
public class PostmanRequestSchemaExtension extends RuntimeSafePOJO {
    private PostVanPaginationProperties pagination;
}
