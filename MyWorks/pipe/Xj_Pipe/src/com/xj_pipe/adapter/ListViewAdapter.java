package com.xj_pipe.adapter;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xj_pipe.bean.LinkmanBean;
import com.xj_pipe.view.R;
  
public class ListViewAdapter extends BaseAdapter {  
  
    private Context context;  
    private List<LinkmanBean> linkmanBeans; 
    private static int select_count = 0;
    
    // 用来控制CheckBox的选中状况  
    public static HashMap<Integer, Boolean> isSelected;  
  
    class ViewHolder {  
        TextView linkmanName;  
        CheckBox linkmanFlag;
        TextView linkmanPosition;
    }  
  
    public ListViewAdapter(Context context, List<LinkmanBean> linkmanBeans) {  
        // TODO Auto-generated constructor stub  
        this.linkmanBeans = linkmanBeans;  
        this.context = context;  
        isSelected = new HashMap<Integer, Boolean>();  
        // 初始化数据  
        initDate();  
    }  
  
    // 初始化isSelected的数据  
    private void initDate() {  
        for (int i = 0; i < linkmanBeans.size(); i++) {  
            getIsSelected().put(i, false);  
        }  
    }  
  
    @Override  
    public int getCount() {  
        // TODO Auto-generated method stub  
        return linkmanBeans.size();  
    }  
  
    @Override  
    public Object getItem(int position) {  
        // TODO Auto-generated method stub  
        return linkmanBeans.get(position);  
    }  
  
    @Override  
    public long getItemId(int position) {  
        // TODO Auto-generated method stub  
        return position;  
    }  
  
    @Override  
    public View getView(final int position, View convertView, ViewGroup parent) {  
        // TODO Auto-generated method stub  
    	Log.i("qf", ""+position);
        // 页面  
        final ViewHolder holder;  
        final LinkmanBean bean = linkmanBeans.get(position);  
        LayoutInflater inflater = LayoutInflater.from(context);  
        if (convertView == null) {  
            convertView = inflater.inflate(  
                    R.layout.linkman_listview_item_detail, null);  
            holder = new ViewHolder();  
            holder.linkmanFlag = (CheckBox) convertView.findViewById(R.id.linkman_flag);  
//            holder.linkmanFlag.setFocusable(false); 
//            holder.linkmanFlag.setClickable(false);
            holder.linkmanName = (TextView) convertView  
                    .findViewById(R.id.linkman_name);  
            holder.linkmanPosition = (TextView) convertView.findViewById(R.id.linkman_position);
            convertView.setTag(holder);  
        } else {  
            // 取出holder  
            holder = (ViewHolder) convertView.getTag();  
        }  
  
        holder.linkmanName.setText(bean.getLinkman_name());  
        holder.linkmanPosition.setText(bean.getLinkman_position());
        
        // 监听checkBox并根据原来的状态来设置新的状态  
        convertView.setOnClickListener(new View.OnClickListener() {  
  
            @Override
			public void onClick(View v) {  
            	holder.linkmanFlag.toggle();
                if (isSelected.get(position)) {  
                    isSelected.put(position, false);
                    bean.setLinkman_flag(false);
                    setIsSelected(isSelected);  
                    select_count-=1;
                } else {  
                    isSelected.put(position, true);  
                    bean.setLinkman_flag(true);
                    setIsSelected(isSelected);
                    select_count+=1;
                }  
  
            }  
        });  
  
        // 根据isSelected来设置checkbox的选中状况  
        holder.linkmanFlag.setChecked(getIsSelected().get(position));  
        return convertView;  
    }  
  
    public static HashMap<Integer, Boolean> getIsSelected() {  
        return isSelected;  
    }  
  
    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {  
        ListViewAdapter.isSelected = isSelected;  
    }  
}  