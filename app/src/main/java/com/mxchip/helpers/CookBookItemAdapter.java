package com.mxchip.helpers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mxchip.activities.ibake.R;
import com.mxchip.activities.ibake.RecipeDetailActivity;
import com.mxchip.manage.ConstHelper;
import com.mxchip.manage.ConstPara;

import java.util.Vector;

/**
 * Created by Rocke on 2016/03/29.
 */
public class CookBookItemAdapter extends BaseAdapter {

    private String TAG = "---BookItemAdapter---";

    private LayoutInflater mInflater;
    private Activity mContext;
    private Vector<BookModel> mModels = new Vector<BookModel>();
    private ListView mListView;
    SyncImageLoader syncImageLoader;

    public CookBookItemAdapter(Activity context, ListView listView) {
        mInflater = LayoutInflater.from(context);
        syncImageLoader = new SyncImageLoader();
        mContext = context;
        mListView = listView;

        mListView.setOnScrollListener(onScrollListener);
    }

    public void addBook(String cb_name, String cb_img, String cb_likeno, String cb_recipeid) {
        BookModel model = new BookModel();
        model.cb_name = cb_name;
        model.cb_img = cb_img;
        model.cb_likeno = cb_likeno;
        model.cb_recipeid = cb_recipeid;
        mModels.add(model);
    }

    public void clean() {
        mModels.clear();
    }

    @Override
    public int getCount() {
        return mModels.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= getCount()) {
            return null;
        }
        return mModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.cloud_cookbook_list_item, null);
        }
        final BookModel model = mModels.get(position);
        convertView.setTag(position);
        LinearLayout cb_layout = (LinearLayout) convertView.findViewById(R.id.cloud_cb_item_lyid);

        TextView cloud_cb_item_txt = (TextView) convertView.findViewById(R.id.cloud_cb_item_txt);
        TextView cloud_cb_item_like_no = (TextView) convertView.findViewById(R.id.cloud_cb_item_like_no);

        cloud_cb_item_txt.setText(model.cb_name);
        cloud_cb_item_like_no.setText(model.cb_likeno);

        if (ConstHelper.checkPara(model.cb_img))
            syncImageLoader.loadImage(position, model, model.cb_img, imageLoadListener);

        cb_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RecipeDetailActivity.class);
                intent.putExtra(ConstPara.INTENT_RECIPENAME, model.cb_name);
                intent.putExtra(ConstPara.INTENT_RECIPEID, model.cb_recipeid);
                mContext.startActivity(intent);
                mContext.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        return convertView;
    }

    SyncImageLoader.OnImageLoadListener imageLoadListener = new SyncImageLoader.OnImageLoadListener() {

        @Override
        public void onImageLoad(final BookModel model, Integer t, final Drawable drawable) {
            View view = mListView.findViewWithTag(t);
            if (view != null) {
                ImageView cloud_cb_item_pic = (ImageView) view.findViewById(R.id.cloud_cb_item_pic);
                final Bitmap bitmap = ConstHelper.drawable2Bitmap(drawable);
            //    cloud_cb_item_pic.setBackgroundDrawable(drawable);
                cloud_cb_item_pic.setImageBitmap(bitmap);
            }
        }

        @Override
        public void onError(Integer t) {
            BookModel model = (BookModel) getItem(t);
            View view = mListView.findViewWithTag(model);
            if (view != null) {
                ImageView cloud_cb_item_pic = (ImageView) view.findViewById(R.id.cloud_cb_item_pic);
                cloud_cb_item_pic.setBackgroundResource(R.drawable.empty_recipe);
            }
        }
    };

    public void loadImage() {
        int start = mListView.getFirstVisiblePosition();
        int end = mListView.getLastVisiblePosition();
        if (end >= getCount()) {
            end = getCount() - 1;
        }
        syncImageLoader.setLoadLimit(start, end);
        syncImageLoader.unlock();
    }

    AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //switch (scrollState) {
            //    //是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
            //    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
            //        syncImageLoader.lock();
            //        break;
            //    //当屏幕停止滚动时
            //    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
            //        loadImage();
            //        break;
            //    //是当用户在以触屏方式滚动屏幕并且手指仍然还在屏幕上时
            //    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
            //        syncImageLoader.lock();
            //        break;
            //    default:
            //        break;
            //}
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            /**
             * we want to load the next chunk of data before the user reach the end of the list.
             * 在列表打到末尾之前，我们要加载下一个数据块
             * firstVisibleItem 表示在当前屏幕显示的第一个listItem在整个listView里面的位置（下标从0开始）
             * visibleItemCount表示在现时屏幕可以见到的ListItem(部分显示的ListItem也算)总数
             * totalItemCount表示ListView的ListItem总数
             * listView.getLastVisiblePosition()表示在现时屏幕最后一个ListItem
             * (最后ListItem要完全显示出来才算)在整个ListView的位置（下标从0开始）
             */

            //Log.d("load", ""+firstVisibleItem+"----"+visibleItemCount+"-----"+totalItemCount);
            if(firstVisibleItem + visibleItemCount < totalItemCount - 3){
            //    Log.d("load", "if------<>");
                return;
            }else{
            //    Log.d("load", "false------>");
                loadImage();
            }
        }
    };
}