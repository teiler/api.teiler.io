package io.teiler.server.endpoint;

import static spark.Spark.get;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.teiler.server.util.GsonUtil;
import org.springframework.stereotype.Controller;

/**
 * Controller for the index endpoint.
 * 
 * @author lroellin
 */
@Controller
public class IndexEndpointController implements EndpointController {

    private Gson gson = GsonUtil.getHomebrewGson();

    @Override
    public void register() {
        /**
         * Returns a description of our versions. This could've been implemented by normal Java objects
         * and then serialising them with GSON, but it's a bit overkill.
         */
        get("/", (req, res) -> {
            JsonObject json = new JsonObject();
            json.addProperty("documentation", "doc.teiler.io");

            JsonArray versions = new JsonArray();
            JsonObject v1 = new JsonObject();
            v1.addProperty("prefix", "v1");
            v1.addProperty("status", "current");
            versions.add(v1);
            JsonObject v2 = new JsonObject();
            v2.addProperty("prefix", "v2");
            v2.addProperty("status", "future");
            versions.add(v2);
            json.add("versions", versions);

            return gson.toJson(json);
        });
    }

}
