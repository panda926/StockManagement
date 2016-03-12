package com.codegineer.datagrid;

import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.codegineer.datagrid.Item;
import com.codegineer.datatable.DataTable;


public class DataGridAdapter extends BaseAdapter {

    private Context mContext;
    private DataGrid.MemberCollection mc;
    
    private static HashMap<Integer, Boolean> isSelected;
    
    public DataGridAdapter(Context context, DataGrid.MemberCollection mc) {
        mContext = context;
        this.mc = mc;
        isSelected = new HashMap<Integer, Boolean>();
        initDate();
    }
    
    public void initDate() {
		for (int i = 0; i < mc.DATA_SOURCE.getRowSize(); i++) {
			getIsSelected().put(i, false);
		}
	}

    public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}
    
    public static int GetCheckedCount()
    {
    	int nTotalCheckCount = 0;
    	
    	HashMap hash = getIsSelected();
    	for(int i = 0; i < hash.size(); i++)
    	{
    		if(getIsSelected().get(i))
    		{
    			nTotalCheckCount++;
    		}
    	}
    	
    	return nTotalCheckCount;
    }

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		DataGridAdapter.isSelected = isSelected;
	}

	public int getCount() {
        return mc.DATA_SOURCE.getRowSize();
    }

    public Object getItem(int position) {
    	return mc.DATA_SOURCE.getRow(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * Make a SpeechView to hold each row.
     * 
     * @see android.widget.ListAdapter#getView(int, android.view.View,
     *      android.view.ViewGroup)
     */
    public View getView(int position, View convertView, ViewGroup parent) {
    	Item ri; 
    	
    	DataTable.DataRow data = mc.DATA_SOURCE.getRow(position);
    	
        if(convertView == null)
        {
        	ri = new Item(mContext, mc, data);	
        }
        else	
        {
        	ri = (Item)convertView;
        	ri.populate(data);
        }
        
        final int nPosition = position;
        
        for(int i = 0; i < ri.getChildCount(); i++)
        {
        	View v = (View)ri.getChildAt(i);
        	
        	if(v instanceof CheckBox)
        	{
        		CheckBox chk = (CheckBox)v;
        		chk.setOnClickListener(new OnClickListener()
        		{
        			
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						getIsSelected().put(nPosition, ((CheckBox)v).isChecked());	
					}
        			
        		});
        		if(getIsSelected().size() > 0)
        		{
        			chk.setChecked(getIsSelected().get(position));
        		}
        	}
        }
        
        return ri;
    }
}