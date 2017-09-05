package com.charmingwong.cwimage.search.converter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.charmingwong.cwimage.search.model.HotSearch;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by CharmingWong on 2017/6/2.
 */

public class HotSearchesConverterFactory extends Converter.Factory {

    private static final String TAG = "SuggestionsJsonConverterFactory";

    private HotSearchesConverterFactory() {
    }

    public static HotSearchesConverterFactory create() {
        return new HotSearchesConverterFactory();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new HotSearchesConverter();
    }

    private class HotSearchesConverter implements Converter<ResponseBody, List<HotSearch>> {
        @Override
        public List<HotSearch> convert(@NonNull ResponseBody value) throws IOException {
            try {

                String result = value.string();
                Document document = Jsoup.parse(result);
                Elements elements = document.getElementsByAttributeValue("class", "img_single_box");

                if (elements.size() == 0) {
                    return null;
                }

                List<HotSearch> hotSearches = new ArrayList<>(elements.size());
                for (Element element : elements) {
                    String imageUrl = element.getElementsByTag("img").get(0).attr("src");
                    String query = element.getElementsByAttributeValue("class", "img_instr_layer").get(0).text();
                    hotSearches.add(new HotSearch(imageUrl, query));
                }
                return hotSearches;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
