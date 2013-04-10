package com.jumbo.torture;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jumbo.torture.provider.Torture.TortureMsg;
import com.jumbo.torture.utils.DateFormatUtils;
import com.jumbo.torture.utils.LogUtils;

public class TortureMsgListAdapter extends CursorAdapter implements OnCreateContextMenuListener, OnMenuItemClickListener {

    private static String TAG = TortureMsgListAdapter.class.toString();

    public static final String[] T_MSG_PROJECTION = new String[]{
        TortureMsg._ID,
        TortureMsg.TITLE,
        TortureMsg.CREATED_TIME
    };

    public static final int T_MSG_INDEX_ID 				= 0;
    public static final int T_MSG_INDEX_TITLE 			= 1;
    public static final int T_MSG_INDEX_CREATED_TIME 	= 2;

    private static final int CONTEXT_MENU_DELETE		= 1;

    private int mResLayout;
    private LayoutInflater mInflater;

    public TortureMsgListAdapter(Context context, Cursor c,int resLayout) {
        super(context, c);
        mResLayout = resLayout;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public void bindView(View convertView, Context context, Cursor cursor) {
        TortureMsgItem viewItem = (TortureMsgItem) convertView.getTag();

        viewItem.mTitleView.setText(cursor.getString(T_MSG_INDEX_TITLE));
        viewItem.mDateView.setText(DateFormatUtils.formatTimeStampString(context,cursor.getLong(T_MSG_INDEX_CREATED_TIME)));

        convertView.setOnCreateContextMenuListener(this);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {

        menu.setHeaderTitle(R.string.hello_world);
        menu.add(0,CONTEXT_MENU_DELETE,1,"delete").setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Cursor cursor = getCursor();
        if (cursor.isClosed() || cursor.isBeforeFirst() || cursor.isAfterLast()) {
            LogUtils.e(TAG, "Bad cursor.");
            return false;
        }

        int id = cursor.getInt(T_MSG_INDEX_ID);

        Uri msgUri = ContentUris.withAppendedId(TortureMsg.CONTENT_URI,id);
        mContext.getContentResolver().delete(msgUri, null, null);

        return false;
    }

}