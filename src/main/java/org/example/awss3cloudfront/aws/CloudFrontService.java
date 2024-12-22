package org.example.awss3cloudfront.aws;

import com.amazonaws.services.cloudfront.CloudFrontUrlSigner;
import com.amazonaws.services.cloudfront.util.SignerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class CloudFrontService {

    @Value("${aws.cloudfront.domain}")
    private String domain;

    @Value("${aws.cloudfront.private-key-path}")
    private String privateKeyPath;

    @Value("${aws.cloudfront.key-id}")
    private String keyId;

    public String generateSignedUrl(String objectKey) throws InvalidKeySpecException, IOException {
        Date expiration = new Date(System.currentTimeMillis() + (1000 * 60 * 20));
        File privateKey = new ClassPathResource(privateKeyPath).getFile();

        return CloudFrontUrlSigner.getSignedURLWithCannedPolicy(
                SignerUtils.Protocol.https,
                domain,
                privateKey,
                objectKey,
                keyId,
                expiration
        );
    }
}
