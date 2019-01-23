package example.com.hvacawards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class compliantDetail extends AppCompatActivity {

    String fullname,emailid,mobno,comp,compdetaills,test;
    TextView nameTV,emailTV,mobTV,compTV,detailTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compliantdetail);

        fullname = getIntent().getExtras().getString("Name");
        mobno = getIntent().getExtras().getString("Mobile");
        emailid = getIntent().getExtras().getString("email");
        comp = getIntent().getExtras().getString("complliant");
        compdetaills = getIntent().getExtras().getString("details");

        test = getIntent().getExtras().getString("isComp");
        if (test.equals("0")){
            View vttt = findViewById(R.id.viewit);
            vttt.setVisibility(View.GONE);
            ImageView imgi = findViewById(R.id.imageview);
            imgi.setVisibility(View.GONE);
        }

        nameTV = findViewById(R.id.name);
        nameTV.setText(fullname);
        emailTV = findViewById(R.id.Email);
        emailTV.setText(emailid);
        mobTV = findViewById(R.id.mobile);
        mobTV.setText(mobno);
        compTV = findViewById(R.id.compliant);
        compTV.setText(comp);
        detailTV = findViewById(R.id.compdetails);
        detailTV.setText(compdetaills);
    }
}
