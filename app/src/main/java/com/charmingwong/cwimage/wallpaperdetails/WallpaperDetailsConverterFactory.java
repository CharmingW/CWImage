package com.charmingwong.cwimage.wallpaperdetails;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by CharmingWong on 2017/6/8.
 */

public class WallpaperDetailsConverterFactory extends Converter.Factory {

    private static final String TAG = "WallpaperDetailsConverterFactory";

    private WallpaperDetailsConverterFactory() {
    }

    public static WallpaperDetailsConverterFactory create() {
        return new WallpaperDetailsConverterFactory();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new WallpaperDetailsConverter();
    }

    private class WallpaperDetailsConverter implements Converter<ResponseBody, Wallpaper> {

        @Override
        public Wallpaper convert(@NonNull ResponseBody value) throws IOException {
            try {
                String result = value.string();
                Document document = Jsoup.parse(result);

                Element image = document.getElementById("bigImg");

                if (image == null) {
                    image = document.getElementsByTag("img").get(0);
                }

                String imageUrl = image.attr("src");

                JSONObject object = new JSONObject();
                object.put("width", 720);
                object.put("height", 1280);
                object.put("url", imageUrl);
                object.put("thumbUrl", imageUrl);

                return new Wallpaper(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
