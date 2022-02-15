package com.ondevice.myjobcard_glass;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.view.WindowUtils;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;
import com.ondevice.myjobcard_glass.entities.WorkOrderGlassEntity;

import android.app.Activity;
import android.content.Context;
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
public class ComponentsCard extends Activity {

    /**
     * {@link CardScrollView} to use as the main content view.
     */
    private CardScrollView mCardScrollView;
    private List<CardBuilder> mCards;
    private ComponentsScrollAdapter mAdapter;
    Boolean isnotes=false,isIsuue=false,isAvailable=false,isissueAlert=false;
    private static final int SPEECH_REQUEST = 0;
    String spokenText="";
    MenuItem item;
    Menu menul;
    WorkOrderGlassEntity workOrderGlassEntity;
    int positioncard;
    String className="Component,";

    @Override
    protected void onCreate(Bundle bundle) {
        try {
            super.onCreate(bundle);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);
            workOrderGlassEntity=HomeData.workOrderGlassEntity;
            getComponenetCardDetails();
        } catch (Exception e) {
            Toast.makeText(ComponentsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void getComponenetCardDetails() {
        try {
            createCards();

            mCardScrollView = new CardScrollView(this);
            mAdapter = new ComponentsScrollAdapter();
            mCardScrollView.setAdapter(mAdapter);
            mCardScrollView.activate();
            setupClickListener();
            getWindow().invalidatePanelMenu(WindowUtils.FEATURE_VOICE_COMMANDS);
            setContentView(mCardScrollView);
        } catch (Exception e) {
            Toast.makeText(ComponentsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void createCards() {
        try {
            mCards = new ArrayList<CardBuilder>();
            if (workOrderGlassEntity.getComponentGlassEntities().size()>0) {
                isAvailable=true;
                for (int i = 0; i < workOrderGlassEntity.getComponentGlassEntities().size(); i++) {
                    mCards.add(new CardBuilder(this, CardBuilder.Layout.COLUMNS)
                            .setText("Component " + workOrderGlassEntity.getComponentGlassEntities().get(i).getItem() +"\n "+ workOrderGlassEntity.getComponentGlassEntities().get(i).getMaterial()+" - "+workOrderGlassEntity.getComponentGlassEntities().get(i).getMaterialDescription()+ "\n Required Qty  : " + workOrderGlassEntity.getComponentGlassEntities().get(i).getReqmtQty() + "\n WithdrawnQty: " + workOrderGlassEntity.getComponentGlassEntities().get(i).getWithdrawalQty())
                            .setFootnote("Tap on this or Say Ok Glass")
                            .addImage(R.drawable.componentsgls));

                }

            }
            else
            {
                isAvailable=false;
                mCards.add(new CardBuilder(this, CardBuilder.Layout.ALERT)
                        .setIcon(R.drawable.warning_icon)
                        .setText("No Components are available"));
            }
        } catch (Exception e) {
            Toast.makeText(ComponentsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                if(isAvailable) {
                    openOptionsMenu();
                    return true;
                }
                else
                {
                    return true;
                }
            }
            else if(keyCode==KeyEvent.KEYCODE_BACK)
            {
                if(!isnotes&&!isIsuue&&!isissueAlert) {
                    startActivity(new Intent(ComponentsCard.this, WorkOrderCard.class));
                    finish();
                    return true;
                }
                else
                {
                    isnotes=false;
                    isIsuue=false;
                    isissueAlert=false;
                    startActivity(new Intent(ComponentsCard.this,ComponentsCard.class));
                    finish();
                }
            }
        } catch (Exception e) {
            Toast.makeText(ComponentsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return false;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        try {
            if (!isnotes&&!isIsuue)
            {
                if(workOrderGlassEntity.getComponentGlassEntities().get(positioncard).getReqmtQty()==workOrderGlassEntity.getComponentGlassEntities().get(positioncard).getWithdrawalQty())
                {
                    item = menu.findItem(R.id.action_addnotesComp);
                    item.setVisible(true);

                    item = menu.findItem(R.id.action_issueComp);
                    item.setVisible(false);
                }
                else
                {
                    item = menu.findItem(R.id.action_addnotesComp);
                    item.setVisible(true);

                    item = menu.findItem(R.id.action_issueComp);
                    item.setVisible(true);
                }


                item = menu.findItem(R.id.action_viewnotecomp);
                item.setVisible(true);

                item = menu.findItem(R.id.action_viewNotessavecom);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewNotescancelcom);
                item.setVisible(false);

                item = menu.findItem(R.id.action_issuesavecomponenet);
                item.setVisible(false);

                item = menu.findItem(R.id.action_issuecancelcomponent);
                item.setVisible(false);

            }
            else if(isnotes)
            {
                item = menu.findItem(R.id.action_addnotesComp);
                item.setVisible(false);

                item = menu.findItem(R.id.action_issueComp);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnotecomp);
                item.setVisible(false);

                item = menu.findItem(R.id.action_issuesavecomponenet);
                item.setVisible(false);

                item = menu.findItem(R.id.action_issuecancelcomponent);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewNotessavecom);
                item.setVisible(true);

                item = menu.findItem(R.id.action_viewNotescancelcom);
                item.setVisible(true);
            }
            else if(isIsuue)
            {
                item = menu.findItem(R.id.action_addnotesComp);
                item.setVisible(false);

                item = menu.findItem(R.id.action_issueComp);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnotecomp);
                item.setVisible(false);

                item = menu.findItem(R.id.action_issuesavecomponenet);
                item.setVisible(true);

                item = menu.findItem(R.id.action_issuecancelcomponent);
                item.setVisible(true);

                item = menu.findItem(R.id.action_viewNotessavecom);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewNotescancelcom);
                item.setVisible(false);
            }
            if(workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Completed")||workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("SUSPENDED")||workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Transfer"))
            {
                item = menu.findItem(R.id.action_addnotesComp);
                item.setVisible(false);
                item = menu.findItem(R.id.action_issueComp);
                item.setVisible(false);
            }
        } catch (Exception e) {
            Toast.makeText(ComponentsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return super.onPrepareOptionsMenu(menu);
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        getMenuInflater().inflate(R.menu.components_menu, menu);
//        menul=menu;
//        if (!isnotes)
//        {
//            item = menu.findItem(R.id.action_addnotesComp);
//            item.setVisible(true);
//
//            item = menu.findItem(R.id.action_issueComp);
//            item.setVisible(true);
//
//            item = menu.findItem(R.id.action_viewnotecomp);
//            item.setVisible(true);
//
//            item = menu.findItem(R.id.action_viewNotessavecom);
//            item.setVisible(false);
//
//            item = menu.findItem(R.id.action_viewNotescancelcom);
//            item.setVisible(false);
//
//            item = menu.findItem(R.id.action_issuesavecomponenet);
//            item.setVisible(false);
//
//            item = menu.findItem(R.id.action_issuecancelcomponent);
//            item.setVisible(false);
//
//        }
//
//        super.onCreateOptionsMenu(menu);
//        return true;
//    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.components_menu, menu);
//        return true;
//    }

    /**
     * Taking appropriate action on selecting menu item
     * */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.action_addnotesComp:
//                displaySpeechRecognizer(item.getItemId());
//                return true;
//            case R.id.action_issueComp:
////                isIsuue=true;
////                setContentView(new CardBuilder(ComponentsCard.this, CardBuilder.Layout.TEXT_FIXED)
////                        .setText("Are you sure you want to complete?")
////                        .setFootnote("Tap on this")
////                        .getView());
//                if(workOrderGlassEntity.getComponentGlassEntities().get(positioncard).getReqmtQty()<workOrderGlassEntity.getComponentGlassEntities().get(positioncard).getWithdrawalQty())
//                {
//                    setContentView(new CardBuilder(ComponentsCard.this, CardBuilder.Layout.TEXT_FIXED)
//                        .setText("You can not withdraw more than "+workOrderGlassEntity.getComponentGlassEntities().get(positioncard).getReqmtQty())
//                        .setFootnote("Tap on this")
//                        .getView());
//                }
//                else {
//                    displaySpeechRecognizer(item.getItemId());
//                }
//
//                return true;
//            case R.id.action_viewnotecomp:
//                isnotes=true;
//                ArrayList<String> notes=HomeData.notesarray_comp;
//                String notescom = "";
//                for(int i=0;i<notes.size();i++)
//                {
//                    String temp=notes.get(i);
//                    notescom=notescom+temp+"\n";
//                }
//                setContentView(new CardBuilder(ComponentsCard.this, CardBuilder.Layout.TEXT_FIXED)
//                        .setText(notescom)
//                        .setFootnote("Swipe down to cancle")
//                        .getView());
//
//                return true;
//            case R.id.action_viewNotessavecom:
//                HomeData.notesarray_comp.add(spokenText);
//                reInitializeAct();
//                return true;
//            case R.id.action_viewNotescancelcom:
//                reInitializeAct();
//                return true;
//
//            case R.id.action_issuesavecomponenet:
//                HomeData.component_ReqQty=spokenText;
//                if(workOrderGlassEntity.getComponentGlassEntities().get(positioncard).getReqmtQty()<Integer.parseInt(spokenText))
//                {
//                    setContentView(new CardBuilder(ComponentsCard.this, CardBuilder.Layout.TEXT_FIXED)
//                            .setText("You can not withdraw more than "+workOrderGlassEntity.getComponentGlassEntities().get(positioncard).getReqmtQty())
//                            .setFootnote("Swipe down to cancle")
//                            .getView());
//
//                }
//                else {
//
//
//                    workOrderGlassEntity.getComponentGlassEntities().get(positioncard).setWithdrawalQty(Integer.parseInt(spokenText));
//                    getComponenetCardDetails();
//                    int withQty = workOrderGlassEntity.getComponentGlassEntities().get(positioncard).getWithdrawalQty();
//                    String data = String.valueOf(withQty);
//                    //BluetoothConnectionActivity.mService.write(data.getBytes());
//                    reInitializeAct();
//                }
//                return true;
//            case R.id.action_issuecancelcomponent:
//                HomeData.component_ReqQty="";
//                reInitializeAct();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    private void displaySpeechRecognizer(int itemId) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        startActivityForResult(intent, itemId);
    }
    public void reInitializeAct()
    {
        startActivity(new Intent(ComponentsCard.this,ComponentsCard.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        try {
            if (requestCode == R.id.action_addnotesComp && resultCode == RESULT_OK) {
                List<String> results = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                spokenText = results.get(0);
                // Do something with spokenText.
                isnotes=true;
                HomeData.addNotes_component=spokenText;
                getWindow().invalidatePanelMenu(WindowUtils.FEATURE_VOICE_COMMANDS);
                setContentView(new CardBuilder(ComponentsCard.this, CardBuilder.Layout.TEXT_FIXED)
                        .setText(spokenText)
                        .setFootnote("Tap on this or Say Ok Glass")
                        .getView());

            }
            else if(requestCode==R.id.action_issueComp && resultCode==RESULT_OK)
            {
                spokenText="";
                List<String> results = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                spokenText = results.get(0);
                try {
                    double withQty=Double.valueOf(spokenText);
                    int withdrawnQty=Integer.parseInt(spokenText)+workOrderGlassEntity.getComponentGlassEntities().get(positioncard).getWithdrawalQty();
                    if(workOrderGlassEntity.getComponentGlassEntities().get(positioncard).getReqmtQty()<withdrawnQty)
                    {
                        isIsuue=false;
                        isissueAlert=true;
                        getWindow().invalidatePanelMenu(WindowUtils.FEATURE_VOICE_COMMANDS);
                        setContentView(new CardBuilder(ComponentsCard.this, CardBuilder.Layout.TEXT_FIXED)
                                .setText("You can not withdraw more than "+workOrderGlassEntity.getComponentGlassEntities().get(positioncard).getReqmtQty())
                                .setFootnote("Swipe down to cancel")
                                .getView());
                    }
                    else {
                        HomeData.finalwithDrawnQty=withdrawnQty;
                        isIsuue = true;
                        isissueAlert=false;
                        getWindow().invalidatePanelMenu(WindowUtils.FEATURE_VOICE_COMMANDS);
                        setContentView(new CardBuilder(ComponentsCard.this, CardBuilder.Layout.TEXT_FIXED)
                                .setText("Withdrawn Qty = " + spokenText)
                                .setFootnote("Tap on this or Say Ok Glass")
                                .getView());
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    isnotes=false;isIsuue=false;isAvailable=false;isissueAlert=false;
                    createCards();
                    Toast.makeText(ComponentsCard.this, "Please Give Valid Withdrawn Quantity", Toast.LENGTH_LONG).show();

                }


            }
        } catch (Exception e) {
            Toast.makeText(ComponentsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private class ComponentsScrollAdapter extends CardScrollAdapter {

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
                positioncard=position;
                openOptionsMenu();
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
                getMenuInflater().inflate(R.menu.components_menu, menu);
                menul=null;
                menul=menu;
                if (!isnotes&&!isIsuue)
                {
                    if(workOrderGlassEntity.getComponentGlassEntities().get(positioncard).getReqmtQty()==workOrderGlassEntity.getComponentGlassEntities().get(positioncard).getWithdrawalQty())
                    {
                        item = menu.findItem(R.id.action_addnotesComp);
                        item.setVisible(true);

                        item = menu.findItem(R.id.action_issueComp);
                        item.setVisible(false);
                    }
                    else
                    {
                        item = menu.findItem(R.id.action_addnotesComp);
                        item.setVisible(true);

                        item = menu.findItem(R.id.action_issueComp);
                        item.setVisible(true);
                    }


                    item = menu.findItem(R.id.action_viewnotecomp);
                    item.setVisible(true);

                    item = menu.findItem(R.id.action_viewNotessavecom);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_viewNotescancelcom);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_issuesavecomponenet);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_issuecancelcomponent);
                    item.setVisible(false);

                }
                else if(isnotes)
                {
                    item = menu.findItem(R.id.action_addnotesComp);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_issueComp);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_viewnotecomp);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_issuesavecomponenet);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_issuecancelcomponent);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_viewNotessavecom);
                    item.setVisible(true);

                    item = menu.findItem(R.id.action_viewNotescancelcom);
                    item.setVisible(true);
                }
                else if(isIsuue)
                {
                    item = menu.findItem(R.id.action_addnotesComp);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_issueComp);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_viewnotecomp);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_issuesavecomponenet);
                    item.setVisible(true);

                    item = menu.findItem(R.id.action_issuecancelcomponent);
                    item.setVisible(true);

                    item = menu.findItem(R.id.action_viewNotessavecom);
                    item.setVisible(false);

                    item = menu.findItem(R.id.action_viewNotescancelcom);
                    item.setVisible(false);
                }
                if(workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Completed")||workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("SUSPENDED")||workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Transfer"))
                {
                    item = menu.findItem(R.id.action_addnotesComp);
                    item.setVisible(false);
                    item = menu.findItem(R.id.action_issueComp);
                    item.setVisible(false);
                }

    //            onPrepareOptionsMenu(menu);
                return true;
            }
        } catch (Exception e) {
            Toast.makeText(ComponentsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        // Pass through to super to setup touch menu.
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        try {
            getMenuInflater().inflate(R.menu.components_menu, menu);
            menul=null;
            menul=menu;
            if (!isnotes&&!isIsuue)
            {
                if(workOrderGlassEntity.getComponentGlassEntities().get(positioncard).getReqmtQty()==workOrderGlassEntity.getComponentGlassEntities().get(positioncard).getWithdrawalQty())
                {
                    item = menu.findItem(R.id.action_addnotesComp);
                    item.setVisible(true);

                    item = menu.findItem(R.id.action_issueComp);
                    item.setVisible(false);
                }
                else
                {
                    item = menu.findItem(R.id.action_addnotesComp);
                    item.setVisible(true);

                    item = menu.findItem(R.id.action_issueComp);
                    item.setVisible(true);
                }


                item = menu.findItem(R.id.action_viewnotecomp);
                item.setVisible(true);

                item = menu.findItem(R.id.action_viewNotessavecom);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewNotescancelcom);
                item.setVisible(false);

                item = menu.findItem(R.id.action_issuesavecomponenet);
                item.setVisible(false);

                item = menu.findItem(R.id.action_issuecancelcomponent);
                item.setVisible(false);

            }
            else if(isnotes)
            {
                item = menu.findItem(R.id.action_addnotesComp);
                item.setVisible(false);

                item = menu.findItem(R.id.action_issueComp);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnotecomp);
                item.setVisible(false);

                item = menu.findItem(R.id.action_issuesavecomponenet);
                item.setVisible(false);

                item = menu.findItem(R.id.action_issuecancelcomponent);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewNotessavecom);
                item.setVisible(true);

                item = menu.findItem(R.id.action_viewNotescancelcom);
                item.setVisible(true);
            }
            else if(isIsuue)
            {
                item = menu.findItem(R.id.action_addnotesComp);
                item.setVisible(false);

                item = menu.findItem(R.id.action_issueComp);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewnotecomp);
                item.setVisible(false);

                item = menu.findItem(R.id.action_issuesavecomponenet);
                item.setVisible(true);

                item = menu.findItem(R.id.action_issuecancelcomponent);
                item.setVisible(true);

                item = menu.findItem(R.id.action_viewNotessavecom);
                item.setVisible(false);

                item = menu.findItem(R.id.action_viewNotescancelcom);
                item.setVisible(false);
            }
            if(workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Completed")||workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("SUSPENDED")||workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Transfer"))
            {
                item = menu.findItem(R.id.action_addnotesComp);
                item.setVisible(false);
                item = menu.findItem(R.id.action_issueComp);
                item.setVisible(false);
            }
        } catch (Exception e) {
            Toast.makeText(ComponentsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        super.onCreateOptionsMenu(menu);
        return true;
    }


    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        try {
            if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS|| featureId == Window.FEATURE_OPTIONS_PANEL) {
                switch (item.getItemId()) {

                    case R.id.action_addnotesComp:
                        displaySpeechRecognizer(item.getItemId());
                        return true;
                    case R.id.action_issueComp:
    //                isIsuue=true;
    //                setContentView(new CardBuilder(ComponentsCard.this, CardBuilder.Layout.TEXT_FIXED)
    //                        .setText("Are you sure you want to complete?")
    //                        .setFootnote("Tap on this")
    //                        .getView());
                        if(workOrderGlassEntity.getComponentGlassEntities().get(positioncard).getReqmtQty()<workOrderGlassEntity.getComponentGlassEntities().get(positioncard).getWithdrawalQty())
                        {
                            setContentView(new CardBuilder(ComponentsCard.this, CardBuilder.Layout.TEXT_FIXED)
                                    .setText("You can not withdraw more than "+workOrderGlassEntity.getComponentGlassEntities().get(positioncard).getReqmtQty())
                                    .setFootnote("Swipe down to cancel")
                                    .getView());
                        }
                        else {
                            displaySpeechRecognizer(item.getItemId());
                        }

                        return true;
                    case R.id.action_viewnotecomp:
                        isnotes=true;
                        ArrayList<String> notes=HomeData.notesarray_comp;
                        String notescom = "";
                        for(int i=0;i<notes.size();i++)
                        {
                            String temp=notes.get(i);
                            notescom=notescom+temp+"\n";
                        }
                        setContentView(new CardBuilder(ComponentsCard.this, CardBuilder.Layout.TEXT_FIXED)
                                .setText(notescom)
                                .setFootnote("Swipe down to go back")
                                .getView());

                        return true;
                    case R.id.action_viewNotessavecom:
                        HomeData.notesarray_comp.add(spokenText);
                        String addedNotes="ComponentNotes,"+spokenText;
                        //BluetoothConnectionActivity.mService.write(addedNotes.getBytes());
                        reInitializeAct();
                        return true;
                    case R.id.action_viewNotescancelcom:
                        reInitializeAct();
                        return true;

                    case R.id.action_issuesavecomponenet:
                        HomeData.component_withQty=spokenText;
                        if(workOrderGlassEntity.getComponentGlassEntities().get(positioncard).getReqmtQty()<Integer.parseInt(spokenText))
                        {
                            isIsuue=false;
                            setContentView(new CardBuilder(ComponentsCard.this, CardBuilder.Layout.TEXT_FIXED)
                                    .setText("You can not withdraw more than "+workOrderGlassEntity.getComponentGlassEntities().get(positioncard).getReqmtQty())
                                    .setFootnote("Swipe down to cancel")
                                    .getView());

                        }
                        else {

                            isIsuue=false;
                            workOrderGlassEntity.getComponentGlassEntities().get(positioncard).setWithdrawalQty(HomeData.finalwithDrawnQty);
                            getComponenetCardDetails();
                            if(HomeData.finalwithDrawnQty==workOrderGlassEntity.getComponentGlassEntities().get(positioncard).getReqmtQty())
                            {
                                HomeData.finalwithDrawnQty=0;
                            }
                            //int withQty = workOrderGlassEntity.getComponentGlassEntities().get(positioncard).getWithdrawalQty();
                            int withQty = Integer.parseInt(HomeData.component_withQty);
                            String data = className+String.valueOf(withQty);
                           // BluetoothConnectionActivity.mService.write(data.getBytes());
                            reInitializeAct();
                        }
                        return true;
                    case R.id.action_issuecancelcomponent:
                        HomeData.component_withQty="";
                        reInitializeAct();
                        return true;

                    default:
                        return true;
                }
            }
        } catch (NumberFormatException e) {
            Toast.makeText(ComponentsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return super.onMenuItemSelected(featureId, item);
    }

}