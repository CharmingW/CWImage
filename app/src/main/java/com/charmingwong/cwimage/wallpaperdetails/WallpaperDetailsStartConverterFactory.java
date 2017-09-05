package com.charmingwong.cwimage.wallpaperdetails;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by CharmingWong on 2017/6/9.
 */

public class WallpaperDetailsStartConverterFactory extends Converter.Factory {


    private static final String TAG = "WallpaperDetailsStartConverterFactory";

    private final String size;

    private WallpaperDetailsStartConverterFactory(String size) {
        this.size = size;
    }

    public static WallpaperDetailsStartConverterFactory create(String size) {
        return new WallpaperDetailsStartConverterFactory(size);
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new WallpaperDetailsStartConverter();
    }

    private class WallpaperDetailsStartConverter implements Converter<ResponseBody, List<String>> {

        @Override
        public List<String> convert(@NonNull ResponseBody value) throws IOException {
            String result = value.string();
            List<String> urls = new ArrayList<>();
            Document document = Jsoup.parse(result);

            Element image;
            if ("".equals(size) || (!"".equals(size) && document.getElementById(size) == null)) {
                image = document
                        .getElementsByAttributeValue("class","model wallpaper-down clearfix")
                        .get(0)
                        .getElementsByTag("dd")
                        .get(0)
                        .getElementsByTag("a")
                        .get(0);
            } else {
                image = document.getElementById(size);
            }

            Elements elements = document.getElementById("showImg").getElementsByTag("li");

            if (image != null && image.hasAttr("id")) {
                urls.add(image.attr("href"));
            } else {
                urls.add(elements.get(0).getElementsByTag("a").get(0).attr("href"));
            }

            for (Element element : elements) {
                urls.add(element.getElementsByTag("a").get(0).attr("href"));
            }

            return urls;
        }
    }
}
