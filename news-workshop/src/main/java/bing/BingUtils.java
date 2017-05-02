package bing;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class BingUtils {

    public static String extractDisplaylUrl(String url) {
        try {
            Map<String, String> queryParams = getQueryParams(url);
            if(queryParams.containsKey("r") && StringUtils.isNotBlank(queryParams.get("r"))){
                return queryParams.get("r");
            }

        }catch (Exception exp){
            log.warn("failed to extract display url from '{}', caught an exception: '{}'",url,exp);
        }
        return url;
    }


    public static Map<String, String> getQueryParams(String url) throws UnsupportedEncodingException, MalformedURLException {
        String query = new URL(url).getQuery();
        Map<String, String> query_pairs = new LinkedHashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }
}
