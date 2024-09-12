package com.postvan.models;

import com.fasterxml.jackson.annotation.JsonKey;
import com.postvan.models.base.RuntimeSafePOJO;
import lombok.Data;

import java.util.Objects;

@Data
public class PostVanCertificateProperties extends RuntimeSafePOJO {
    private String certificatePath;
    private String secretKeyPath;
    private String keystorePath;
    private String secretKeyPassphrase;

    public PostVanCertificatePropertiesStrategy getCertificateStrategy() {
        if (Objects.isNull(this.secretKeyPassphrase)) {
            throw new IllegalStateException("Cannot use certificate without a passphrase!");
        }
        return this.keystorePath == null ? PostVanCertificatePropertiesStrategy.KEY_CERT_PAIR : PostVanCertificatePropertiesStrategy.PKCS12_FILE;
    }
}
