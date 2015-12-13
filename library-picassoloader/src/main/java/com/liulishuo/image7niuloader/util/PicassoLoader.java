package com.liulishuo.image7niuloader.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

/**
 * Copyright (c) 2015 LingoChamp Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by Jacksgong on 12/13/15.
 */
public class PicassoLoader {

    private final static String TAG = "ImageLoader";

    /**
     * 设置全局的默认占位图
     *
     * @param defaultPlaceHolder
     * @param defaultAvatarPlaceHolder
     */
    public static void setGlobalPlaceHolder(final int defaultPlaceHolder, final int defaultAvatarPlaceHolder) {
        PicassoImage7NiuLoader.DEFAULT_PLACE_HOLDER = defaultPlaceHolder;
        PicassoImage7NiuLoader.DEFAULT_AVATAR_PLACE_HOLDER = defaultAvatarPlaceHolder;
    }

    public static void setGlobalTargetProvider(final TargetProvider provider) {
        PicassoImage7NiuLoader.DEFAULT_TARGET_PROVIDER = provider;
    }

    public static PicassoImage7NiuLoader display7NiuAvatar(final ImageView imageView, final String oriUrl) {
        PicassoImage7NiuLoader image7NiuLoader = new PicassoImage7NiuLoader(imageView, oriUrl);
        return image7NiuLoader.avatar();
    }

    public static PicassoImage7NiuLoader display7Niu(final ImageView imageView, final String oriUrl) {
        return new PicassoImage7NiuLoader(imageView, oriUrl);
    }

    public static PicassoImage7NiuLoader display7Niu(final ImageView imageView, final String oriUrl, @DrawableRes final int defaultDrawable) {
        PicassoImage7NiuLoader image7NiuLoader = new PicassoImage7NiuLoader(imageView, oriUrl);
        image7NiuLoader.defaultD(defaultDrawable);
        return image7NiuLoader;
    }

    static void display(ImageView imageView, String url, Drawable drawable, final Transformation transformation, Target target, Callback callback) {
        if (imageView == null) {
            return;
        }


        //最终都是在这里触发
        attachToImage(imageView, url, drawable, transformation, target, callback);
    }

    static void fetch(final Context context, String url, FetchCallBack callback) {
        RequestCreator creator = attach(context, url);

        if (creator == null) {
            Log.e(TAG, String.format("creator == null : url[%s], context[%s]", url, context));
            return;
        }

        if (callback == null) {
            creator.fetch();
        } else {
            creator.into(callback);
        }

    }


    static void attachToImage(final ImageView imageView, final String url, final Drawable placeHolder, final Transformation transformation, final Target target, final Callback callback) {
        if (imageView == null) {
            return;
        }

        final RequestCreator creator = attach(imageView.getContext(), url, placeHolder);
        if (creator == null) {
            return;
        }

        if (transformation != null) {
            creator.transform(transformation);
        }

        if (target != null) {
            creator.tag(target);
        }

        try {
            creator.into(imageView, callback);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    static RequestCreator attach(final Context context, String url) {
        return attach(context, url, null);
    }

    static RequestCreator attach(final Context context, String url, final Drawable placeHolder) {
        if (context == null) {
            return null;
        }

        try {
            url = url == null ? null :
                    (url.trim().length() <= 0 ? null : url);


            if (url != null && !url.startsWith("http")) {
                url = "file:" + url;
            }

            RequestCreator creator = Picasso.with(context)
                    .load(url);

            if (placeHolder != null) {
                creator.placeholder(placeHolder);
            }

            return creator;

        } catch (Throwable e) {
            e.printStackTrace();
        }


        return null;
    }

    public interface TargetProvider {
        Target get(final String originUrl, final Context context);
    }

    public static class FetchCallBack implements Target {

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }
}
