package com.ztobit.firebaseotp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;


public class AskFrag extends Fragment {
    private static final String DEBUG_TAG = "NetworkStatusExample";
    View view;
    EditText content;
    Button post;
    String s_content, s_location = "AMD";
    String s_phone;
    Location l_location;
    SharedPreferences sp;
    double user_latitude, user_longitude;
    Location location;

    public AskFrag() {
        // Required empty public constructor
    }

    public static AskFrag newInstance(String param1, String param2) {
        AskFrag fragment = new AskFrag();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getActivity().getSharedPreferences(SP_Phone.PREFERENCE, MODE_PRIVATE);
        s_phone = sp.getString(SP_Phone.PHONE, "");
        access_permision();

    }

    private void access_permision() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        }
    }


    private void setlocation()
    {

            LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
            //Best location provider is decided by the criteria
            Criteria criteria = new Criteria();
            //location manager will take the best location from the criteria

            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!isNetworkEnabled && !isGPSEnabled)
            {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

            }
            else
            {
                locationManager.getBestProvider(criteria, true);
                //nce you know the name of the LocationProvider, you can call getLastKnownPosition() to find out where you were recently.
                @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));

                Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
                List<Address> addresses;

                try {
                    addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses.size() > 0)

                    {
                        //s_location = "ADMIN AREA__"+addresses.get(0).getAdminArea()+"__PREMISES__"+addresses.get(0).getPremises()+"__SUB ADMIN__"+addresses.get(0).getSubAdminArea();
                        s_location = addresses.get(0).getSubAdminArea();

                        //Show(s_location);
                    }

                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }


    }

    public void Show(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }

    private boolean validate() {
        getData();
        if (s_content.length() == 0) {
            content.setError("Fill first name");
            return  false;
        }
        else
        {
            return true;
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_ask, container, false);
        init();
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate())
                {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        setlocation();

                        //Toast.makeText(getActivity(), "Validated",Toast.LENGTH_LONG).show();
                        if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
                            new insertData().execute();
                        } else {
                            new ConnectionDetector(getActivity()).connectiondetect();
                        }
                    }
                    else
                    {
                        Show("Please Allow location access to post");
                        access_permision();
                    }
                }
            }
        });

        return view;
    }
    private void init() {
        content = (EditText) view.findViewById(R.id.content);
        post = (Button) view.findViewById(R.id.ask_post);
    }
    private void getData()
    {
        s_content=content.getText().toString().trim();
    }

    private class insertData extends AsyncTask<String,String,String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("phone", s_phone);
            hashMap.put("location", s_location);
            hashMap.put("content", s_content);
            //String ip=getResources().getString(R.string.ip);
            String url="https://succor.ztobit.com";
            return new MakeServiceCall().getPostData(url+"/ask_post.php", hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            try {
                JSONObject jsonObject = new JSONObject(s.toString());
                if (jsonObject.getString("Status").equalsIgnoreCase("True")) {
                    Toast.makeText(getActivity(), jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();

                    Fragment home_frag = new HomeFrag();
                    FragmentManager manager = getFragmentManager();
                    manager.beginTransaction().replace(R.id.container,home_frag).commit();
                    //ImageButton dash= view.findViewById(R.id.navigation_dashboard); set dashboard selected
                } else {
                    Toast.makeText(getActivity(), jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
