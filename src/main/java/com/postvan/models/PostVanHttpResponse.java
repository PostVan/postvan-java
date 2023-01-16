package com.postvan.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.http.HttpResponse;

@Data
@AllArgsConstructor
public class PostVanHttpResponse {
    private HttpResponse rawResponse;
    private String responseBody;
    private int statusCode;
}
