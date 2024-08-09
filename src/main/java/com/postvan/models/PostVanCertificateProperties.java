package com.postvan.models;

import com.fasterxml.jackson.annotation.JsonKey;
import com.postvan.models.base.RuntimeSafePOJO;
import lombok.Data;

@Data
public class PostVanCertificateProperties extends RuntimeSafePOJO {
    private String certificatePath;
    private String secretKeyPath;
    private String keystorePath;
    private String secretKeyPassphrase;

    public PostVanCertificatePropertiesStrategy getCertificateStrategy() {
        assert this.secretKeyPassphrase != null;
        return this.keystorePath == null ? PostVanCertificatePropertiesStrategy.KEY_CERT_PAIR : PostVanCertificatePropertiesStrategy.PKCS12_FILE;
    }
}
