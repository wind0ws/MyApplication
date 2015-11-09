package com.michael.listviewasyncload;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jiang on 2015/7/19.
 */
public class NewsAdapter extends BaseAdapter implements AbsListView.OnScrollListener {

    private List<NewsBean> newsBeanList;
    private Context context;
    private LayoutInflater inflater;
    private int mStartItem;
    private int mEndItem;
    private boolean isFirstLoad;
    private ListView mListView;
    private LruCache<String, Bitmap> mImageCaches;
   // private String[] mImageUrls;
    private List<LoadImageAsyncTask> mLoadImageAsyncTaskList;

    public NewsAdapter(Context context, List<NewsBean> newsBeanList, ListView listView) {
        this.context = context;
        this.newsBeanList = newsBeanList;
        this.mListView = listView;
        inflater = LayoutInflater.from(context);
        isFirstLoad=true;
        mStartItem=0;
        mEndItem = 0;
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        mImageCaches =new LruCache<String,Bitmap>(maxMemory/4){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
        mLoadImageAsyncTaskList = new ArrayList<>();
//        int size = newsBeanList.size();
//        mImageUrls = new String[size];
//        for (int i = 0; i < size; i++) {
//            mImageUrls[i] = newsBeanList.get(i).iconUrl;
//        }
        listView.setOnScrollListener(this);//设置滚动监听
    }

    @Override
    public int getCount() {
        return newsBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null) {
            convertView = inflater.inflate(R.layout.news_item, parent,false);
            viewHolder = new ViewHolder();
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NewsBean newsBean = newsBeanList.get(position);
        //设置图像 交由OnScrollStateChange()事件来处理
       // showImageByAsyncTask(position);
        viewHolder.ivIcon.setImageResource(R.mipmap.ic_launcher);
        viewHolder.ivIcon.setTag(newsBean.iconUrl);
        viewHolder.tvTitle.setText(newsBean.title);
        viewHolder.tvContent.setText(newsBean.content);
        return convertView;
    }

    private void showImageByAsyncTask(int position) {
        String imageUrl = newsBeanList.get(position).iconUrl;
        Bitmap image = mImageCaches.get(imageUrl);
        if (image == null) {
            LoadImageAsyncTask loadImageAsyncTask = new LoadImageAsyncTask();
            loadImageAsyncTask.execute(imageUrl);
            mLoadImageAsyncTaskList.add(loadImageAsyncTask);
        }else {
            ImageView imageView = (ImageView) mListView.findViewWithTag(imageUrl);
            imageView.setImageBitmap(image);
        }
    }

    private Bitmap getBitmapFromUrl(String imageUrl) throws IOException {
//        URLConnection urlConnection = new URL(imageUrl).openConnection();
//        InputStream inputStream = urlConnection.getInputStream();
        InputStream inputStream = new URL(imageUrl).openStream();
        return BitmapFactory.decodeStream(inputStream);
    }



    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState==SCROLL_STATE_IDLE) {
            //加载数据
            loadImage(mStartItem, mEndItem);
        }else {
            //停止加载(取消所有异步家在任务)
            cancelLoadImageTasks();
        }
    }

    private void cancelLoadImageTasks() {
        for(LoadImageAsyncTask task:mLoadImageAsyncTaskList) {
            task.cancel(false);
        }
        mLoadImageAsyncTaskList.clear();
    }

    private void loadImage(int mStartItem, int mEndItem) {
        for (int i = mStartItem; i < mEndItem; i++) {
            showImageByAsyncTask(i);
//            String imageUrl = newsBeanList.get(i).iconUrl;
//            LoadImageAsyncTask task = new LoadImageAsyncTask();
//            task.execute(imageUrl);
//            mLoadImageAsyncTaskList.add(task);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStartItem=firstVisibleItem;
        mEndItem = firstVisibleItem + visibleItemCount;
        if(isFirstLoad&&visibleItemCount>0) {
            // 加载第一次数据
            loadImage(mStartItem,mEndItem);
            isFirstLoad = false;
        }
    }

    private class LoadImageAsyncTask extends AsyncTask<String,Void,Bitmap>{

        private String imageUrl;
//
//        public LoadImageAsyncTask(String imageUrl) {
//            this.imageUrl = imageUrl;
//        }

        @Override
        protected Bitmap doInBackground(String... params) {
            imageUrl=params[0];
            Bitmap bitmap=null;
            try {
                 bitmap = getBitmapFromUrl(imageUrl);
                if(bitmap!=null){
                    mImageCaches.put(imageUrl, bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ImageView ivIcon = (ImageView) mListView.findViewWithTag(imageUrl);
            if (ivIcon!=null&&bitmap!=null) {
                ivIcon.setImageBitmap(bitmap);//设置图片
            }
            mLoadImageAsyncTaskList.remove(this);
            super.onPostExecute(bitmap);
        }
    }

    private class ViewHolder{
        public ImageView ivIcon;
        public TextView tvTitle;
        public TextView tvContent;
    }
}
