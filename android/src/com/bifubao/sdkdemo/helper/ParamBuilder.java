package com.bifubao.sdkdemo.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ParamBuilder {
    private static final String paramsEncoding = "UTF-8";

    public static ParamBuilder create() {
        ParamBuilder bd = new ParamBuilder();
        bd.parameters = new TreeMap<String, String>();
        return bd;
    }

    public static ParamBuilder create(Map<String, String> map) {
        ParamBuilder bd = create();
        bd.parameters.putAll(map);
        return bd;
    }

    private Map<String, String> parameters;

    public ParamBuilder put(String key, String value) {
        parameters.put(key, value);
        return this;
    }

    public ParamBuilder put(String key, Object value) {
        parameters.put(key, String.valueOf(value));
        return this;
    }

    public String populateForSign(String... ignoreKeys) {
        StringBuilder sb = new StringBuilder();
        if (parameters != null) {
            Set<String> igkeys = new HashSet<String>();
            if (ignoreKeys != null) {
                for (String str: igkeys) {
                    igkeys.add(str);
                }
            }
            for (String key: parameters.keySet()) {
                if (igkeys.contains(key)) {
                    continue;
                }
                sb.append(key);
                sb.append(parameters.get(key));
            }
        }
        return sb.toString();
    }

    public String buildQuery() throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        if (parameters != null) {
            for (String key: parameters.keySet()) {
                addKeyValue(sb, key, parameters.get(key));
                sb.append('&');
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        return sb.toString();
    }

    private void addKeyValue(StringBuilder sb, String key, String value)
            throws UnsupportedEncodingException {
        sb.append(URLEncoder.encode(key, paramsEncoding));
        sb.append('=');
        sb.append(URLEncoder.encode(value, paramsEncoding));
    }
}
