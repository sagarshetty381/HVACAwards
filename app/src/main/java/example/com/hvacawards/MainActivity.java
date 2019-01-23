package example.com.hvacawards;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IllegalFormatCodePointException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseCompliant,mDatabaseInstallation,mDatabaseService,mDatabaseisThere;
    String uid,compexist="";

    int num = 0;

    EditText experienceET,detailsET,contact;
    TextInputLayout textInputLayout,detaillayout;
    CheckBox chk1,chk2,chk3,chk4,chk5,chk6,chk7,chk8,chk9,chk10,chk11,chk12,chk13,chk14,chk15;
    Button save;
    List<String> compliants;
    List<String> services,installations;
    String emailId,promo,refpromo,fullname,address,mobile,valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        emailId = user.getEmail();

        contact = findViewById(R.id.contactno);
        detaillayout = findViewById(R.id.compvisi);
        textInputLayout = findViewById(R.id.textinput);

        valid = getIntent().getExtras().getString("isGoogle");
        if (valid.equals("0")){
            textInputLayout.setVisibility(View.GONE);
            fullname = getIntent().getExtras().getString("Name");
            mobile = getIntent().getExtras().getString("mobileNo");
        }else if (valid.equals("1")){
            textInputLayout.setVisibility(View.VISIBLE);
            fullname = user.getDisplayName();
        }

        mDatabaseService = FirebaseDatabase.getInstance().getReference().child("Services");
        mDatabaseInstallation = FirebaseDatabase.getInstance().getReference().child("Installations");
        mDatabaseCompliant = FirebaseDatabase.getInstance().getReference().child("Compliants");

        chk1 = findViewById(R.id.service1);
        chk2 = findViewById(R.id.installation1);
        chk3 = findViewById(R.id.compliant1);
        chk4 = findViewById(R.id.service2);
        chk5 = findViewById(R.id.installation2);
        chk6 = findViewById(R.id.compliant2);
        chk7 = findViewById(R.id.service3);
        chk8 = findViewById(R.id.installation3);
        chk9 = findViewById(R.id.compliant3);
        chk10 = findViewById(R.id.service4);
        chk11 = findViewById(R.id.installation4);
        chk12 = findViewById(R.id.compliant4);
        chk13 = findViewById(R.id.service5);
        chk14 = findViewById(R.id.installation5);
        chk15 = findViewById(R.id.compliant5);

        experienceET = findViewById(R.id.experienceet);
        detailsET = findViewById(R.id.details);

        save = findViewById(R.id.savebt);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                services = new ArrayList<String>();
                installations = new ArrayList<String>();
                compliants = new ArrayList<String>();

                if (chk1.isChecked()){
                    services.add("Windows");
                }if (chk4.isChecked()){
                    services.add("Split");
                }if (chk7.isChecked()){
                    services.add("Ductable");
                }if (chk10.isChecked()){
                    services.add("VRV/VRF");
                }if (chk13.isChecked()){
                    services.add("Chiller");
                }
                if (chk2.isChecked()){
                    installations.add("Windows");
                }if (chk5.isChecked()){
                    installations.add("Split");
                }if (chk8.isChecked()){
                    installations.add("Ductable");
                }if (chk11.isChecked()){
                    installations.add("VRV/VRF");
                }if (chk14.isChecked()){
                    installations.add("Chiller");
                }
                if (chk3.isChecked()){
                    compliants.add("Windows");
                    compexist = compexist + "Windows,";
                }if (chk6.isChecked()){
                    compliants.add("Split");
                    compexist = compexist + "Split,";
                }if (chk9.isChecked()){
                    compliants.add("Ductable");
                    compexist = compexist + "Ductable,";
                }if (chk12.isChecked()){
                    compliants.add("VRV/VRF");
                    compexist = compexist + "VRV/VRF,";
                }if (chk15.isChecked()){
                    compliants.add("Chiller");
                    compexist = compexist + "Chiller";
                }

                String Service = services.toString().replace("[", "").replace("]", "")
                        .replace(", ", ",");
                String Installation = installations.toString().replace("[", "").replace("]", "")
                        .replace(", ", ",");
                String Compliant = compliants.toString().replace("[", "").replace("]", "")
                        .replace(", ", ",");

                String experience = experienceET.getText().toString().trim();
                String detail = detailsET.getText().toString().trim();

                if (valid.equals("1")){
                    mobile = contact.getText().toString().trim();
                }

                if ((!compexist.equals(""))&&(detail.equals(""))) {
                    detailsET.setError("Please write your compliants about " + compexist);
                }else if (!(mobile.length()==10)){
                    contact.setError("Mobile number should be 10 digits");
                }
                else{
                    HashMap<String, String> datamap = new HashMap<String, String>();
                    datamap.put("Experience", experience);
                    datamap.put("Userid", uid);
                    datamap.put("Fullname", fullname);
                    datamap.put("EmailID", emailId);
                    datamap.put("MobileNo", mobile);
                    datamap.put("Service", Service);
                    mDatabaseService.push().setValue(datamap);
                    datamap.remove("Service");
                    datamap.put("Installation", Installation);
                    mDatabaseInstallation.push().setValue(datamap);
                    datamap.remove("Installation");
                    datamap.put("Compliant", Compliant);
                    datamap.put("CompDetails", detail);
                    mDatabaseCompliant.push().setValue(datamap);

                    startActivity(new Intent(MainActivity.this, MyProfile.class));
                    finish();
                }
            }
        });
    }
}