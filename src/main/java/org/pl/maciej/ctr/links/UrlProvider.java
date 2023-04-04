package org.pl.maciej.ctr.links;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlProvider {

    private final IdProvider idProvider;
    private final String prefix;
    private final String absoluteUrl;

    public UrlProvider(@Value("${ctr.relative-prefix:/links}") String prefix,
                       @Value("${ctr.absolute-url:http://localhost:8080}") String absoluteUrl,
                       IdProvider idProvider) {
        this.prefix = prefix;
        this.absoluteUrl = absoluteUrl;
        this.idProvider = idProvider;
    }

    public String generateUniqueIdentifier(String targetUrl) {
        return idProvider.getUnique(targetUrl);
    }

    public String getRelativeUrl(String uniqeIdentifier) {
        return String.format("%s/%s", prefix, uniqeIdentifier);
    }

    public String getAbsolute(String relativeUrl) {
        return String.format("%s%s/%s", absoluteUrl, prefix, relativeUrl);
    }
}
