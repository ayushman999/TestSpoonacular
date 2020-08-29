package com.example.android.testspoonacular;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class spoonacularUtils {
    private static final String LOG_TAG = spoonacularUtils.class.getSimpleName();
    public spoonacularUtils() {
    }
    public static List<Elements> fetchdata(String requestUrl) throws IOException, JSONException {
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        jsonResponse = makeHttpRequest(url);

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<Elements> data = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Earthquake}s
        return data;
    }

    private static List<Elements> extractFeatureFromJson(String jsonResponse) throws JSONException {
        ArrayList<Elements> data=new ArrayList<>();
        String title="";
        String imageUrl="";
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        try {
            JSONArray baseArray = new JSONArray(jsonResponse);
            for (int i = 0; i < baseArray.length(); i++) {
                JSONObject currentObject = baseArray.getJSONObject(i);
                title = currentObject.getString("title");
                imageUrl = currentObject.getString("image");
                JSONArray missedIngridients = currentObject.getJSONArray("missedIngredients");
                JSONArray usedIngridients = currentObject.getJSONArray("usedIngredients");
                ArrayList<String> missed=new ArrayList<>();
                ArrayList<String> used=new ArrayList<>();
                for (int j = 0; j < missedIngridients.length(); j++) {
                    JSONObject current_ing = missedIngridients.getJSONObject(j);
                    String ing = current_ing.getString("name");
                    missed.add(ing);
                }
                for (int j = 0; j < usedIngridients.length(); j++) {
                    JSONObject current_ing = usedIngridients.getJSONObject(j);
                    String ing = current_ing.getString("name");
                    used.add(ing);
                }
                data.add(new Elements(imageUrl, title, missed, used));

            }
        }
        catch (JSONException e)
        {
            Log.i("spoonacularUtils","problem in parsing data");
        }
        return data;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }
}
