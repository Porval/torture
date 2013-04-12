package com.jumbo.torture;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jumbo.torture.provider.Torture.TortureMsg;
import com.jumbo.torture.utils.LogUtils;

public class TortureMainActivity extends Activity implements OnClickListener, OnItemClickListener{
    private static final String TAG = TortureMainActivity.class.getName();

    private static final int CONTEXT_MENU_DELETE		= 1;
    private static final int CONTEXT_MENU_EDIT			= 2;

    ListView mMsgList;

    Cursor mCursor;
    TortureMsgListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.torture_main_layout);

        LogUtils.init(this, LogUtils.LOG_MODE_DEBUG);

        initView();
    }

    private void initView(){
        findViewById(R.id.new_msg).setOnClickListener(this);
        mCursor = managedQuery(TortureMsg.CONTENT_URI,
                TortureMsgListAdapter.T_MSG_PROJECTION, null, null, null);
        mAdapter = new TortureMsgListAdapter(this,mCursor,R.layout.t_msg_item);
        mMsgList = (ListView) findViewById(R.id.main_list);
        mMsgList.setAdapter(mAdapter);
        mMsgList.setOnItemClickListener(this);
        mMsgList.setOnCreateContextMenuListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mCursor = managedQuery(TortureMsg.CONTENT_URI,
//                TortureMsgListAdapter.T_MSG_PROJECTION, null, null, null);
//        mAdapter.changeCursor(mCursor);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Cursor cursor = (Cursor) mMsgList.getAdapter().getItem(position);
        Intent intent = new Intent(TortureMainActivity.this, ComposeMsgActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = ContentUris.withAppendedId(TortureMsg.CONTENT_URI,
                cursor.getLong(TortureMsgListAdapter.T_MSG_INDEX_ID));
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.new_msg:
                Intent intent = new Intent(TortureMainActivity.this, ComposeMsgActivity.class);
                intent.setAction(Intent.ACTION_INSERT);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {

        menu.setHeaderTitle(R.string.hello_world);
        menu.add(0,CONTEXT_MENU_EDIT,1,"Edit");
        menu.add(0,CONTEXT_MENU_DELETE,2,"Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info;
        try {
            info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        } catch (ClassCastException e) {
            Log.e(TAG, "bad menuInfo", e);
            return false;
        }

        Cursor cursor = (Cursor) mMsgList.getAdapter().getItem(info.position);

        Uri uri = ContentUris.withAppendedId(TortureMsg.CONTENT_URI,
                cursor.getLong(TortureMsgListAdapter.T_MSG_INDEX_ID));

        switch(item.getItemId()){
            case CONTEXT_MENU_DELETE:
                getContentResolver().delete(uri, null, null);
                return true;
            case CONTEXT_MENU_EDIT:
                Intent intent = new Intent(TortureMainActivity.this, ComposeMsgActivity.class);
                intent.setAction(Intent.ACTION_EDIT);
                startActivity(intent);
                return true;
        }

        return super.onContextItemSelected(item);
    }
}
