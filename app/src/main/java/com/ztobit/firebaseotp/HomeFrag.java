package com.ztobit.firebaseotp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeFrag extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public HomeFrag() {
        // Required empty public constructor
    }

    public static HomeFrag newInstance(String param1, String param2) {
        HomeFrag fragment = new HomeFrag();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        //recyclerView.setHasFixedSize(true);
        // use a linear layout manager


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.home_frag, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView= getActivity().findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
            new insertData().execute();
        } else {
            new ConnectionDetector(getActivity()).connectiondetect();
        }
//        List<String> input = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            input.add("Test" + i);
//        }// define an adapter
//        mAdapter = new MyAdapter(input);
//        recyclerView.setAdapter(mAdapter);
    }
    private class insertData extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Getting Data...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url="https://succor.ztobit.com";

            return new MakeServiceCall().MakeServiceCall(url + "/dashboard_getdata.php", MakeServiceCall.GET);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("Status").equalsIgnoreCase("True")) {
//                    mainLists = new ArrayList<>();
                    JSONArray jsonArray = object.getJSONArray("response");
                    //Show(String.valueOf(jsonArray.length()));
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        // distance = new String[jsonArray.length()][2];
//
//                        MainList list = new MainList();
//                        list.setId(jsonObject.getString("id"));
//                        list.setName(jsonObject.getString("restaurant_name").toUpperCase());
//                        list.setCity("," + jsonObject.getString("city").toUpperCase()+"\n DIST  "+Float.valueOf(jsonObject.getString("distance"))+" KM");
//                        list.setArea(jsonObject.getString("address"));
//
//
//                        list.setType(type);
//                        list.setCost("₹" + jsonObject.getString("cost") + " for two (approx.)");
//                        list.setImage(jsonObject.getString("url"));
//                        //Show(jsonObject.getString("url"));
//                        mainLists.add(list);
//                    }

                    mAdapter = new MyAdapter(jsonArray);
                    recyclerView.setAdapter(mAdapter);
//                    adapter = new MainAdapter(getActivity(), mainLists, getFragmentManager());
//                    lv.setAdapter(adapter);
                } else {
                    Toast.makeText(getActivity(), object.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
