package com.recceasy.idp.layers.tenant.utils;

import com.recceasy.idp.utils.Utils;

public class TenantUtils {
    public static String nextTenantId(String tenantPrefix, Long sequence) {
        return String.format("%s%s%04d", tenantPrefix, Utils.getSaltString(), sequence);
    }
}
