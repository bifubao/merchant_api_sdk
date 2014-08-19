package com.bifubao.sdkdemo.helper;

import java.io.UnsupportedEncodingException;

import com.dearcoin.wallet.sdk.api.common.Utils;

public class OrderHelper {

    private ParamBuilder builder;

    private String key;

    public OrderHelper(String pid, String key) {
        builder = ParamBuilder.create();
        builder.put("_pid_", pid);
        this.key = key;
    }

    public void setOrderId(String orderId) {
        builder.put("external_order_id", orderId);
    }

    public void setPriceBtc(String priceBtc) {
        builder.put("price_btc", priceBtc);
    }

    public void setPriceCny(String priceCny) {
        builder.put("price_cny", priceCny);
    }

    public void setDisplayDesc(String displayDesc) {
        builder.put("display_desc", displayDesc);
    }

    public void setDisplayName(String diaplayName) {
        builder.put("display_name", diaplayName);
    }

    public void setExternalInfo(String externalInfo) {
        builder.put("external_info", externalInfo);
    }

    public void setExternalCallbackUrl(String externalCallbackUrl) {
        builder.put("external_callback_url", externalCallbackUrl);
    }

    public void setExternalRedirectUrl(String externalRedirectUrl) {
        builder.put("external_redirect_url", externalRedirectUrl);
    }

    public String buildPostParameter() throws UnsupportedEncodingException {
        builder.put("_time_", System.currentTimeMillis() / 1000);
        String sign = builder.populateForSign("_checksum_");
        builder.put("_checksum_", Utils.md5(sign + key));
        return builder.buildQuery();
    }

}
