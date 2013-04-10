package com.jumbo.torture;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.jumbo.torture.provider.Torture.TortureMsg;
import com.jumbo.torture.utils.LogUtils;

public class TortureMainActivity extends Activity implements OnClickListener, OnItemClickListener {
    private static final String TAG = TortureMainActivity.class.getName();

    ListView mMsgList;
    ListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.torture_main_layout);

        LogUtils.init(this, LogUtils.LOG_MODE_DEBUG);

        findViewById(R.id.new_msg).setOnClickListener(this);

        mMsgList = (ListView) findViewById(R.id.main_list);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor = managedQuery(TortureMsg.CONTENT_URI, TortureMsgListAdapter.T_MSG_PROJECTION, null, null,null);

        TortureMsgListAdapter adapter = new TortureMsgListAdapter(this, cursor,R.layout.t_msg_item);

        mMsgList.setAdapter(adapter);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.new_msg:
                Intent intent = new Intent(TortureMainActivity.this, ComposeMsgActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_toture, menu);
        return true;
    }
}
