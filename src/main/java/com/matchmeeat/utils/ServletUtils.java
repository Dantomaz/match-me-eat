package com.matchmeeat.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

public final class ServletUtils {

    private ServletUtils() {
    }

    public static String getVerificationUrl(HttpServletRequest httpServletRequest, String verificationToken) {
        String baseServerUrl = ServletUtils.getBaseServerUrl(httpServletRequest);
        String contextPath = httpServletRequest.getContextPath();
        String verifyEndpoint = "/auth/verify?token=" + verificationToken;
        return StringUtils.join(baseServerUrl, contextPath, verifyEndpoint);
    }

    public static String getBaseServerUrl(HttpServletRequest httpServletRequest) {
        String protocol = httpServletRequest.getScheme();
        String serverName = httpServletRequest.getServerName();
        int serverPort = httpServletRequest.getServerPort();
        return StringUtils.join(protocol, "://", serverName, ":", serverPort);
    }
}
