package com.wiley.fordummies.androidsdk.tictactoe;

import java.util.List;

import android.app.Activity;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

public class WhereAmI extends Activity implements OnClickListener,
		LocationListener {
	private EditText mEditLocation;
	private GoogleMap mMap;
	private MapView mMapView;
	private String mProvider = null;
	private Button mButtonFindme;
	private String whereAmIString = null;
	private Button mButtonLocate;
	private static final String WHEREAMISTRING = "WhereAmIString";
	private static final String TAG = "WhereAmI";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whereami);

		mMapView = (MapView) findViewById(R.id.mapview);
		if (mMapView != null) {
			mMapView.onCreate(savedInstanceState);
			mMapView.onResume();

			mMap = mMapView.getMap();
			mMap.getUiSettings().setMyLocationButtonEnabled(true);
			mMap.setMyLocationEnabled(true);
		}

		mEditLocation = (EditText) findViewById(R.id.location);

		mButtonLocate = (Button) findViewById(R.id.button_locate);
		mButtonLocate.setOnClickListener(this);

		mButtonFindme = (Button) findViewById(R.id.button_findme);
		mButtonFindme.setOnClickListener(this);
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (whereAmIString != null)
			outState.putString(WHEREAMISTRING, whereAmIString);
	}

	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		whereAmIString = savedInstanceState.getString(WHEREAMISTRING);
		if (whereAmIString != null)
			mEditLocation.setText(whereAmIString);
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, "Location changed to: " + location.getLatitude() + ", "
				+ location.getLongitude());
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_locate:
			try {
				// Need to call MapsInitializer before doing any
				// CameraUpdateFactory calls
				MapsInitializer.initialize(this);

				String locationName = mEditLocation.getText().toString();
				Geocoder gc = new Geocoder(this);
				List<Address> addresses = gc.getFromLocationName(locationName,
						1);
				if (addresses.isEmpty()) {
					Toast.makeText(getApplicationContext(),
							"No location found matching this address",
							Toast.LENGTH_LONG).show();
				} else {
					LatLng latlng = new LatLng(addresses.get(0).getLatitude(),
							addresses.get(0).getLongitude());
					mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
							latlng, 8));
				}
			} catch (Exception e) {
				// do nothing
				e.printStackTrace();
			}
			break;
		case R.id.button_findme:
			LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			mProvider = service.getBestProvider(criteria, true);
			service.requestLocationUpdates(mProvider, 15000, 1,
					(LocationListener) this);

			// get the location from the provider
			Location location = service.getLastKnownLocation(mProvider);
			if (location == null) {
				// if it's not available, get the location from the network
				location = service
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
            //for test
//			try {
//				MapsInitializer.initialize(WhereAmI.this);
//			} catch (GooglePlayServicesNotAvailableException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			LatLng latlng = new LatLng(location.getLatitude(),
					location.getLongitude());
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 8));
			break;
		}
	}
}
