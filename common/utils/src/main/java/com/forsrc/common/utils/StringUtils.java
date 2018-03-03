package com.forsrc.common.utils;

import java.util.UUID;

public class StringUtils {

    public static boolean isEmpty(String str) {
        return str != null && !"".equals(str) && !"".equals(str.trim());
    }

    public static String toUuidString(String value) {
        return value.indexOf("-") > 0 ? value
                : value.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
    }

    public static UUID toUuid(String uuid) {
        return UUID.fromString(toUuidString(uuid));
    }
}
