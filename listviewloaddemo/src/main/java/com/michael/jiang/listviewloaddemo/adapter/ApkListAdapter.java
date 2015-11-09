package com.michael.jiang.listviewloaddemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.michael.jiang.listviewloaddemo.R;
import com.michael.jiang.listviewloaddemo.entity.ApkEntity;

import org.w3c.dom.Text;

import java.util.List;
import java.util.zip.Inflater;


/**
 * Created by Jiang on 2015/3/25.
 */
public class ApkListAdapter extends BaseAdapter {
    private List<ApkEntity> apkEntityList;
    private LayoutInflater inflater;

    /**
     * Adapter的构造函数
     * @param context 上下文
     * @param apkEntityList ApkEntity类：提供APk相关信息
     */
    public ApkListAdapter(Context context ,List<ApkEntity> apkEntityList) {
        this.inflater= LayoutInflater.from(context);
        this.apkEntityList=apkEntityList;
    }

    /**
     * 在数据源改变时调用这个方法通知数据源已改变
     * @param apkEntityList 新数据源
     */
    public void onDataChanged(List<ApkEntity> apkEntityList)
    {
        this.apkEntityList=apkEntityList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return apkEntityList.size();
    }

    @Override
    public Object getItem(int i) {
        return apkEntityList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ApkEntity apkEntity=apkEntityList.get(i);
        ViewHolder viewHolder;
        if(view==null) {
            view = inflater.inflate(R.layout.item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_apkName = (TextView) view.findViewById(R.id.item_apkName);
            viewHolder.tv_apkInfo = (TextView) view.findViewById(R.id.item_apkInfo);
            viewHolder.tv_apkDescription = (TextView) view.findViewById(R.id.item_apkDescription);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.tv_apkName.setText(apkEntity.getName());
        viewHolder.tv_apkInfo.setText(apkEntity.getInfo());
        viewHolder.tv_apkDescription.setText(apkEntity.getDescription());
        return view;
    }

    class ViewHolder{
        TextView tv_apkName;
        TextView tv_apkInfo;
        TextView tv_apkDescription;
    }
}
