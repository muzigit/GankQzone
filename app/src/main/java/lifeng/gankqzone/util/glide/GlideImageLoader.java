package lifeng.gankqzone.util.glide;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.loader.ImageLoader;

import lifeng.gankqzone.MyApplication;


public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        GlideUtils.LoadImage(MyApplication.getContext(), (String) path, imageView);
    }

//    @Override
//    public ImageView createImageView(Context context) {
//        //圆角
//        return new RoundAngleImageView(context);
//    }
}
