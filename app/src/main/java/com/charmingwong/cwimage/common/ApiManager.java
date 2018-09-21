package com.charmingwong.cwimage.common;

import retrofit2.Converter.Factory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * <pre>
 *     author: Charming Wong
 *     github: https://github.com/CharmingW
 *     blog  : http://www.jianshu.com/u/05686c7c92af & http://blog.csdn.net/CharmingWong
 *     公众号 ： Charming写字的地方
 *     time  : 17-9-27
 *     desc  :
 * </pre>
 */
public class ApiManager {

    private static final String TAG = "ApiManager";

    private static final String BASE_URL = "https://www.baidu.com/";

    private static ApiManager sInstance;

    private ApiManager() {
    }

    public static ApiManager getInstance() {
        if (sInstance == null) {
            synchronized (ApiManager.class) {
                if (sInstance == null) {
                    sInstance = new ApiManager();
                }
            }
        }
        return sInstance;
    }

    public JsonRequestService getJsonRequestService(Factory converterFactory) {
        return new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(JsonRequestService.class);
    }

}
