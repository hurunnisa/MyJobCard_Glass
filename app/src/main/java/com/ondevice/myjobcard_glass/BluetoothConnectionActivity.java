package com.ondevice.myjobcard_glass;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ondevice.myjobcard_glass.entities.OperationGlassEntity;
import com.ondevice.myjobcard_glass.entities.WorkOrderGlassEntity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BluetoothConnectionActivity extends Activity
{
    /**
     * {@link CardScrollView} to use as the main content view.
     */
    private CardScrollView mCardScrollView;
    private List<CardBuilder> mCards;
    private BluetoothConnectionActivity.BTCardScrollAdapter mAdapter;

    public static BluetoothService mService = null;

    private boolean mIsBound;
    WorkOrderGlassEntity workOrderGlassEntity;
    @Override
    protected void onCreate(Bundle bundle) {
        try {
            super.onCreate(bundle);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            startService(new Intent(this.getBaseContext(), BluetoothService.class));
            doBindService();
        } catch (Exception e) {
            Toast.makeText(BluetoothConnectionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder)
        {
            try {
                mService = ((BluetoothService.LocalBinder)iBinder).getInstance();
                mService.setHandler(mHandler);
                initialize();
            } catch (Exception e) {
                Toast.makeText(BluetoothConnectionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName)
        {
            mService = null;
        }
    };
    private void doBindService()
    {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        try {
            bindService(new Intent(this,
                    BluetoothService.class), mConnection, Context.BIND_AUTO_CREATE);
            mIsBound = true;
        } catch (Exception e) {
            Toast.makeText(BluetoothConnectionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void doUnbindService()
    {
        try {
            if (mIsBound)
            {
                // Detach our existing connection.
                unbindService(mConnection);
                mIsBound = false;
            }
        } catch (Exception e) {
            Toast.makeText(BluetoothConnectionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                Log.d("DEBUG", "handleMessage");

                if (msg.what == Constants.MESSAGE_WRITE) {
                    try {
                        byte[] writeBuf = (byte[]) msg.obj;//if message is what we want
                        // construct a string from the buffer
                        String writeMessage = new String(writeBuf);
                        //String readMessage = (String) msg.obj; // msg.arg1 = bytes from connect thread
                        Toast.makeText(BluetoothConnectionActivity.this, writeMessage+" status has sent to "+HomeData.connecteddeviceName, Toast.LENGTH_LONG).show();//`enter code here`

                        Log.d("RECORDED", writeMessage.toString());
                    } catch (Exception e) {
                        Toast.makeText(BluetoothConnectionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    // Do stuff here with your data, like adding it to the database
                }else if(msg.what == Constants.MESSAGE_TOAST)
                {
    //                Toast.makeText(BluetoothConnectionActivity.this, msg.getData().getString(Constants.TOAST),
    //                        Toast.LENGTH_SHORT).show();

                    try {
                        mCards = new ArrayList<CardBuilder>();
                        mCards.add(new CardBuilder(BluetoothConnectionActivity.this, CardBuilder.Layout.ALERT)
                                .setIcon(R.drawable.bt_icon)
                                .setText(msg.getData().getString(Constants.TOAST)));
                        CardScrollView mCardScrollView = new CardScrollView(BluetoothConnectionActivity.this);
                        mAdapter = new BTCardScrollAdapter();
                        mCardScrollView.setAdapter(mAdapter);
                        mCardScrollView.activate();


                        setContentView(mCardScrollView);
                        mAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Toast.makeText(BluetoothConnectionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }


                }else if(msg.what == Constants.MESSAGE_READ) {
                    try {
                        byte[] readBuf = (byte[]) msg.obj;
                        // construct a string from the valid bytes in the buffer
                        String readMessage = new String(readBuf, 0, msg.arg1);
                        //String data=readMessage;

                        Gson jsonrecstr=new Gson();
                        workOrderGlassEntity=jsonrecstr.fromJson(readMessage,WorkOrderGlassEntity.class);
                        HomeData.workOrderGlassEntity=workOrderGlassEntity;

                        mCards = new ArrayList<CardBuilder>();
                        mCards.add(new CardBuilder(BluetoothConnectionActivity.this, CardBuilder.Layout.ALERT)
                                .setIcon(R.drawable.bt_icon)
                                .setText("Connected to " +  HomeData.connecteddeviceName)
                                .setFootnote("tap on this"));
                        CardScrollView mCardScrollView = new CardScrollView(BluetoothConnectionActivity.this);
                        mAdapter = new BTCardScrollAdapter();
                        mCardScrollView.setAdapter(mAdapter);
                        mCardScrollView.activate();

                        mCardScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                                am.playSoundEffect(Sounds.TAP);
                                startActivity(new Intent(BluetoothConnectionActivity.this, HomeCardActivity.class));
                                finish();

                            }
                        });
                        setContentView(mCardScrollView);
                        mAdapter.notifyDataSetChanged();
                    } catch (JsonSyntaxException e) {
                        Toast.makeText(BluetoothConnectionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
                else if(msg.what == Constants.MESSAGE_DEVICE_NAME)
                {
    //                Toast.makeText(BluetoothConnectionActivity.this, "Connected to "
    //                        +  msg.getData().getString(Constants.DEVICE_NAME), Toast.LENGTH_LONG).show();
                    HomeData.connecteddeviceName=msg.getData().getString(Constants.DEVICE_NAME);
                    Toast.makeText(BluetoothConnectionActivity.this,"success", Toast.LENGTH_SHORT).show();



                }
            } catch (Exception e) {
                Toast.makeText(BluetoothConnectionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    };

//    @Override
//    protected void onResume() {
//        super.onResume();
//        startService(new Intent(this.getBaseContext(), BluetoothService.class));
//        doBindService();
//    }

    @Override
    protected void onDestroy()
    {
        try {
            super.onDestroy();
            doUnbindService();
        } catch (Exception e) {
            Toast.makeText(BluetoothConnectionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void initialize() {
        try {
            createCards();

            mCardScrollView = new CardScrollView(this);
            mAdapter = new BTCardScrollAdapter();
            mCardScrollView.setAdapter(mAdapter);
            mCardScrollView.activate();
            setupClickListener();
            setContentView(mCardScrollView);
        } catch (Exception e) {
            Toast.makeText(BluetoothConnectionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
//        Toast.makeText(BluetoothConnectionActivity.this,"Test Toast message",
//                Toast.LENGTH_SHORT).show();
    }

    private void createCards() {
        try {
            mCards = new ArrayList<CardBuilder>();

            mCards.add(new CardBuilder(this, CardBuilder.Layout.ALERT)
                    .setIcon(R.drawable.bt_icon)
                    .setText("not connected"));
        } catch (Exception e) {
            Toast.makeText(BluetoothConnectionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
    private class BTCardScrollAdapter extends CardScrollAdapter {

        @Override
        public int getPosition(Object item) {
            return mCards.indexOf(item);
        }

        @Override
        public int getCount() {
            return mCards.size();
        }

        @Override
        public Object getItem(int position) {
            return mCards.get(position);
        }

        @Override
        public int getViewTypeCount() {
            return CardBuilder.getViewTypeCount();
        }

        @Override
        public int getItemViewType(int position){
            return mCards.get(position).getItemViewType();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return mCards.get(position).getView(convertView, parent);
        }
    }

    public void setupClickListener() {
        mCardScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(Sounds.TAP);

//                    startActivity(new Intent(BluetoothConnectionActivity.this, HomeCardActivity.class));
//                    finish();

            }
        });
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mCardScrollView.activate();
//    }
//
//    @Override
//    protected void onPause() {
//        mCardScrollView.deactivate();
//        super.onPause();
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if(keyCode==KeyEvent.KEYCODE_BACK)
            {
                HomeData.conn_State=0;
                mService.stop();
                finish();
                return true;
            }
        } catch (Exception e) {
            Toast.makeText(BluetoothConnectionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return false;
    }


}
