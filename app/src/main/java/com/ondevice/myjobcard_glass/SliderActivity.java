package com.ondevice.myjobcard_glass;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;
import com.google.android.glass.widget.Slider;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
public class SliderActivity extends Activity {

    private static final int MAX_SLIDER_VALUE = 5;
    private static final long ANIMATION_DURATION_MILLIS = 5000;

    private CardScrollView mCardScroller;
    private Slider mSlider;
    private List<CardBuilder> mCards;
    private Slider.GracePeriod mGracePeriod;
    private Slider.Determinate mDeterminate;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        // Create the cards for the view
        createCards();
        mCardScroller = new CardScrollView(this);

        mCardScroller.setAdapter(new CardAdapter());
        mCardScroller.activate();
        // Set the view for the Slider
        mSlider = Slider.from(mCardScroller);

        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //processSliderRequest(position+1);
              //  mGracePeriod = mSlider.startGracePeriod(mGracePeriodListener);
                mDeterminate = mSlider.startDeterminate(MAX_SLIDER_VALUE, 0);
                ObjectAnimator animator = ObjectAnimator.ofFloat(mDeterminate,
                        "position", 0, MAX_SLIDER_VALUE);

                // Hide the slider when the animation stops.
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mDeterminate.hide();
                    }
                });

                // Start an animation showing the different positions of the slider.
                animator.setDuration(ANIMATION_DURATION_MILLIS).start();
            }

        });

        setContentView(mCardScroller);
    }
    private void createCards() {
        mCards = new ArrayList<CardBuilder>();

        mCards.add(new CardBuilder(this, CardBuilder.Layout.COLUMNS_FIXED)
                .setText("Work Order 2001231 ")
                .setFootnote("Active Work Order")
                .addImage(R.drawable.wo));

        mCards.add(new CardBuilder(this, CardBuilder.Layout.COLUMNS)
                .setText("Notification 4002121")
                .setFootnote("Active Notification")
                .addImage(R.drawable.notifigls));

        mCards.add(new CardBuilder(this, CardBuilder.Layout.COLUMNS)
                .setText("Supervisor")
                .setFootnote("Active Supervisor")
                .addImage(R.drawable.supgls));
    }
    private class CardAdapter extends CardScrollAdapter {


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
        public int getItemViewType(int position) {
            return mCards.get(position).getItemViewType();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return mCards.get(position).getView(convertView, parent);
        }
    }
    private void processSliderRequest(int position) {
        Slider.Scroller scroller = mSlider.startScroller(MAX_SLIDER_VALUE, 0);

        // Animate the slider to the next position. The slider
        // automatically hides after the duration has elapsed
        ObjectAnimator.ofFloat(scroller, "position", 0, position)
                .setDuration(ANIMATION_DURATION_MILLIS)
                .start();
    }



    private final Slider.GracePeriod.Listener mGracePeriodListener =
            new Slider.GracePeriod.Listener() {

                @Override
                public void onGracePeriodEnd() {
                    // Play a SUCCESS sound to indicate the end of the grace period.
                    AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    am.playSoundEffect(Sounds.SUCCESS);
                    mGracePeriod = null;
                }

                @Override
                public void onGracePeriodCancel() {
                    // Play a DIMISS sound to indicate the cancellation of the grace period.
                    AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    am.playSoundEffect(Sounds.DISMISSED);
                    mGracePeriod = null;
                }
            };

    @Override
    public void onBackPressed() {
        // If the Grace Period is running,
        // cancel it instead of finishing the Activity.
        if (mGracePeriod != null) {
            mGracePeriod.cancel();
        } else {
            super.onBackPressed();
        }
    }
}