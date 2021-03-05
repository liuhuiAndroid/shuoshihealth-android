package com.ssh.shuoshi.ui.imagegallery;

import android.text.TextUtils;

import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.ui.BaseActivity;
import com.tencent.mmkv.MMKV;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;

/**
 * 图片画廊
 */
public class ImageGalleryActivity extends BaseActivity {

    @BindView(R.id.viewPager2)
    ViewPager2 viewPager2;

    @Override
    public int initContentView() {
        return R.layout.activity_image_gallery;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initUiAndListener() {
        String images = MMKV.defaultMMKV().getString("imageUrls","");
        if (images != null && !TextUtils.isEmpty(images)) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>() {
            }.getType();
            List<String> imageUrls = gson.fromJson(images, type);
            ImageGalleryAdapter adapter = new ImageGalleryAdapter();
            viewPager2.setAdapter(adapter);

            adapter.setOnClickListener(new ImageGalleryAdapter.OnClickListener() {
                @Override
                public void onClick() {
                    finish();
                }
            });

            adapter.setImageUrls(imageUrls);
            adapter.notifyDataSetChanged();

        }
    }
}
