package com.postvan.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.http.HttpResponse;


@Data
@AllArgsConstructor
public class PostVanHttpResponse {
    private HttpResponse responseBody;
    private String rawResponse;
    private int statusCode;
}
