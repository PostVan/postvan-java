package com.postvan.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.postvan.models.base.RuntimeSafePOJO;
import lombok.Data;
import lombok.val;

import java.util.HashMap;
import java.util.Map;

@Data
public class PostmanInfoSchemaExtension extends RuntimeSafePOJO {
    @JsonDeserialize
    private Map<String, PostVanCertificateProperties> certificates = new HashMap<>();

    private void addCertificateInfo(final String url, final PostVanCertificateProperties certificateProperties) {
        assert url.startsWith("https://");
        this.certificates.put(url, certificateProperties);
    }

    private PostVanCertificateProperties getCertificateInfoForUrl(final String url) {
        return this.certificates.get(url);
    }
}
