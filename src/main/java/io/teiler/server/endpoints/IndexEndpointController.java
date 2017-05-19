/**
 * MIT License
 *
 * Copyright (c) 2017 L. Röllin, P. Bächli, K. Thurairatnam & D. Thoma
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.teiler.server.endpoints;

import static spark.Spark.get;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.teiler.server.util.HomebrewGson;
import org.springframework.stereotype.Controller;

/**
 * Controller for the index endpoint.
 *
 * @author lroellin
 */
@Controller
public class IndexEndpointController implements EndpointController {

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

            return HomebrewGson.getInstance().toJson(json);
        });
    }

}
