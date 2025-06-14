package com.kyle.kyleimagesearchmcpserver.tools;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Haoran Wang
 * @date 2025/6/14 15:19
 */
@Service
public class ImageSearchTool {
    private static final String API_KEY = "your api key";

    private static final String API_URL = "https://api.pexels.com/v1/search";

    @Tool(description = "The tool for searching image from web")
    public String searchImage(@ToolParam(description = "Search query keyword") String keyword) {
        try{
            return String.join("," , searchImagesWithMediumSize(keyword));
        }catch (Exception e){
            return "Error Searching Image" + e.getMessage();
        }
    }

    public List<String> searchImagesWithMediumSize(String query) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", API_KEY);
        Map<String, Object> params = new HashMap<>();
        params.put("query", query);

        String response = HttpUtil.createGet(API_URL)
                .addHeaders(headers)
                .form(params)
                .execute()
                .body();
        return JSONUtil.parseObj(response)
                .getJSONArray("photos")
                .stream()
                .map(o -> (JSONObject) o)
                .map(o -> o.getJSONObject("src"))
                .map(o -> o.getStr("medium"))
                .collect(Collectors.toList());
    }

}
