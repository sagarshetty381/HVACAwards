package example.com.hvacawards;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class UploadImages extends AppCompatActivity {

    CheckBox chk1, chk2, chk3, chk4, chk5, chk6, chk7, chk8, chk9, chk10, chk11;
    EditText mNameet, mAddresset, mAreaet, mCityet, mCapacityet;
    String uid;
    Integer prono = 0;
    Integer count = 0;
    Integer rate = 0;

    private Button mSelectImage;
    private ImageView img;
    private Button mSubmitBtn;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;
    TextView projno;
    Spinner citySpinner, stateSpinner;
    ArrayList<String> stringArrayState;
    ArrayList<String> stringArrayCity;
    String spinnerStateValue, city,spinnerCityValue;

    Uri tempUri = null;
    private static final int CAMERA_REQUEST_CODE = 1;
    private StorageReference mStorage;

    ArrayList<String> list = new ArrayList<String>();

    private GpsTracker gpsTracker;
    private TextView tvLatitude,tvLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        checkPermission();

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Projects").push();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        mSelectImage = findViewById(R.id.select_btn);
        mNameet = findViewById(R.id.nameet);
        mAddresset = findViewById(R.id.addresset);
        mAreaet = findViewById(R.id.areaet);
        tvLatitude = findViewById(R.id.latitude);
        tvLongitude = findViewById(R.id.longitude);
        mCapacityet = findViewById(R.id.capacityeet);
        mSubmitBtn = findViewById(R.id.addproject);
        img = findViewById(R.id.image);
        projno = findViewById(R.id.projnotv);

        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        int defaultValue = getPreferences(MODE_PRIVATE).getInt("count",prono);
        ++defaultValue;
        getPreferences(MODE_PRIVATE).edit().putInt("count",defaultValue).apply();
        prono = getPreferences(MODE_PRIVATE).getInt("count",prono);
        String saveit = prono.toString();
        projno.setText(saveit);

        if (count == 0){
            TextView text = findViewById(R.id.text);
            text.setVisibility(View.GONE);
        }

        init();

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        getLocation();

        chk1 = findViewById(R.id.ck1);
        chk2 = findViewById(R.id.ck2);
        chk3 = findViewById(R.id.ck3);
        chk4 = findViewById(R.id.ck4);
        chk5 = findViewById(R.id.ck5);
        chk6 = findViewById(R.id.ck6);
        chk7 = findViewById(R.id.ck7);
        chk8 = findViewById(R.id.ck8);
        chk9 = findViewById(R.id.ck9);
        chk10 = findViewById(R.id.ck10);
        chk11 = findViewById(R.id.ck11);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Uploading");
        mProgress.setMessage("Please wait..");
        mProgress.setCancelable(false);

        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent1, CAMERA_REQUEST_CODE);
                intent1.setType("image/*");
            }
        });
    }

    public void getLocation(){
        gpsTracker = new GpsTracker(UploadImages.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            tvLatitude.setText(String.valueOf(latitude));
            tvLongitude.setText(String.valueOf(longitude));
            Log.e("vcvh",longitude +  " " + latitude);
        }else{
            gpsTracker.showSettingsAlert();
        }
    }

    void init() {
        citySpinner = findViewById(R.id.citySpinner);
        stateSpinner = findViewById(R.id.stateSpinner);

        stringArrayState = new ArrayList<String>();
        stringArrayCity = new ArrayList<String>();

        final ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(this, R.layout.spinnertextview, stringArrayCity);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapterCity);

        try {
            JSONObject obj = new JSONObject(loadJSONFromAssetState());
            JSONArray m_jArry = obj.getJSONArray("statelist");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);

                String state = jo_inside.getString("State");
                String id = jo_inside.getString("id");

                stringArrayState.add(state);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinnertextview, stringArrayState);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(adapter);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                String Text = stateSpinner.getSelectedItem().toString();

                spinnerStateValue = String.valueOf(stateSpinner.getSelectedItem());
                stringArrayCity.clear();

                try {
                    JSONObject obj = new JSONObject(loadJSONFromAssetCity());
                    JSONArray m_jArry = obj.getJSONArray("citylist");

                    for (int i = 0; i < m_jArry.length(); i++) {
                        JSONObject jo_inside = m_jArry.getJSONObject(i);
                        String state = jo_inside.getString("State");
                        String cityid = jo_inside.getString("id");

                        if (spinnerStateValue.equalsIgnoreCase(state)) {
                            city = jo_inside.getString("city");
                            stringArrayCity.add(city);
                        }

                    }
                    adapterCity.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerCityValue = String.valueOf(citySpinner.getSelectedItem());
                Log.e("SpinnerCityValue", spinnerCityValue);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            mProgress.show();
            Bitmap mImageUri = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(mImageUri);

            tempUri = getImageUri(getApplicationContext(),mImageUri);

            Uri imageuri = data.getData();
            img.setImageURI(imageuri);

            if (count<11) {

                StorageReference filepath = mStorage.child("Images").child(tempUri.getLastPathSegment());
                filepath.putFile(tempUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ++count;
                        TextView Count = findViewById(R.id.text);
                        Count.setVisibility(View.VISIBLE);
                        Count.setText(count + "");

                        mProgress.cancel();

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        list.add(downloadUrl.toString());

                        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e("jjfh", count.toString());
                                ArrayList<String> hvaclist = new ArrayList<String>();
                                if (chk1.isChecked()) {
                                    hvaclist.add("Window");
                                }
                                if (chk2.isChecked()) {
                                    hvaclist.add("Cold Storage");
                                }
                                if (chk3.isChecked()) {
                                    hvaclist.add("Split");
                                }
                                if (chk4.isChecked()) {
                                    hvaclist.add("Ductable");
                                }
                                if (chk5.isChecked()) {
                                    hvaclist.add("Chiller");
                                }
                                if (chk6.isChecked()) {
                                    hvaclist.add("VRF");
                                }
                                if (chk7.isChecked()) {
                                    hvaclist.add("Ventilation");
                                }
                                if (chk8.isChecked()) {
                                    hvaclist.add("Humidification");
                                }
                                if (chk9.isChecked()) {
                                    hvaclist.add("Dehumidification");
                                }
                                if (chk10.isChecked()) {
                                    hvaclist.add("Clean Room");
                                }
                                if (chk11.isChecked()) {
                                    hvaclist.add("Filteration");
                                }
                                String fields = hvaclist.toString().replace("[", "").replace("]", "")
                                        .replace(", ", ",");

                                String name = mNameet.getText().toString().trim();
                                String address = mAddresset.getText().toString().trim();
                                String capacity = mCapacityet.getText().toString().trim();
                                String area = mAreaet.getText().toString().trim();
                                String latitude = tvLatitude.getText().toString().trim();
                                String longitude = tvLongitude.getText().toString().trim();
                                String rating = rate.toString();

                                HashMap<String, String> datamap = new HashMap<String, String>();
                                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address) || TextUtils.isEmpty(capacity)
                                        || TextUtils.isEmpty(area)||TextUtils.isEmpty(longitude)||TextUtils.isEmpty(latitude)) {
                                    Toast.makeText(UploadImages.this, "Some fields are empty!", Toast.LENGTH_SHORT).show();
                                } else {
                                    datamap.put("Name", name);
                                    datamap.put("Rating", rating);
                                    datamap.put("HVACfields", fields);
                                    datamap.put("PushId", mDatabase.getKey());
                                    datamap.put("CapacityTR", capacity);
                                    datamap.put("Address", address);
                                    datamap.put("Status", "pending");
                                    datamap.put("City", spinnerCityValue);
                                    datamap.put("State", spinnerStateValue);
                                    datamap.put("Area", area);
                                    datamap.put("Userid", uid);
                                    datamap.put("Lattitude", latitude);
                                    datamap.put("Longitude", longitude);

                                    for (int k = 0; k < list.size(); k++) {
                                        String img = "img" + k;
                                        datamap.put(img, list.get(k));
                                    }
                                    if (count > 1) {
                                        mDatabase.setValue(datamap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                DatabaseReference mProdatabase = FirebaseDatabase.getInstance().getReference().child("ProjectRate").child(mDatabase.getKey()).child("test");
                                                mProdatabase.setValue("start");
                                                startActivity(new Intent(UploadImages.this, MyProfile.class));
                                                finish();
                                                Toast.makeText(UploadImages.this, "Your project has been uploaded! :)", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(UploadImages.this, "Upload atleast 2 images!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                        Toast.makeText(UploadImages.this, "Image successfully uploaded! :)", Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                mProgress.cancel();
                Toast.makeText(UploadImages.this, "Coudn't updoad, maximum number reached!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private boolean checkPermission(){
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)
            !=PackageManager.PERMISSION_GRANTED){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},5);
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5){
            if (grantResults[0] == 5){
            }else{

            }
        }
    }

    public String loadJSONFromAssetState() {
        String json = null;
        try {
            InputStream is = getAssets().open("state.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public String loadJSONFromAssetCity() {
        String json = null;
        try {
            InputStream is = getAssets().open("cityState.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}