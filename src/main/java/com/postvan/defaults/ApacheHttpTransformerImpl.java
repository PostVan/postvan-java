package com.postvan.defaults;

import com.postvan.models.PostmanEnvironment;
import com.postvan.models.PostmanRequest;
import com.postvan.models.PostvanRequestTransformer;
import com.postvan.models.base.PostmanValue;
import lombok.val;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.http.client.methods.*;
import org.apache.http.message.BasicHeader;

import java.util.Base64;
import java.util.Objects;

public class ApacheHttpTransformerImpl implements PostvanRequestTransformer<HttpUriRequest> {
    final PostmanEnvironment defaultEnvironment = new PostmanEnvironment();
    @Override
    public HttpUriRequest transform(final PostmanRequest request) {
        val method = request.getMethod();
        val url = request.getUrl().getRaw();
        switch (method) {
            case GET:
                return buildHttpGet(request);
            case POST:
                return new HttpPost(url);
            case PUT:
                return new HttpPut(url);
            case PATCH:
                return new HttpPatch(url);
            case DELETE:
                return new HttpDelete(url);
            default:
                throw new IllegalArgumentException("Cannot find HTTP method!");
        }
    }

    private HttpUriRequest buildHttpGet(final PostmanRequest request) {
        val httpGet = new HttpGet(defaultEnvironment.replacer(request.getUrl().getRaw()));
        val auth = request.getAuth();
        if (Objects.nonNull(auth)) {
            switch (auth.getType()) {
                case BASIC:
                    val userName = auth.getAuthValues().stream()
                            .filter(authValue -> StringUtils.equals(authValue.getKey(), "username"))
                            .map(PostmanValue::getValue)
                            .map(defaultEnvironment::replacer)
                            .findAny();
                    val password = auth.getAuthValues().stream()
                            .filter(authValue -> StringUtils.equals(authValue.getKey(), "password"))
                            .map(PostmanValue::getValue)
                            .map(defaultEnvironment::replacer)
                            .findAny();
                    if (userName.isPresent() && password.isPresent()) {
                        val basicAuth = Base64.getEncoder().encodeToString(StringUtils.getBytesUtf8(String.format("%s:%s", userName.get(), password.get())));
                        val headerValue = String.format("Basic %s", basicAuth);
                        httpGet.setHeader(new BasicHeader("Authorization", headerValue));
                    }
                    break;
                case BEARER:
                    val token = auth.getAuthValues().stream()
                            .filter(authValue -> StringUtils.equals(authValue.getKey(), "token"))
                            .map(PostmanValue::getValue)
                            .findAny();
                    if (token.isPresent()) {
                        val headerValue = String.format("Bearer %s", token.get());
                        httpGet.setHeader(new BasicHeader("Authorization", headerValue));
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Cannot find auth type handler!");
            }
        }
        return httpGet;
    }
}
