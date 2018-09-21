package com.charmingwong.cwimage.search.converter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.charmingwong.cwimage.search.ImageSearchSuggestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by CharmingWong on 2017/5/21.
 */

public class SuggestionsJsonConverterFactory extends Converter.Factory {

    private static final String TAG = "SuggestionsFactory";

    private SuggestionsJsonConverterFactory() {
    }

    public static SuggestionsJsonConverterFactory create() {
        return new SuggestionsJsonConverterFactory();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new SuggestionsJsonConverter();
    }

    private class SuggestionsJsonConverter implements Converter<ResponseBody, List<ImageSearchSuggestion>> {
        @Override
        public List<ImageSearchSuggestion> convert(@NonNull ResponseBody value) throws IOException {
            String result = value.string();
            Log.i(TAG, "convert: " + result);
            result = result.substring(result.indexOf('{'), result.lastIndexOf('}') + 1);
            Log.i(TAG, "convert: " + result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("s");
                List<ImageSearchSuggestion> suggestions = new ArrayList<>(jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    suggestions.add(new ImageSearchSuggestion(jsonArray.getString(i)));
                }
                return suggestions;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new ArrayList<>(0);
        }
    }

}
