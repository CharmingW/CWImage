package com.charmingwong.cwimage.wallpaper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by CharmingWong on 2017/6/7.
 */

public class WallpaperConverterFactory extends Converter.Factory {


    private static final String TAG = "WallpaperConverterFactory";

    private WallpaperConverterFactory() {
    }

    public static WallpaperConverterFactory create() {
        return new WallpaperConverterFactory();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new WallpaperConverter();
    }

    private class WallpaperConverter implements Converter<ResponseBody, List<WallpaperCover>> {

        @Override
        public List<WallpaperCover> convert(@NonNull ResponseBody value) throws IOException {
            try {
                String result = new String(value.bytes(), "gb2312");

                Log.i(TAG, "convert: " + result);

                Document document = Jsoup.parse(result);
                Elements items = document.getElementsByAttributeValue("class", "photo-list-padding");

                List<WallpaperCover> wallpaperCovers = new ArrayList<>(items.size());

                for (Element item : items) {
                    Element image = item.getElementsByTag("img").get(0);
                    Pattern pattern = Pattern.compile("\\d+");
                    Matcher matcher = pattern.matcher(image.attr("width"));
                    int width = 0, height = 0;
                    if (matcher.find()) {
                        width = Integer.valueOf(matcher.group());
                    }
                    matcher = pattern.matcher(image.attr("height"));
                    if (matcher.find()) {
                        height = Integer.valueOf(matcher.group());
                    }
                    String imageUrl = image.attr("src");
                    String title = item.getElementsByTag("span").text();
                    String detailsUrl = item.getElementsByAttributeValue("class", "pic").get(0).attr("href");
                    JSONObject object = new JSONObject();
                    object.put("width", width);
                    object.put("height", height);
                    object.put("imageUrl", imageUrl);
                    object.put("title", title);
                    object.put("detailsUrl", detailsUrl);
                    wallpaperCovers.add(new WallpaperCover(object));
                }

                return wallpaperCovers;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
