package example.com.hvacawards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProjectDetails extends AppCompatActivity {

    Adapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager Manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        String Name = getIntent().getExtras().getString("Name");
        String Address = getIntent().getExtras().getString("Address");
        String Area = getIntent().getExtras().getString("Area");
        String City = getIntent().getExtras().getString("City");
        String State = getIntent().getExtras().getString("State");
        String TypeHVAC = getIntent().getExtras().getString("HVAC");
        final String latti = getIntent().getExtras().getString("Latti");
        final String longi = getIntent().getExtras().getString("Longi");
        String capa = getIntent().getExtras().getString("Capacity");
        String rate = getIntent().getExtras().getString("Rating");
        ArrayList<String> newList = (ArrayList<String>) getIntent().getSerializableExtra("urls");

        ImageView mapy = findViewById(R.id.direct);
        mapy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProjectDetails.this,MapsActivity.class).putExtra("Lat",latti)
                        .putExtra("long",longi));
            }
        });

        this.recyclerView = findViewById(R.id.recyclerView12);
        Manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(Manager);
        adapter = new Adapter(newList, this);
        recyclerView.setAdapter(adapter);

        String title = Name;
        TextView titlle = findViewById(R.id.projecttitle);
        titlle.setText(title);

        String full = Address + "," + Area + "," + City + "," + State;
        TextView fullAddress = findViewById(R.id.fulladdress);
        fullAddress.setText(full);

        TextView type = findViewById(R.id.typeOf);
        type.setText(TypeHVAC);

        TextView cap = findViewById(R.id.capacityTR);
        cap.setText(capa);

        TextView lat = findViewById(R.id.lattitudePro);
        lat.setText(latti);

        TextView lon = findViewById(R.id.longitudePro);
        lon.setText(longi);

        TextView rat = findViewById(R.id.ratingPro);
        rat.setText(rate);
   }
}
