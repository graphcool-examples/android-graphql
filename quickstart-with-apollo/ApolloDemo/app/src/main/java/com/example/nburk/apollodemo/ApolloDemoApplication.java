package com.example.nburk.apollodemo;

import android.app.Application;
import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.ResponseField;
import com.apollographql.apollo.cache.normalized.CacheKey;
import com.apollographql.apollo.cache.normalized.CacheKeyResolver;
import com.apollographql.apollo.cache.normalized.NormalizedCacheFactory;
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy;
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory;

import java.util.Map;

import javax.annotation.Nonnull;

import okhttp3.OkHttpClient;

public class ApolloDemoApplication extends Application {

    // replace __SIMPLE_API_ENDPOINT__ with the endpoint of your service's GraphQL API
    // looks like: https://api.graph.cool/simple/v1/__SERVICE_ID__
    private static final String BASE_URL = "__SIMPLE_API_ENDPOINT__";
    public static final String TAG = "graphcool";

    private ApolloClient apolloClient;
    private static Context context;

    public static Context getAppContext() {
        return ApolloDemoApplication.context;
    }

    @Override public void onCreate() {
        super.onCreate();
        ApolloDemoApplication.context = getApplicationContext();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        NormalizedCacheFactory cacheFactory = new LruNormalizedCacheFactory(EvictionPolicy.builder().maxSizeBytes(10 * 1024).build());
        CacheKeyResolver cacheKeyResolver = new CacheKeyResolver() {
            @Nonnull
            @Override
            public CacheKey fromFieldRecordSet(@Nonnull ResponseField field, @Nonnull Map<String, Object> recordSet) {
                if (recordSet.containsKey("id")) {
                    String id = (String) recordSet.get("id");
                    return CacheKey.from(id);
                }
                return CacheKey.NO_KEY;
            }

            @Nonnull @Override
            public CacheKey fromFieldArguments(@Nonnull ResponseField field, @Nonnull Operation.Variables variables) {
                return CacheKey.NO_KEY;
            }
        };

        apolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .normalizedCache(cacheFactory, cacheKeyResolver)
                .okHttpClient(okHttpClient)
                .build();
    }

    public ApolloClient apolloClient() {
        return apolloClient;
    }

}
