package com.jumbo.torture;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jumbo.torture.provider.Torture.TortureMsg;
import com.jumbo.torture.utils.DateFormatUtils;

public class TortureMsgListAdapter extends CursorAdapter {

    private static String TAG = TortureMsgListAdapter.class.toString();

    public static final String[] T_MSG_PROJECTION = new String[]{
        TortureMsg._ID,
        TortureMsg.TITLE,
        TortureMsg.CREATED_TIME
    };

    public static final int T_MSG_INDEX_ID 				= 0;
    public static final int T_MSG_INDEX_TITLE 			= 1;
    public static final int T_MSG_INDEX_CREATED_TIME 	= 2;

    private int mResLayout;
    private LayoutInflater mInflater;

    public TortureMsgListAdapter(Context context, Cursor c,int resLayout) {
        super(context, c);
        mResLayout = resLayout;
        //mInflater = LayoutInflater.from(context);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void bindView(View convertView, Context context, Cursor cursor) {
        TortureMsgItem viewItem = (TortureMsgItem) convertView.getTag();

        viewItem.mTitleView.setText(cursor.getString(T_MSG_INDEX_TITLE));
        viewItem.mDateView.setText(DateFormatUtils.formatTimeStampString(context,cursor.getLong(T_MSG_INDEX_CREATED_TIME)));

        /*
         * error opertion
         */
        //convertView.setOnCreateContextMenuListener(this);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.t_msg_item, viewGroup,false);
        initView(view);
        return view;
    }

    private void initView(View view){
        TortureMsgItem viewHolder = new TortureMsgItem();
        viewHolder.mTitleView = (TextView) view.findViewById(R.id.msg_item_title);
        viewHolder.mDateView = (TextView) view.findViewById(R.id.msg_item_time);
        view.setTag(viewHolder);
    }

    private class TortureMsgItem{
        public TextView mTitleView;
        public TextView mDateView;
    }

}