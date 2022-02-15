package com.ondevice.myjobcard_glass;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.view.WindowUtils;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;
import com.ondevice.myjobcard_glass.entities.WorkOrderGlassEntity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

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
public class OperationsCard extends Activity {

    /**
     * {@link CardScrollView} to use as the main content view.
     */
    private CardScrollView mCardScrollView;
    private List<CardBuilder> mCards;
    private OpearationsScrollAdapter mAdapter;
    private static final int SPEECH_REQUEST = 0;
    String spokenText="";
    private View view;
    MenuItem item;
    Menu menul;
    private final DialogInterface.OnClickListener mOnClickListener;
    Boolean isnotes=false,isComplete=false,isViewNotes=false;
    //String oprStatus="Not Completed";
    public OperationsCard() {
        mOnClickListener =null;
    }
    WorkOrderGlassEntity workOrderGlassEntity;
    int cardposition;
    String className="Opeartion,";
    @Override
    protected void onCreate(Bundle bundle) {
        try {
            super.onCreate(bundle);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);

            workOrderGlassEntity= HomeData.workOrderGlassEntity;
            getOperationCardDetails();
        } catch (Exception e) {
            Toast.makeText(OperationsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void getOperationCardDetails() {
        try {
            createCards();

            mCardScrollView = new CardScrollView(this);
            mAdapter = new OpearationsScrollAdapter();
            mCardScrollView.setAdapter(mAdapter);
            mCardScrollView.activate();
            setupClickListener();
            getWindow().invalidatePanelMenu(WindowUtils.FEATURE_VOICE_COMMANDS);
            setContentView(mCardScrollView);
        } catch (Exception e) {
            Toast.makeText(OperationsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void createCards() {
        try {
            mCards = new ArrayList<CardBuilder>();

            for(int i=0;i<workOrderGlassEntity.getOprentities().size();i++)
            {
                mCards.add(new CardBuilder(this, CardBuilder.Layout.COLUMNS)
                        .setText("Operation : "+ workOrderGlassEntity.getOprentities().get(i).getOperationNum()+" \nDescription : " +workOrderGlassEntity.getOprentities().get(i).getShortText()+" \n Status : " +workOrderGlassEntity.getOprentities().get(i).getMobileStatus())
                        .setFootnote("Tap on this or Say Ok Glass")
                        .addImage(R.drawable.operationsgls1));
            }
        } catch (Exception e) {
            Toast.makeText(OperationsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


//        mCards.add(new CardBuilder(this, CardBuilder.Layout.COLUMNS)
//                .setText("Operation 20 \n\n Customer Service Work Order \n Status : ")
//                .setFootnote("Tap on this")
//                .addImage(R.drawable.operationsgls1));
//                //.setTimestamp("just now"));

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
               openOptionsMenu();
                return true;
            }
            else if(keyCode==KeyEvent.KEYCODE_BACK)
            {
                if(!isnotes&&!isViewNotes&&!isComplete) {
                    startActivity(new Intent(OperationsCard.this, WorkOrderCard.class));
                    finish();
                    return true;
                }
                else
                {
                    isnotes=false;
                    startActivity(new Intent(OperationsCard.this,OperationsCard.class));
                    finish();
                }
            }
        } catch (Exception e) {
            Toast.makeText(OperationsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        try {
            if (!isnotes&&!isComplete)
            {
                if(workOrderGlassEntity.getOprentities().get(cardposition).getMobileStatus().equalsIgnoreCase("Confirmed"))
                {
                    item = menu.findItem(R.id.action_addnotesopr);
                    item.setVisible(true);

                    item = menu.findItem(R.id.action_cmpltopr);
                    item.setVisible(false);
                }
                else {
                    item = menu.findItem(R.id.action_addnotesopr);
                    item.setVisible(true);

                    item = menu.findItem(R.id.action_cmpltopr);
                    item.setVisible(true);
                }


                item = menu.findItem(R.id.action_viewnoteopr);
                item.setVisible(true);

                item = menu.findItem(R.id.action_viewnoteoprsave);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnoteoprcancel);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnoteoprcomplete);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnoteoprcancelcmplt);
                item.setVisible(false);

            }
            else if(isnotes)
            {
                item = menu.findItem(R.id.action_addnotesopr);
                item.setVisible(false);

                item = menu.findItem(R.id.action_cmpltopr);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnoteopr);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnoteoprcancel);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnoteoprcomplete);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnoteoprcancelcmplt);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnoteoprsave);
                item.setVisible(true);

                item = menu.findItem(R.id.action_viewnoteoprcancel);
                item.setVisible(true);
            }
            else if(isComplete)
            {
                item = menu.findItem(R.id.action_addnotesopr);
                item.setVisible(false);

                item = menu.findItem(R.id.action_cmpltopr);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnoteopr);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnoteoprcancel);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnoteoprcomplete);
                item.setVisible(true);

                item = menu.findItem(R.id.action_viewnoteoprcancelcmplt);
                item.setVisible(true);

                item = menu.findItem(R.id.action_viewnoteoprsave);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnoteoprcancel);
                item.setVisible(false);
            }
            if(workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Completed")||workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("SUSPENDED")||workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Transfer"))
            {
                item = menu.findItem(R.id.action_addnotesopr);
                item.setVisible(false);
                item = menu.findItem(R.id.action_cmpltopr);
                item.setVisible(false);
            }
        } catch (Exception e) {
            Toast.makeText(OperationsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return super.onPrepareOptionsMenu(menu);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//            getMenuInflater().inflate(R.menu.operations_menu, menu);
//            menul=menu;
//        if (!isnotes)
//        {
//            item = menu.findItem(R.id.action_addnotesopr);
//            item.setVisible(true);
//
//            item = menu.findItem(R.id.action_cmpltopr);
//            item.setVisible(true);
//
//            item = menu.findItem(R.id.action_viewnoteopr);
//            item.setVisible(true);
//
//            item = menu.findItem(R.id.action_viewnoteoprsave);
//            item.setVisible(false);
//
//            item = menu.findItem(R.id.action_viewnoteoprcancel);
//            item.setVisible(false);
//
//            item = menu.findItem(R.id.action_viewnoteoprcomplete);
//            item.setVisible(false);
//
//            item = menu.findItem(R.id.action_viewnoteoprcancelcmplt);
//            item.setVisible(false);
//
//        }
//
//       super.onCreateOptionsMenu(menu);
//        return true;
//    }

    /**
     * Taking appropriate action on selecting menu item
     * */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // String actionStatus = (String) item.getTitle();
//        switch (item.getItemId()) {
//            case R.id.action_addnotesopr:
//                displaySpeechRecognizer();
//                return true;
//            case R.id.action_cmpltopr:
//                isComplete=true;
//                setContentView(new CardBuilder(OperationsCard.this, CardBuilder.Layout.TEXT_FIXED)
//                        .setText("Are you sure you want to complete?")
//                        .setFootnote("Tap on this")
//                        .getView());
//
//                return true;
//            case R.id.action_viewnoteopr:
//                isnotes=true;
//                ArrayList<String> notes=HomeData.notesarray;
//                String notesopr = "";
//                for(int i=0;i<notes.size();i++)
//                {
//                    String temp=notes.get(i);
//                    notesopr=notesopr+temp+"\n";
//                }
//                setContentView(new CardBuilder(OperationsCard.this, CardBuilder.Layout.TEXT_FIXED)
//                        .setText(notesopr)
//                        .setFootnote("Swipe down to cancle")
//                        .getView());
//
//                return true;
//            case R.id.action_viewnoteoprsave:
//                HomeData.notesarray.add(spokenText);
//                reInitializeAct();
//                return true;
//            case R.id.action_viewnoteoprcancel:
//                reInitializeAct();
//                return true;
//
//            case R.id.action_viewnoteoprcomplete:
//                HomeData.notesarray.add(spokenText);
//                //HomeData.opr_Status="Completed";
//                workOrderGlassEntity.getOprentities().get(cardposition).setMobileStatus("Confirmed");
//                getOperationCardDetails();
//                String oprStatus=workOrderGlassEntity.getOprentities().get(cardposition).getMobileStatus();
//                BluetoothConnectionActivity.mService.write(oprStatus.getBytes());
//                reInitializeAct();
//
//                return true;
//            case R.id.action_viewnoteoprcancelcmplt:
//                reInitializeAct();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        startActivityForResult(intent, SPEECH_REQUEST);
    }
    public void reInitializeAct()
    {
        startActivity(new Intent(OperationsCard.this,OperationsCard.class));
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        try {
            if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) {
                List<String> results = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                spokenText = results.get(0);
                // Do something with spokenText.
                isnotes=true;
                HomeData.addNotes_opr=spokenText;
                getWindow().invalidatePanelMenu(WindowUtils.FEATURE_VOICE_COMMANDS);
                //HomeData.notesarray.add(spokenText);
                setContentView(new CardBuilder(OperationsCard.this, CardBuilder.Layout.TEXT_FIXED)
                        .setText(spokenText)
                        .setFootnote("Tap on this or Say Ok Glass")
                        .getView());

            }
        } catch (Exception e) {
            Toast.makeText(OperationsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private View buildView() {
        CardBuilder card = new CardBuilder(this, CardBuilder.Layout.TEXT);

        card.setText(spokenText);
        return card.getView();
    }

    //    @Override
//    public boolean onCreatePanelMenu(int featureId, Menu menu) {
//        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS||featureId == Window.FEATURE_OPTIONS_PANEL){
//            return true;
//        }
//        return super.onCreatePanelMenu(featureId, menu);
//    }
//
//
//    @Override
//    public boolean onMenuItemSelected(int featureId, MenuItem item) {
//        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS||featureId == Window.FEATURE_OPTIONS_PANEL) {
//            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//            startActivityForResult(intent, item.getItemId());
//            return true;
//        }
//        return super.onMenuItemSelected(featureId, item);
//    }
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == R.id.action_addnotesopr && resultCode == RESULT_OK) {
//            //1
//            //Part A
//            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//            String spokenText = results.get(0);
//
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
    private class OpearationsScrollAdapter extends CardScrollAdapter {

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

    private void setupClickListener() {
        mCardScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(Sounds.TAP);
                cardposition=position;
                openOptionsMenu();

//                if (position == 0) {
//                    startActivity(new Intent(OperationsCard.this, WorkOrderCard.class));
//                }
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
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        try {
            if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS|| featureId == Window.FEATURE_OPTIONS_PANEL) {
                getMenuInflater().inflate(R.menu.operations_menu, menu);
                menul=null;
                menul=menu;
                if (!isnotes&&!isComplete)
                {
                    if(workOrderGlassEntity.getOprentities().get(cardposition).getMobileStatus().equalsIgnoreCase("Confirmed"))
                    {
                        item = menu.findItem(R.id.action_addnotesopr);
                        item.setVisible(true);

                        item = menu.findItem(R.id.action_cmpltopr);
                        item.setVisible(false);
                    }
                    else {
                        item = menu.findItem(R.id.action_addnotesopr);
                        item.setVisible(true);

                        item = menu.findItem(R.id.action_cmpltopr);
                        item.setVisible(true);
                    }


                    item = menu.findItem(R.id.action_viewnoteopr);
                    item.setVisible(true);

                    item = menu.findItem(R.id.action_viewnoteoprsave);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_viewnoteoprcancel);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_viewnoteoprcomplete);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_viewnoteoprcancelcmplt);
                    item.setVisible(false);

                }
                else if(isnotes)
                {
                    item = menu.findItem(R.id.action_addnotesopr);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_cmpltopr);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_viewnoteopr);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_viewnoteoprcancel);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_viewnoteoprcomplete);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_viewnoteoprcancelcmplt);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_viewnoteoprsave);
                    item.setVisible(true);

                    item = menu.findItem(R.id.action_viewnoteoprcancel);
                    item.setVisible(true);
                }
                else if(isComplete)
                {
                    item = menu.findItem(R.id.action_addnotesopr);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_cmpltopr);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_viewnoteopr);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_viewnoteoprcancel);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_viewnoteoprcomplete);
                    item.setVisible(true);

                    item = menu.findItem(R.id.action_viewnoteoprcancelcmplt);
                    item.setVisible(true);

                    item = menu.findItem(R.id.action_viewnoteoprsave);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_viewnoteoprcancel);
                    item.setVisible(false);
                }
                if(workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Completed")||workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("SUSPENDED")||workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Transfer"))
                {
                    item = menu.findItem(R.id.action_addnotesopr);
                    item.setVisible(false);
                    item = menu.findItem(R.id.action_cmpltopr);
                    item.setVisible(false);
                }

    //            onPrepareOptionsMenu(menu);
                return true;
            }
        } catch (Exception e) {
            Toast.makeText(OperationsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        // Pass through to super to setup touch menu.
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        try {
            getMenuInflater().inflate(R.menu.operations_menu, menu);
            menul=null;
            menul=menu;
            if (!isnotes&&!isComplete)
            {
                if(workOrderGlassEntity.getOprentities().get(cardposition).getMobileStatus().equalsIgnoreCase("Confirmed"))
                {
                    item = menu.findItem(R.id.action_addnotesopr);
                    item.setVisible(true);

                    item = menu.findItem(R.id.action_cmpltopr);
                    item.setVisible(false);
                }
                else {
                    item = menu.findItem(R.id.action_addnotesopr);
                    item.setVisible(true);

                    item = menu.findItem(R.id.action_cmpltopr);
                    item.setVisible(true);
                }


                item = menu.findItem(R.id.action_viewnoteopr);
                item.setVisible(true);

                item = menu.findItem(R.id.action_viewnoteoprsave);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnoteoprcancel);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnoteoprcomplete);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnoteoprcancelcmplt);
                item.setVisible(false);

            }
            else if(isnotes)
            {
                item = menu.findItem(R.id.action_addnotesopr);
                item.setVisible(false);

                item = menu.findItem(R.id.action_cmpltopr);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnoteopr);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnoteoprcancel);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnoteoprcomplete);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnoteoprcancelcmplt);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnoteoprsave);
                item.setVisible(true);

                item = menu.findItem(R.id.action_viewnoteoprcancel);
                item.setVisible(true);
            }
            else if(isComplete)
            {
                item = menu.findItem(R.id.action_addnotesopr);
                item.setVisible(false);

                item = menu.findItem(R.id.action_cmpltopr);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnoteopr);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnoteoprcancel);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnoteoprcomplete);
                item.setVisible(true);

                item = menu.findItem(R.id.action_viewnoteoprcancelcmplt);
                item.setVisible(true);

                item = menu.findItem(R.id.action_viewnoteoprsave);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnoteoprcancel);
                item.setVisible(false);
            }
            if(workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Completed")||workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("SUSPENDED")||workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Transfer"))
            {
                item = menu.findItem(R.id.action_addnotesopr);
                item.setVisible(false);
                item = menu.findItem(R.id.action_cmpltopr);
                item.setVisible(false);
            }
        } catch (Exception e) {
            Toast.makeText(OperationsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        super.onCreateOptionsMenu(menu);
        return true;
    }


    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        try {
            if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS|| featureId == Window.FEATURE_OPTIONS_PANEL) {
                switch (item.getItemId()) {

                    case R.id.action_addnotesopr:
                        displaySpeechRecognizer();
                        return true;
                    case R.id.action_cmpltopr:
                        isComplete=true;
                        getWindow().invalidatePanelMenu(WindowUtils.FEATURE_VOICE_COMMANDS);
                        setContentView(new CardBuilder(OperationsCard.this, CardBuilder.Layout.TEXT_FIXED)
                                .setText("Are you sure you want to complete?")
                                .setFootnote("Tap on this or Say Ok Glass")
                                .getView());

                        return true;
                    case R.id.action_viewnoteopr:
                        isnotes=false;
                        isViewNotes=true;
                        ArrayList<String> notes=HomeData.notesarray;
                        String notesopr = "";
                        for(int i=0;i<notes.size();i++)
                        {
                            String temp=notes.get(i);
                            notesopr=notesopr+temp+"\n";
                        }
                       // getWindow().invalidatePanelMenu(WindowUtils.FEATURE_VOICE_COMMANDS);
                        setContentView(new CardBuilder(OperationsCard.this, CardBuilder.Layout.TEXT_FIXED)
                                .setText(notesopr)
                                .setFootnote("Swipe down to go back")
                                .getView());

                        return true;
                    case R.id.action_viewnoteoprsave:
                        HomeData.notesarray.add(spokenText);
                        String addedNotes="OpeartionNotes,"+spokenText;
                        //BluetoothConnectionActivity.mService.write(addedNotes.getBytes());
                        reInitializeAct();
                        return true;
                    case R.id.action_viewnoteoprcancel:
                        reInitializeAct();
                        return true;

                    case R.id.action_viewnoteoprcomplete:
                        isComplete=false;
                        HomeData.notesarray.add(spokenText);
                        //HomeData.opr_Status="Completed";
                        workOrderGlassEntity.getOprentities().get(cardposition).setMobileStatus("Confirmed");
                        getOperationCardDetails();
                        String oprStatus=className+workOrderGlassEntity.getOprentities().get(cardposition).getMobileStatus();
                       // BluetoothConnectionActivity.mService.write(oprStatus.getBytes());
                        reInitializeAct();

                        return true;
                    case R.id.action_viewnoteoprcancelcmplt:
                        reInitializeAct();
                        return true;

                    default:
                        return true;
                }
            }
        } catch (Exception e) {
            Toast.makeText(OperationsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return super.onMenuItemSelected(featureId, item);
    }


}