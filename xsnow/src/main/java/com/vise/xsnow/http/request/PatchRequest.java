package com.vise.xsnow.http.request;

import android.content.Context;

import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.mode.CacheResult;
import com.vise.xsnow.http.subscriber.ApiCallbackSubscriber;

import java.lang.reflect.Type;

import rx.Observable;
import rx.Subscription;

/**
 * @Description: Patch请求
 * @author: <a href="http://www.xiaoyaoyou1212.com">DAWI</a>
 * @date: 17/5/14 20:29.
 */
public class PatchRequest extends BaseRequest<PatchRequest> {
    @Override
    protected <T> Observable<T> execute(Type type) {
        return apiService.patch(suffixUrl, params).compose(this.<T>norTransformer(type));
    }

    @Override
    protected <T> Observable<CacheResult<T>> cacheExecute(Type type) {
        return this.<T>execute(type).compose(ViseHttp.getInstance().getApiCache().<T>transformer(cacheMode, type));
    }

    @Override
    protected <T> Subscription execute(Context context, ACallback<T> callback) {
        if (isLocalCache) {
            return this.cacheExecute(getSubType(callback))
                    .subscribe(new ApiCallbackSubscriber(context, callback));
        }
        return this.execute(getType(callback))
                .subscribe(new ApiCallbackSubscriber(context, callback));
    }
}