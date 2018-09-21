package com.charmingwong.cwimage.imagechannel;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

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
 * Created by CharmingWong on 2017/5/15.
 */

public class ChannelImageJsonConverterFactory extends Converter.Factory {

    private static final String TAG = "ChannelConverterFactory";

    public static ChannelImageJsonConverterFactory create() {
        return new ChannelImageJsonConverterFactory();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new ChannelImageJsonConverter();
    }

    private final class ChannelImageJsonConverter implements Converter<ResponseBody, List<ChannelImage>> {

        @Override
        public List<ChannelImage> convert(@NonNull ResponseBody value) throws IOException {
            try {
                String result = value.string();
                JSONObject jsonObject = new JSONObject(result);
                Log.i(TAG, "convert: " + result);
                Log.i(TAG, "convert: " + jsonObject.getInt("lastid"));
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                List<ChannelImage> images = new ArrayList<>(jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    images.add(new ChannelImage(jsonArray.getJSONObject(i)));
                }
                return images;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "图片频道 json 解析出错");
            }
            return new ArrayList<>(0);
        }
    }
}
