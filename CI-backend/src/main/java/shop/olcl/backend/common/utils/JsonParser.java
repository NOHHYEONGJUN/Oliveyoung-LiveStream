package shop.olcl.backend.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class JsonParser {

    public static List<String> parseRecommendedItems(String jsonString) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonString);

        JsonNode recommendedItemsNode = rootNode.path("output").path("recommendedItems");
        List<String> recommendedItems = objectMapper.convertValue(recommendedItemsNode, List.class);

        return recommendedItems;
    }
}
