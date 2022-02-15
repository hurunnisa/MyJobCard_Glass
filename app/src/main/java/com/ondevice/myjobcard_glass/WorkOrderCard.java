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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
public class WorkOrderCard extends Activity {

    /**
     * {@link CardScrollView} to use as the main content view.
     */
    private CardScrollView mCardScrollView;
    private List<CardBuilder> mCards;
    private WorkOrderCardScrollAdapter mAdapter;
    WorkOrderGlassEntity workOrderGlassEntity;
    String className="WorkOrder,";
    private static final int SPEECH_REQUEST = 0;
    String spokenText="";
    Menu menul;
    @Override
    protected void onCreate(Bundle bundle) {
        try {
            super.onCreate(bundle);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);
            getWindow().invalidatePanelMenu(WindowUtils.FEATURE_VOICE_COMMANDS);
            workOrderGlassEntity= HomeData.workOrderGlassEntity;//(WorkOrderGlassEntity) getIntent().getSerializableExtra("wodata");

            getCardDetails();
        } catch (Exception e) {
            Toast.makeText(WorkOrderCard.this, e.getMessage(), Toast.LENGTH_LONG).show();

        }

    }

    private void getCardDetails() {
        try {
            createCards();

            mCardScrollView = new CardScrollView(this);
            mAdapter = new WorkOrderCardScrollAdapter();
            mCardScrollView.setAdapter(mAdapter);
            mCardScrollView.activate();
            //setupClickListener();
            setContentView(mCardScrollView);
        } catch (Exception e) {
            Toast.makeText(WorkOrderCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Opening menu on tapping on D-pad
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                openOptionsMenu();
                invalidateOptionsMenu();
                return true;
            }
            else if(keyCode==KeyEvent.KEYCODE_BACK)
            {
                startActivity(new Intent(WorkOrderCard.this,HomeCardActivity.class));
                finish();
                return true;
            }
        } catch (Exception e) {
            Toast.makeText(WorkOrderCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.wo_menu, menu);
            if(workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Accepted"))
            {
                menu.findItem(R.id.action_transfer).setVisible(true);
                menu.findItem(R.id.action_enrote).setVisible(true);
            }
            else if(workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Started"))
            {
                menu.findItem(R.id.action_transfer).setVisible(true);
                menu.findItem(R.id.action_hold).setVisible(true);
                menu.findItem(R.id.action_suspend).setVisible(true);
                menu.findItem(R.id.action_complete).setVisible(true);
            }
            else if(workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Hold"))
            {
                menu.findItem(R.id.action_enrote).setVisible(true);
                menu.findItem(R.id.action_suspend).setVisible(true);
            }
            else if(workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Enroute"))
            {
                menu.findItem(R.id.action_onsite).setVisible(true);
            }
            else if(workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Arrived"))
            {
                menu.findItem(R.id.action_start).setVisible(true);
            }
        } catch (Exception e) {
            Toast.makeText(WorkOrderCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return true;
    }

    /**
     * Taking appropriate action on selecting menu item
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String actionStatus = className+(String) item.getTitle();
        try {
            switch (item.getItemId()) {
                case R.id.action_accept:
                   // BluetoothConnectionActivity.mService.write(actionStatus.getBytes());
                    workOrderGlassEntity.setMobileObjStatus("ACCEPTED");
                    getCardDetails();
                    return true;
                case R.id.action_reject:
                    //BluetoothConnectionActivity.mService.write(actionStatus.getBytes());
                    workOrderGlassEntity.setMobileObjStatus("REJECTED");
                    getCardDetails();
                    return true;
                case R.id.action_transfer:
                    //BluetoothConnectionActivity.mService.write(actionStatus.getBytes());
                    workOrderGlassEntity.setMobileObjStatus("TRANSFER");
                    getCardDetails();
                    return true;
                case R.id.action_enrote:
                   // BluetoothConnectionActivity.mService.write(actionStatus.getBytes());
                    workOrderGlassEntity.setMobileObjStatus("ENROUTE");
                    getCardDetails();
                    return true;
                case R.id.action_onsite:
                  //  BluetoothConnectionActivity.mService.write(actionStatus.getBytes());
                    workOrderGlassEntity.setMobileObjStatus("ARRIVED");
                    getCardDetails();
                    return true;
                case R.id.action_start:
                    //BluetoothConnectionActivity.mService.write(actionStatus.getBytes());
                    workOrderGlassEntity.setMobileObjStatus("STARTED");
                    getCardDetails();
                    return true;
                case R.id.action_hold:
                   // BluetoothConnectionActivity.mService.write(actionStatus.getBytes());
                    workOrderGlassEntity.setMobileObjStatus("HOLD");
                    getCardDetails();
                    return true;
                case R.id.action_suspend:
                    //BluetoothConnectionActivity.mService.write(actionStatus.getBytes());
                    workOrderGlassEntity.setMobileObjStatus("SUSPENDED");
                    getCardDetails();
                    return true;
                case R.id.action_complete:
                   // BluetoothConnectionActivity.mService.write(actionStatus.getBytes());
                    workOrderGlassEntity.setMobileObjStatus("COMPLETED");
                    getCardDetails();
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        } catch (Exception e) {
            Toast.makeText(WorkOrderCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return true;
    }
    private void createCards() {
        try {
            mCards = new ArrayList<CardBuilder>();

            mCards.add(new CardBuilder(this, CardBuilder.Layout.TEXT_FIXED)
                    .setText("WO Header \n \t\t Order Type          : "+workOrderGlassEntity.getOrderType()+" \n \t\t Equipment No    : "+workOrderGlassEntity.getEquipNum()+"\n \t\t Priority                 : "+workOrderGlassEntity.getPriority()+" \n \t\t Mobile Status     : "+workOrderGlassEntity.getMobileObjStatus())
                    .setFootnote("Tap or Say OK Glass for change status"));
            //.setTimestamp("just now"));

            mCards.add(new CardBuilder(this, CardBuilder.Layout.TEXT_FIXED)
                    .setText("Asset And Dates \n \t\t Equipment                : "+workOrderGlassEntity.getEquipNum()+"\n \t\t Functional Location : "+workOrderGlassEntity.getFuncLocation()+" \n \t\t Start Date                  : "+workOrderGlassEntity.getOprentities().get(0).getEarlSchStartExecDate()+" \n \t\t Schedule Finish        : "+workOrderGlassEntity.getOprentities().get(0).getEarlSchFinishExecDate())
                    .setFootnote("Tap or Say OK Glass for change status"));
            //.setTimestamp("just now"));

            mCards.add(new CardBuilder(this, CardBuilder.Layout.TEXT_FIXED)
                    .setText("Customer Info \n \t\t Name           : David\n \t\t Contact No  : 8765768999 \n \t\t Address       : Shady Lane,Birmingham,GB \n \t\t Bussiness Area: 0001 \n \t\t Work Center               : 0DEE0001 \n \t\t Maintenanace Plant  : OD01")
                    .setFootnote("Tap or Say OK Glass for change status"));
        } catch (Exception e) {
            Toast.makeText(WorkOrderCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        //.setTimestamp("just now"));
        //.setText("Customer Info \n \t\t Created On                 : 09-05-2019\n \t\t Person Responsible  : Sup User \n \t\t Bussiness Area          : 0001 \n \t\t Work Center               : 0DEE0001 \n \t\t Maintenanace Plant  : OD01")

    }

    private class WorkOrderCardScrollAdapter extends CardScrollAdapter {

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
            }
        });
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        try {
            menul=menu;
            if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS){
                getMenuInflater().inflate(R.menu.okglass_menu, menu);

                if(workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Accepted"))
                {
                    menu.findItem(R.id.action_transfer).setVisible(true);
                    menu.findItem(R.id.action_enrote).setVisible(true);
                }
                else if(workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Started"))
                {
                    menu.findItem(R.id.action_transfer).setVisible(true);
                    menu.findItem(R.id.action_hold).setVisible(true);
                    menu.findItem(R.id.action_suspend).setVisible(true);
                    menu.findItem(R.id.action_complete).setVisible(true);
                }
                else if(workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Hold"))
                {
                    menu.findItem(R.id.action_enrote).setVisible(true);
                    menu.findItem(R.id.action_suspend).setVisible(true);
                }
                else if(workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Enroute"))
                {
                    menu.findItem(R.id.action_onsite).setVisible(true);
                }
                else if(workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Arrived"))
                {
                    menu.findItem(R.id.action_start).setVisible(true);
                }


                return true;
            }
            return super.onCreatePanelMenu(featureId, menu);
        } catch (Exception e) {
            Toast.makeText(WorkOrderCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return true;
    }


    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        try {
            if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
                String actionStatus = className+(String) item.getTitle();
                if (item.getItemId() == R.id.operation_menu_item) {
                    Intent intent = new Intent(WorkOrderCard.this, OperationsCard.class);
                    intent.putExtra("wodata", workOrderGlassEntity);
                    startActivity(intent);
                    // startActivity(new Intent(WorkOrderCard.this, OperationsCard.class));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.comp_menu_item) {
                    startActivity(new Intent(WorkOrderCard.this, ComponentsCard.class));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.attach_menu_item) {
                    startActivity(new Intent(WorkOrderCard.this, AttachmentsCard.class));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.action_accept) {
                    //openOptionsMenu();
                    // onMenuOpened(WindowUtils.FEATURE_VOICE_COMMANDS,menul);
                    //displaySpeechRecognizer();
                    //BluetoothConnectionActivity.mService.write(actionStatus.getBytes());
                    workOrderGlassEntity.setMobileObjStatus("ACCEPTED");
                    getCardDetails();
                } else if (item.getItemId() == R.id.action_reject) {
                    //BluetoothConnectionActivity.mService.write(actionStatus.getBytes());
                    workOrderGlassEntity.setMobileObjStatus("REJECTED");
                    getCardDetails();

                } else if (item.getItemId() == R.id.action_transfer) {
                   // BluetoothConnectionActivity.mService.write(actionStatus.getBytes());
                    workOrderGlassEntity.setMobileObjStatus("TRANSFER");
                    getCardDetails();
                } else if (item.getItemId() == R.id.action_enrote) {
                   // BluetoothConnectionActivity.mService.write(actionStatus.getBytes());
                    workOrderGlassEntity.setMobileObjStatus("ENROUTE");
                    getCardDetails();
                } else if (item.getItemId() == R.id.action_onsite) {
                   // BluetoothConnectionActivity.mService.write(actionStatus.getBytes());
                    workOrderGlassEntity.setMobileObjStatus("ARRIVED");
                    getCardDetails();
                } else if (item.getItemId() == R.id.action_start) {
                    //BluetoothConnectionActivity.mService.write(actionStatus.getBytes());
                    workOrderGlassEntity.setMobileObjStatus("STARTED");
                    getCardDetails();
                } else if (item.getItemId() == R.id.action_hold) {
                  //  BluetoothConnectionActivity.mService.write(actionStatus.getBytes());
                    workOrderGlassEntity.setMobileObjStatus("HOLD");
                    getCardDetails();
                } else if (item.getItemId() == R.id.action_suspend) {
                   // BluetoothConnectionActivity.mService.write(actionStatus.getBytes());
                    workOrderGlassEntity.setMobileObjStatus("SUSPENDED");
                    getCardDetails();
                } else if (item.getItemId() == R.id.action_complete) {
                   // BluetoothConnectionActivity.mService.write(actionStatus.getBytes());
                    workOrderGlassEntity.setMobileObjStatus("COMPLETED");
                    getCardDetails();
                }
            }

            return super.onMenuItemSelected(featureId, item);
        } catch (Exception e) {
            Toast.makeText(WorkOrderCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return true;
    }
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        try {
            if(menu!=null) {
                Log.d("tag", "menu is ready");
                if(featureId==WindowUtils.FEATURE_VOICE_COMMANDS) {
                    if (workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Accepted")) {
                        menu.findItem(R.id.action_transfer).setVisible(true);
                        menu.findItem(R.id.action_enrote).setVisible(true);

                        menu.findItem(R.id.action_hold).setVisible(false);
                        menu.findItem(R.id.action_suspend).setVisible(false);
                        menu.findItem(R.id.action_complete).setVisible(false);
                        menu.findItem(R.id.action_onsite).setVisible(false);
                        menu.findItem(R.id.action_start).setVisible(false);
                    } else if (workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Started")) {
                        menu.findItem(R.id.action_transfer).setVisible(true);
                        menu.findItem(R.id.action_hold).setVisible(true);
                        menu.findItem(R.id.action_suspend).setVisible(true);
                        menu.findItem(R.id.action_complete).setVisible(true);

                        menu.findItem(R.id.action_enrote).setVisible(false);
                        menu.findItem(R.id.action_onsite).setVisible(false);
                        menu.findItem(R.id.action_start).setVisible(false);
                    } else if (workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Hold")) {
                        menu.findItem(R.id.action_enrote).setVisible(true);
                        menu.findItem(R.id.action_suspend).setVisible(true);

                        menu.findItem(R.id.action_transfer).setVisible(false);
                        menu.findItem(R.id.action_hold).setVisible(false);
                        menu.findItem(R.id.action_complete).setVisible(false);
                        menu.findItem(R.id.action_onsite).setVisible(false);
                        menu.findItem(R.id.action_start).setVisible(false);

                    } else if (workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Enroute")) {
                        menu.findItem(R.id.action_onsite).setVisible(true);

                        menu.findItem(R.id.action_enrote).setVisible(false);
                        menu.findItem(R.id.action_suspend).setVisible(false);
                        menu.findItem(R.id.action_transfer).setVisible(false);
                        menu.findItem(R.id.action_hold).setVisible(false);
                        menu.findItem(R.id.action_complete).setVisible(false);
                        menu.findItem(R.id.action_start).setVisible(false);
                    } else if (workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Arrived")) {
                        menu.findItem(R.id.action_start).setVisible(true);

                        menu.findItem(R.id.action_onsite).setVisible(false);
                        menu.findItem(R.id.action_enrote).setVisible(false);
                        menu.findItem(R.id.action_suspend).setVisible(false);
                        menu.findItem(R.id.action_transfer).setVisible(false);
                        menu.findItem(R.id.action_hold).setVisible(false);
                        menu.findItem(R.id.action_complete).setVisible(false);
                    }
                    else if (workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Completed")||workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("SUSPENDED")||workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Transfer")) {
                        menu.findItem(R.id.action_start).setVisible(false);

                        menu.findItem(R.id.action_onsite).setVisible(false);
                        menu.findItem(R.id.action_enrote).setVisible(false);
                        menu.findItem(R.id.action_suspend).setVisible(false);
                        menu.findItem(R.id.action_transfer).setVisible(false);
                        menu.findItem(R.id.action_hold).setVisible(false);
                        menu.findItem(R.id.action_complete).setVisible(false);
                    }

                }
            } else {
                Log.d("tag", "menu is null yet");
            }
            return super.onMenuOpened(featureId, menu);
        } catch (Exception e) {
            Toast.makeText(WorkOrderCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return true;
    }
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        startActivityForResult(intent, SPEECH_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            spokenText = results.get(0);
            // Do something with spokenText.

            //HomeData.addNotes_opr=spokenText;
            getWindow().invalidatePanelMenu(WindowUtils.FEATURE_VOICE_COMMANDS);
            //HomeData.notesarray.add(spokenText);
            setContentView(new CardBuilder(WorkOrderCard.this, CardBuilder.Layout.TEXT_FIXED)
                    .setText(spokenText)
                    .setFootnote("Tap on this")
                    .getView());

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

  /*  @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

            getMenuInflater().inflate(R.menu.okglass_menu, menu);

            if(workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Accepted"))
            {
                menu.findItem(R.id.action_transfer).setVisible(true);
                menu.findItem(R.id.action_enrote).setVisible(true);
            }
            else if(workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Started"))
            {
                menu.findItem(R.id.action_transfer).setVisible(true);
                menu.findItem(R.id.action_hold).setVisible(true);
                menu.findItem(R.id.action_suspend).setVisible(true);
                menu.findItem(R.id.action_complete).setVisible(true);
            }
            else if(workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Hold"))
            {
                menu.findItem(R.id.action_enrote).setVisible(true);
                menu.findItem(R.id.action_suspend).setVisible(true);
            }
            else if(workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Enroute"))
            {
                menu.findItem(R.id.action_onsite).setVisible(true);
            }
            else if(workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Arrived"))
            {
                menu.findItem(R.id.action_start).setVisible(true);
            }

        return super.onPrepareOptionsMenu(menu);
    }*/
}
