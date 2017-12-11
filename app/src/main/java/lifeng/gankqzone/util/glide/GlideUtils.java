package lifeng.gankqzone.util.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import lifeng.gankqzone.R;

/**
 * Glide工具类
 * Created by 坤 on 2016/9/1.
 */
public class GlideUtils {

    /**
     * 普通加载(所有图片当前静态图加载)
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void disPlay(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .asBitmap()
                .apply(new RequestOptions()
                        .error(R.mipmap.image_error)
                        .placeholder(R.mipmap.default_pic)
                )
                /*** 设置缩略图 ***/
                .thumbnail(0.2f)
                .load(url)
                .into(imageView);
    }

    public static void LoadImage(final Context context, Object url, final ImageView imageView) {
        Glide.with(context).load(url)
                .apply(new RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .transition(new DrawableTransitionOptions().crossFade(800))
                .into(imageView);
    }

    public static void LoadImages(final Context context, Object url, SimpleTarget<Drawable> st) {
        Glide.with(context).load(url)
                .apply(new RequestOptions()
                        .centerCrop()
//                        .override(1000,1000)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .transition(new DrawableTransitionOptions().crossFade(800))
                .into(st);
    }

    /**
     * 需要回调时使用
     *
     * @param context         上下文
     * @param url             图片链接
     * @param imageViewTarget 回调需求
     */
    public static void LoadImage(Context context, Object url, ImageViewTarget imageViewTarget) {
        Glide.with(context).load(url)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .transition(new DrawableTransitionOptions().crossFade(800))
                .into(imageViewTarget);
    }

    /**
     * 需要回调时使用
     *
     * @param context   上下文
     * @param url       图片链接
     * @param imageView 回调需求
     */
    public static void LoadImage(Context context, Object url, ImageView imageView, RequestListener listener) {
        Glide.with(context).load(url)
                //.thumbnail(0.1f)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .transition(new DrawableTransitionOptions().crossFade(800))
                .listener(listener)
                .into(imageView);
    }



    /**
     * 加载圆形图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void disPlayCircleImage(Context context, String url, ImageView imageView) {
        RequestOptions requestOptions = RequestOptions.bitmapTransform(new CropCircleTransformation(context));
        Glide.with(context).load(url)
                .apply(requestOptions
                        .error(R.mipmap.image_error)
                        .placeholder(R.mipmap.default_pic)
                )
                .into(imageView);
    }

    /**
     * 显示加载中的视图
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void disPlayShowLoading(Context context, String url, final ImageView imageView, final ProgressBar progressBar) {
        Glide.with(context)
                .asBitmap()
                .apply(new RequestOptions()
                        .error(R.mipmap.image_error)
                        .placeholder(R.mipmap.default_pic)
                )
                /*** 设置动画 ***/
                /*** 设置缩略图 ***/
                .thumbnail(0.1f)
                .load(url)
                /*** 如果确定ImageView需要显示的固定大小 可以作为参数传递 ***/
//                .into(new SimpleTarget<Bitmap>(SizeUtils.dp2px(100), SizeUtils.dp2px(100)) {
//                    @Override
//                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
//                        if (progressBar != null) {
//                            progressBar.setVisibility(View.GONE);
//                        }
//                        /*** 后台处理完成之后  设置图片到ImageView中 ***/
//                        imageView.setImageBitmap(bitmap);
//                    }
//                });
                .into(new SimpleTarget<Bitmap>(SizeUtils.dp2px(100), SizeUtils.dp2px(100)) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        /*** 后台处理完成之后  设置图片到ImageView中 ***/
                        imageView.setImageBitmap(bitmap);
                    }
                });
    }


    /**
     * 不显示加载中视图
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void disPlayNoShowLoading(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .asBitmap()
                .apply(new RequestOptions()
                        .error(R.mipmap.image_error)
                        .placeholder(R.mipmap.default_pic)
                )
                /*** 设置动画 ***/
//                .animate(R.anim.set_image_anim)
                /*** 设置缩略图 ***/
                .load(url)
//                .thumbnail(0.5f)
                /*** 如果确定ImageView需要显示的固定大小 可以作为参数传递 ***/
                .into(imageView);
    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param url
     * @param imageView
     * @param round
     */
    public static void test(Context context, String url, ImageView imageView, int round) {
        Glide.with(context)
                /*** Glide可以加载本地视频  filePath:必须是本地视频路径 非本地视频无法加载***/
//                .load( Uri.fromFile( new File(filePath)))
                /*** 单独使用load()会检查图片是否是Gif ***/
                .load(url)

                /*** 不会检查图片是否是Gif图片 即所有图片按照Gif图片加载 非Gif图片会当成error处理 ***/
//                .asGif()
                /*** 将所有图片作为普通图片加载 如果是gif图片则取Gif的第一帧显示 ***/
//                .asBitmap()

                /*** 占位图 ***/
//                .placeholder(R.mipmap.ic_launcher)
                /*** 错误占位图 ***/
//                .error(R.mipmap.ic_launcher)
                /*** Glide默认就是有一个淡入淡出的动画 crossFade:强制使用一个淡入淡出动画 dontAnimate:直接显示图片,没有动画效果 ***/
//                .crossFade()
                /*** 将图片重新设置为指定大小 ***/
//                .override(600, 200)
                /*** 将图片填充到 ImageView 界限内并且裁剪额外的部分 ImageView可能会完全填充，但图像可能不会完整显示 ***/
//                .centerCrop()
                /*** 将图片测量出来等于或小于 ImageView 的边界范围 图片将会完全显示，但可能不会填满整个 ImageView ***/
//                .fitCenter()
                /*** true:跳过内存缓存 但Glide会将重复网络请求添加到磁盘中去(Glide默认将图片缓存到内存中) ***/
//                .skipMemoryCache(true)
                /*** 跳过磁盘缓存:
                 * DiskCacheStrategy.NONE:什么都不缓存
                 * DiskCacheStrategy.SOURCE: 仅仅只缓存原来的全分辨率的图像
                 * DiskCacheStrategy.RESULT: 仅仅缓存最终的图像(降低分辨率之后的)
                 * DiskCacheStrategy.ALL:全部缓存(Glide默认磁盘缓存策略)
                 注:如果内存缓存和磁盘缓存都需禁用,则两个都需要调用 如只是单独调用其中一个,Glide则会默认启用另一个进行缓存
                 ***/
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
                /*** thumbnail:缩略图功能 将图片按照其原始分辨率的指定倍数进行缩略 ***/
//                .thumbnail(0.1f)
                .into(imageView);
    }

    /**
     * 给图标染上当前提示文本的颜色并且转出Bitmap
     *
     * @param resources
     * @param context
     * @return
     */
    public static Drawable changeDrawableColor(int resources, Context context) {
        final Drawable drawable = ContextCompat.getDrawable(context, resources);
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);

        /********************getCurrentHintTextColor() 将图标渲染成当前输入框的颜色********************/
//        DrawableCompat.setTint(wrappedDrawable, getCurrentHintTextColor());
        DrawableCompat.setTint(wrappedDrawable, context.getResources().getColor(R.color.color_949494));
        return wrappedDrawable;
    }


    /**
     * 清除缓存
     */
    public static void cleanCache(final Context context) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            //清除内存缓存需要在Ui线程执行
            Glide.get(context).clearMemory();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //清除磁盘缓存需要在非ui线程执行
                    Glide.get(context.getApplicationContext()).clearDiskCache();
                }
            }).start();
        } else {
            new Handler(context.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Glide.get(context).clearMemory();
                }
            });
            Glide.get(context.getApplicationContext()).clearDiskCache();
        }
    }
}

