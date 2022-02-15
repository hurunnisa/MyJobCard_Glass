package com.ondevice.myjobcard_glass;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;
import com.ondevice.myjobcard_glass.entities.WorkOrderGlassEntity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link Activity} showing a tuggable "Hello World!" card.
 * <p>
 * The main content view is composed of a one-card {@link CardScrollView} that provides tugging
 * feedback to the user when swipe gestures are detected.
 * If your Glassware intends to intercept swipe gestures, you should set the content view directly
 * and use a {@link com.google.android.glass.touchpad.GestureDetector}.
 *
 * @see <a href="https://developers.google.com/glass/develop/gdk/touch">GDK Developer Guide</a>
 */
public class HomeCardActivity extends Activity {

    /**
     * {@link CardScrollView} to use as the main content view.
     */
    private CardScrollView mCardScrollView;
    private List<CardBuilder> mCards;
    private HomeCardScrollAdapter mAdapter;
    WorkOrderGlassEntity workOrderGlassEntity;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        workOrderGlassEntity= HomeData.workOrderGlassEntity;//(WorkOrderGlassEntity) getIntent().getSerializableExtra("wodata");
        createCards();

        mCardScrollView = new CardScrollView(this);
        mAdapter = new HomeCardScrollAdapter();
        mCardScrollView.setAdapter(mAdapter);
        mCardScrollView.activate();
        setupClickListener();
        setContentView(mCardScrollView);
    }
    private void createCards() {
        mCards = new ArrayList<CardBuilder>();

        mCards.add(new CardBuilder(this, CardBuilder.Layout.COLUMNS_FIXED)
                .setText("Work Order "+ workOrderGlassEntity.getWorkOrderNum())
                .setFootnote("Active Work Order")
                .addImage(R.drawable.start)
                .addImage(R.drawable.emergency_very_high1)
                .addImage(R.drawable.wogls1));


        mCards.add(new CardBuilder(this, CardBuilder.Layout.COLUMNS)
                .setText("Notification 4002121")
                .addImage(R.drawable.notifigls));

        mCards.add(new CardBuilder(this, CardBuilder.Layout.COLUMNS)
                .setText("Supervisor")
                .addImage(R.drawable.supgls));
    }
    private class HomeCardScrollAdapter extends CardScrollAdapter {

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

    private void setupClickListener() {
        mCardScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(Sounds.TAP);
                if(position==0) {
                    startActivity(new Intent(HomeCardActivity.this, WorkOrderCard.class));
                    finish();
                }
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
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            startActivity(new Intent(HomeCardActivity.this,BluetoothConnectionActivity.class));
            finish();
            return true;
        }
        return false;
    }
}
