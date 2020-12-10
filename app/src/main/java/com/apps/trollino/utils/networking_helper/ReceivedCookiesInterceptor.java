package com.apps.trollino.utils.networking_helper;

import android.content.Context;
import android.preference.PreferenceManager;

import com.apps.trollino.R;
import com.apps.trollino.utils.data.PrefUtils;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class ReceivedCookiesInterceptor implements Interceptor {
    protected PrefUtils prefUtils;
    private Context context;

    public ReceivedCookiesInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookiesHashSet = (HashSet<String>) PreferenceManager.getDefaultSharedPreferences(context).getStringSet("PREF_COOKIES", new HashSet<>());

            for (String header : originalResponse.headers("Set-Cookie")) {
                cookiesHashSet.add(header);
            }

            prefUtils = new PrefUtils(context.getSharedPreferences(context.getString(R.string.app_name), MODE_PRIVATE));
            String cookies = cookiesHashSet.toString();
            prefUtils.saveCookie(cookies.substring(1, cookies.length()-1));
        }

        return originalResponse;
    }
}
