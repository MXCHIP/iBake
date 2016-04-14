package com.mxchip.helpers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxchip.activities.ibake.R;
import com.mxchip.manage.ConstHelper;

/**
 * Created by Rocke on 2016/03/25.
 */
public class GetDelicacy {
    private SyncImageLoader syncImageLoader;
    private Activity mactivity;

    private TextView txt1;
    private TextView txt2;
    private TextView txt3;
    private TextView txt4;
    private TextView txt5;

    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private ImageView iv4;
    private ImageView iv5;

    public GetDelicacy(Activity activity) {
        syncImageLoader = new SyncImageLoader();
        this.mactivity = activity;
        initView();
    }

    private void initView() {
        txt1 = (TextView) mactivity.findViewById(R.id.delicacy_1_text);
        txt2 = (TextView) mactivity.findViewById(R.id.delicacy_2_text);
        txt3 = (TextView) mactivity.findViewById(R.id.delicacy_3_text);
        txt4 = (TextView) mactivity.findViewById(R.id.delicacy_4_text);
        txt5 = (TextView) mactivity.findViewById(R.id.delicacy_5_text);

        iv1 = (ImageView) mactivity.findViewById(R.id.delicacy_1_pic);
        iv2 = (ImageView) mactivity.findViewById(R.id.delicacy_2_pic);
        iv3 = (ImageView) mactivity.findViewById(R.id.delicacy_3_pic);
        iv4 = (ImageView) mactivity.findViewById(R.id.delicacy_4_pic);
        iv5 = (ImageView) mactivity.findViewById(R.id.delicacy_5_pic);
    }

    public void refreshDelicy() {

        String imgurl1 = "http://sh.sinaimg.cn/2011/1115/U5839P18DT20111115095540.jpg";
        String imgurl2 = "http://img4.duitang.com/uploads/item/201301/10/20130110005302_vmrLc.thumb.600_0.jpeg";
        String pic = "SkImageDecoder Factory returned ";

        for (int i = 1; i < 6; i++) {
            switch (i) {
                case 1:
                    txt1.setText(i + pic);
                    break;
                case 2:
                    txt2.setText(i + pic);
                    break;
                case 3:
                    txt3.setText(i + pic);
                    break;
                case 4:
                    txt4.setText(i + pic);
                    break;
                case 5:
                    txt5.setText(i + pic);
                    break;
            }
            if (i % 2 == 1)
                syncImageLoader.loadImage(i, null, imgurl1, imageLoadListener);
            else
                syncImageLoader.loadImage(i, null, imgurl2, imageLoadListener);
        }
    }

    SyncImageLoader.OnImageLoadListener imageLoadListener = new SyncImageLoader.OnImageLoadListener() {

        @Override
        public void onImageLoad(BookModel model, Integer i, Drawable drawable) {

            final Bitmap bitmap = ConstHelper.drawable2Bitmap(drawable);
            switch (i) {
                case 1:
                    iv1.setImageBitmap(bitmap);
                    break;
                case 2:
                    iv2.setImageBitmap(bitmap);
                    break;
                case 3:
                    iv3.setImageBitmap(bitmap);
                    break;
                case 4:
                    iv4.setImageBitmap(bitmap);
                    break;
                case 5:
                    iv5.setImageBitmap(bitmap);
                    break;
            }
//                setimg.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
        }

        @Override
        public void onError(Integer t) {
//            BookModel model = (BookModel) getItem(t);
//            View view = mListView.findViewWithTag(model);
//            if (view != null) {
//                ImageView iv = (ImageView) view.findViewById(R.id.dev_item_imgid);
//                iv.setBackgroundResource(R.drawable.mydevice_icon_device_online);
//            }
        }
    };
}
