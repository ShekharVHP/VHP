package katheria.vhp.Activity;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import katheria.vhp.Fragment.HomeFragment;
import katheria.vhp.Http.HttpCall;
import katheria.vhp.Model.Model_register;
import katheria.vhp.R;
import katheria.vhp.SpecialChecking;

public class NewUserInputActivity extends AppCompatActivity {

    Context context = NewUserInputActivity.this;
    Spinner Spinner_state,Spinner_district,Spinner_designation,Spinner_designation2;
    Model_register model_register = new Model_register();
    private ArrayList<String> cityList = new ArrayList<>();

    String[] designation = new String[]{"--Padh--","Prant","Vibhag","Jila","Prakhand","Gram"};
    String[] designation2 = new String[]{"--Padh 2--","Adhyaksh","Upadhyaksh","Mantri","Sah Mantri","Koshadhx","Sanyojk Bajrang Dal","Sah Sanyojak Bajrang Dal","Sanyojika Matra shakti",
            "Sah Sanyojika MatraShakti","Sanyojika Durga Vahini","Sah Sayojika Durga Vahini","Satsang Pramukh","Samrasta Pramukh","Dhamachary Samprkh Pramukh","Dharm Prasar Pramkh",
            "Gau Raxa Pramukh","Seva Pramukh","Matha Mandir Pramukha","Pujak Archak Pramukh"};
    String[] states = new String[]{"--Select State--", "Andaman and Nicobar", "Andhra Pradesh", "Arunachal Pradesh",
            "Assam", "Bihar", "Chandigarh","Delhi","Gujarat","Haryana","Himachal Pradesh","Jammu and Kashmir","Jharkhand","Karnataka","Kerala",
            "Madhya Pradesh","Maharashtra","Manipur","Meghalaya","Mizoram","Nagaland","Orissa","Pondicherry","Punjab","Rajasthan","Tamil Nadu",
            "Tripura","Uttar Pradesh","Uttaranchal","West Bengal"};
    Button next;
    EditText name, phone, address,block;
    boolean updateded =true;
    String selected_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_input);
        Spinner_state = (Spinner) findViewById(R.id.spinner_state);
        Spinner_district = (Spinner) findViewById(R.id.spinner_district);
        Spinner_designation = (Spinner) findViewById(R.id.spinner_designation);
        Spinner_designation2 = (Spinner) findViewById(R.id.spinner_designation2);
        setUpToolBar();
        ArrayAdapter<String> adapter_state = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, states);
        final ArrayAdapter<String> adapter_designation = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, designation);
        ArrayAdapter<String> adapter_designation2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, designation2);
        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_designation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_designation2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_designation.setAdapter(adapter_designation);
        Spinner_designation2.setAdapter(adapter_designation2);
        Spinner_state.setAdapter(adapter_state);
        Spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_state=Spinner_state.getSelectedItem().toString();
                setCitySpin();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        name = (EditText)findViewById(R.id.name);
        phone = (EditText)findViewById(R.id.phone);
        address= (EditText) findViewById(R.id.address);
        block= (EditText) findViewById(R.id.block);
        next = (Button)findViewById(R.id.next);
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                new HttpCall().checkUniquePhone(context, phone.getText().toString(), phone, next);
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateded) {
                    if (!name.getText().toString().equals("") &&
                            Spinner_designation.getSelectedItemPosition() != 0 &&
                            Spinner_designation2.getSelectedItemPosition() != 0 &&
                            Spinner_district.getSelectedItemPosition() != 0 &&
                            Spinner_state.getSelectedItemPosition() != 0 &&
                            !address.getText().toString().equals("") &&
                            !block.getText().toString().equals("") &&
                            phone.getError() == null &&
                            !phone.getText().toString().equals("") && phone.getText().toString().length() == 10) {

                        if (new SpecialChecking().specialChecking(context, name))

                        {
                            model_register.name = name.getText().toString();
                            model_register.phone = phone.getText().toString();
                            model_register.designation = Spinner_designation.getSelectedItem().toString();
                            model_register.designation2 = Spinner_designation2.getSelectedItem().toString();
                            model_register.address = address.getText().toString();
                            model_register.block = block.getText().toString();
                            model_register.district = Spinner_district.getSelectedItem().toString();
                            model_register.state = Spinner_state.getSelectedItem().toString();


                            new HttpCall().inputnewuser(context,model_register);
                            /*HomeFragment fragment = new HomeFragment();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container,fragment);
                            fragmentTransaction.commit();*/



                        } else {
                            Snackbar.make(v, "Please enter details correctly", Snackbar.LENGTH_LONG).show();
                        }
                    }  else {
                        Snackbar.make(v, "Please enter details correctly", Snackbar.LENGTH_LONG).show();
                    }
                }else
                {
                    Toast.makeText(context,"Unable to register",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setCitySpin() {
        Spinner_district.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,cityList));

        new HttpCall().getDistrictList(context,selected_state,Spinner_district);
    }


    private void setUpToolBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("New User Input");
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
