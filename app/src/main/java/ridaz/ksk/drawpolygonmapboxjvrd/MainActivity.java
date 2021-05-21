package ridaz.ksk.drawpolygonmapboxjvrd;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, SeekBar.OnSeekBarChangeListener {

 GoogleMap gMap;
 CheckBox checkBox;
 SeekBar seek_red,seek_green,seek_blue;
 Button btDraw,btnClear;


 Polygon polygon = null;
 List<LatLng> latLngList = new ArrayList<>();
 List<Marker> markerList = new ArrayList<>();

 float alpha= (float) 0.2,
 red =0,
 green=0,
 blue=0;

 @Override protected void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
 setContentView(R.layout.activity_main);

 //Assign vars
 checkBox = findViewById(R.id.checkbox);
 seek_red = findViewById(R.id.seek_red);
 seek_green = findViewById(R.id.seek_green);
 seek_blue = findViewById(R.id.seek_blue);
 btDraw = findViewById(R.id.bt_draw);
 btnClear = findViewById(R.id.btn_clear);

 //init SpMapFrg
 SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
 .findFragmentById(R.id.map);
 mapFragment.getMapAsync(this);

 checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
 @RequiresApi(api = Build.VERSION_CODES.O)
 @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
 // Get CheckBox State
 if (b){
 if (polygon == null) return;
 //Fill Polygon Color
 //  polygon.setFillColor(Color.rgb(red,green,blue));

 polygon.setFillColor(Color.argb(alpha,red,green,blue));
 }
 else {
 //UnFill PolyGon Color if Unchecked
 polygon.setFillColor(Color.TRANSPARENT);
 }

 }
 });

 btDraw.setOnClickListener(new View.OnClickListener() {
 @RequiresApi(api = Build.VERSION_CODES.O)
 @Override public void onClick(View view) {
 //Draw PolyGon On Map
 if (polygon != null)polygon.remove();

 //Create PolygonOption

 PolygonOptions polygonOptions = new PolygonOptions().addAll(latLngList).clickable(true);
 polygon = gMap.addPolygon(polygonOptions);

 //Set Polygon Stroke Color
 polygon.setStrokeColor(Color.argb(1,red,green,blue));

 if (checkBox.isChecked())
 // Fill Polygon Color
 polygon.setFillColor(Color.argb(alpha,red,green,blue));

 }
 });

 btnClear.setOnClickListener(new View.OnClickListener() {
 @Override public void onClick(View view) {
 //Clear All
 if (polygon != null) polygon.remove();
 for (Marker marker : markerList)marker.remove();
 latLngList.clear();
 markerList.clear();
 checkBox.setChecked(false);
 seek_red.setProgress(0);
 seek_green.setProgress(0);
 seek_blue.setProgress(0);
 }
 });

 seek_red.setOnSeekBarChangeListener(this);
 seek_green.setOnSeekBarChangeListener(this);
 seek_blue.setOnSeekBarChangeListener(this);
 }

 @Override public void onMapReady(@NonNull GoogleMap googleMap) {

 gMap = googleMap;

 gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

 gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
 @Override public void onMapClick(@NonNull LatLng latLng) {

 //Create Marker Options

 MarkerOptions markerOptions = new MarkerOptions().position(latLng);
 //Create LangLat and Marker
 Marker marker = gMap.addMarker(markerOptions);

 //Add latlng And marker
 latLngList.add(latLng);
 markerList.add(marker);


 }
 });

 }

 @RequiresApi(api = Build.VERSION_CODES.O)
 @Override public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

 switch (seekBar.getId()){

 case R.id.seek_red:
 red = i;
 break;

 case R.id.seek_green:
 green = i;
 break;

 case R.id.seek_blue:
 blue = i;
 break;
 }

 if (polygon != null) {
 //Set Polygon Stroke Color
 polygon.setStrokeColor(Color.argb(1,red, green, blue));

 if (checkBox.isChecked())
 // Fill Polygon Color
 polygon.setFillColor(Color.argb(alpha,red, green, blue));

 }
 }

 @Override public void onStartTrackingTouch(SeekBar seekBar) {

 }

 @Override public void onStopTrackingTouch(SeekBar seekBar) {

 }


}