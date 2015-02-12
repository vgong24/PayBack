package com.example.victor.payback;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


//Help
//https://www.parse.com/tutorials/anywall-android#2-manage-users

public class HomeScreen extends ActionBarActivity {
    private static final int EDIT = 0, DELETE = 1;

    //TextFields
    EditText nameTxt, phoneTxt, descTxt, amountTxt;
    Switch debtSwitch;
    String amtString = "", debtTypeTxt;// = amountTxt.getText().toString().trim();
    double amtdbl = 0.0;   // Double.parseDouble(amtString);
    Button saveBtn = null;
    Button logOut = null;

    DatabaseHandler dbHandler;
    List<DebtProfile> profiles = new ArrayList<DebtProfile>();
    ListView profileListView;

    int longClickedItemIndex;
    ArrayAdapter<DebtProfile> profileAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        /*
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "a2DlMumhUo2cXhX1UnY9nG7PxDUCfZf3glxTa7Ps", "bF6kYXauBnQMI1YhwRFcF0qInpibEBCnxj0YMoWU");

        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "barrrr");
        testObject.saveInBackground();
        */



        //Initialize global variables
        initVariables();

        registerForContextMenu(profileListView);
        profileListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longClickedItemIndex = position;

                return false;
            }
        });

        //Turn off button on start until i figure out how to check if fields are filled
        saveBtn.setEnabled(false);

        //TextListeners
        textListeners();
        //Creating TabHost
        initTabs();

        logOut.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                startActivity(new Intent(v.getContext(), DispatchActivity.class));
            }

        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debtSwitch.isChecked()){
                    debtTypeTxt = "Owe";
                }else{
                    debtTypeTxt = "Spotted";
                }

               DebtProfile profile = new DebtProfile(dbHandler.getProfileCount(), String.valueOf(nameTxt.getText()), Double.parseDouble(amountTxt.getText().toString()), debtTypeTxt, "", String.valueOf(descTxt.getText()));
               if(!profileExists(profile)) {
                   dbHandler.createDebtProfile(profile);
                   profiles.add(profile);
                   profileAdapter.notifyDataSetChanged();

                   Toast.makeText(getApplicationContext(), String.valueOf(nameTxt.getText()) + " has been added", Toast.LENGTH_SHORT).show();
                   clearFields();
                   return;
               }
                Toast.makeText(getApplicationContext(), String.valueOf(nameTxt.getText()) + " already exists. Please use a different name", Toast.LENGTH_SHORT).show();
                //Clear fields
                clearFields();

            }
        });

        if(dbHandler.getProfileCount() != 0 )
        profiles.addAll(dbHandler.getAllProfiles());
        populateList();


    }

    /**=============================================================================================
     * OnCreate Initializations
     */
    private void initVariables(){
        nameTxt = (EditText) findViewById(R.id.pName);
        //phoneTxt = (EditText) findViewById(R.id.)
        amountTxt = (EditText) findViewById(R.id.amountEdit);
        amountTxt.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(7,2)});
        debtSwitch = (Switch) findViewById(R.id.debtSwitch);
        descTxt = (EditText) findViewById(R.id.descriptionEdit);
        saveBtn = (Button) findViewById(R.id.saveButton);
        logOut = (Button) findViewById(R.id.logOutBtn);
        profileListView = (ListView) findViewById(R.id.listView);

        dbHandler = new DatabaseHandler(getApplicationContext());
    }

    private void initTabs(){
        TabHost tabhost = (TabHost) findViewById(R.id.tabHost);
        tabhost.setup();
        TabHost.TabSpec tabSpec = tabhost.newTabSpec("all");
        tabSpec.setContent(R.id.allTab);
        tabSpec.setIndicator("ALL");
        tabhost.addTab(tabSpec);

        tabSpec = tabhost.newTabSpec("spotted");
        tabSpec.setContent(R.id.spotTab);
        tabSpec.setIndicator("Spotted");
        tabhost.addTab(tabSpec);

        tabSpec = tabhost.newTabSpec("owed");
        tabSpec.setContent(R.id.oweTab);
        tabSpec.setIndicator("Owe");
        tabhost.addTab(tabSpec);

        tabSpec = tabhost.newTabSpec("add Debt");
        tabSpec.setContent(R.id.addDebt);
        tabSpec.setIndicator("Add Debt");
        tabhost.addTab(tabSpec);

    }

    private void clearFields(){
        nameTxt.setText("");
        amountTxt.setText("");
        descTxt.setText("");
        debtSwitch.setChecked(false);
    }

    //Initializations ==============================================================================

    /**=============================================================================================
     * Long pressing profiles on listView
     *
     */
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);

        menu.setHeaderIcon(R.drawable.pencil_icon);
        menu.setHeaderTitle("Profile Options");
        menu.add(Menu.NONE, EDIT, menu.NONE, "Edit Profile");
        menu.add(Menu.NONE, DELETE, menu.NONE, "Delete Profile");



    }

    public boolean onContextItemSelected(MenuItem item){
        switch(item.getItemId()){
            case EDIT:
                    //TODO: Implement editing a profile
                break;
            case DELETE:
                dbHandler.deleteDebtProfile(profiles.get(longClickedItemIndex));
                profiles.remove(longClickedItemIndex);
                profileAdapter.notifyDataSetChanged();

                break;
            default:
                Toast.makeText(getApplicationContext(),"Nothing happened", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    //==============================================================================================

    private boolean profileExists(DebtProfile profile){
        String name = profile.getName();
        int profileCount = profiles.size();

        for (int i = 0; i < profileCount; i++){
            if(name.compareToIgnoreCase(profiles.get(i).getName()) == 0)
                return true;
        }

        return false;

    }

    private void textListeners(){
        //Text changes
        nameTxt.addTextChangedListener(new TextWatcher() {
            //move to top

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveBtn.setEnabled(!String.valueOf(nameTxt.getText()).trim().isEmpty() && !amountTxt.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        amountTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                saveBtn.setEnabled(!String.valueOf(nameTxt.getText()).trim().isEmpty() && !amountTxt.getText().toString().trim().isEmpty());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveBtn.setEnabled(!String.valueOf(nameTxt.getText()).trim().isEmpty() && !amountTxt.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //  saveBtn.setEnabled();

            }
        });
    }



    private void populateList(){
       profileAdapter = new ProfileListAdapter();
       profileListView.setAdapter(profileAdapter);
    }


    /**
     * Class
     *
     */
    private class ProfileListAdapter extends ArrayAdapter<DebtProfile> {
        public ProfileListAdapter(){
            super(HomeScreen.this, R.layout.listview_item, profiles);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){
            if(view == null)
                view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);

            DebtProfile currentProfile = profiles.get(position);

            TextView name = (TextView) view.findViewById(R.id.pNameDisplay);
            name.setText(currentProfile.getName());
            TextView amt = (TextView) view.findViewById(R.id.amountDisplay);
            amt.setText(String.valueOf(currentProfile.getAmount()));
            TextView description = (TextView) view.findViewById(R.id.descDisplay);
            description.setText(currentProfile.getDescription());
            TextView debt = (TextView) view.findViewById(R.id.debtTypeDisplay);
            debt.setText(currentProfile.getDebtType());

            return view;

        }

    }

    /**=============================================================================================
     * Standard code: Untouched.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //=============================================================================================

    /**
     * Debug actions.
     * @param msg
     */
    public void debugText(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


}
