package com.kecepret.myhome;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.kecepret.myhome.model.Lamp;
import com.kecepret.myhome.model.ResponseBE;
import com.kecepret.myhome.model.Result;
import com.kecepret.myhome.model.TokenResponse;
import com.kecepret.myhome.model.User;
import com.kecepret.myhome.network.APIClient;
import com.kecepret.myhome.network.APIInterface;
import com.kecepret.myhome.network.ServiceGenerator;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Context context;
    private View rootView;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    private static final double LATITUDE_ITB = -6.8906583;
    private static final double LONGITUDE_ITB = 107.6093453;

    private Location mCurrentLocation;
    private boolean isInLocation;

    APIInterface apiInterface;

    private boolean isInLocation(Location location) {
        boolean isInLocation = false;

        double lat2 = location.getLatitude();
        double lng2 = location.getLongitude();

        // lat1 and lng1 are the values of a previously stored location
        if (distance(LATITUDE_ITB, LONGITUDE_ITB, lat2, lng2) < 0.1) { // if distance < 0.1 miles we take locations as equal
            isInLocation = true;
        }
        return isInLocation;
    }

    /** calculates the distance between two locations in MILES */
    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        return dist; // output distance, in MILES
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        Switch terraceSwitch = rootView.findViewById(R.id.switch1);
        Switch livingRoomSwitch = rootView.findViewById(R.id.switch2);
        Switch bedroomSwitch = rootView.findViewById(R.id.switch3);
        final Button frontDoorButton = rootView.findViewById(R.id.button_door1);

        mCurrentLocation = ((MainActivity)getActivity()).mCurrentLocation;
        isInLocation = isInLocation(mCurrentLocation);

        if (isInLocation) {
            Toast.makeText(getActivity(), "isInLocation", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getActivity(), "Not isInLocation", Toast.LENGTH_LONG).show();
        }

        terraceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                String message = "";
                TurnOnOffLamp("rwk", 1);
                if (isChecked) {
                    message = "Terrace lamp turned on";
                } else {
                    message = "Terrace lamp turned off";

                }
                Toast.makeText(getActivity(), message,
                        Toast.LENGTH_LONG).show();
            }

        });

        livingRoomSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String message;
                TurnOnOffLamp("rwk", 2);
                if (isChecked) {
                    message = "Living room lamp turned on";
                } else {
                    message = "Living room lamp turned off";
                }
                Toast.makeText(getActivity(), message,
                        Toast.LENGTH_LONG).show();
            }

        });

        bedroomSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String message;
                TurnOnOffLamp("rwk", 3);
                if (isChecked) {
                    message = "Bedroom lamp turned on";
                } else {
                    message = "Bedroom lamp turned off";
                }
                Toast.makeText(getActivity(), message,
                        Toast.LENGTH_LONG).show();
            }

        });

        // ShakeDetector initialization
        mSensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */

                String buttonString = frontDoorButton.getText().toString();
                String doorToast = "Error occured";

                if((buttonString.equals(getString(R.string.door_lock)))){
                    frontDoorButton.setText(getString(R.string.door_unlock));
                    doorToast = "Front door is unlocked";
                }else if((buttonString.equals(getString(R.string.door_unlock)))){
                    frontDoorButton.setText(getString(R.string.door_lock));
                    doorToast = "Front door is locked";
                }

                Toast.makeText(getActivity(), doorToast, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void TurnOnOffLamp(String username, int id){
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Lamp lamp = new Lamp(username, id);
        Call<ResponseBE> call = apiInterface.turnOnOff(lamp);

        call.enqueue(new Callback<ResponseBE>() {

            @Override
            public void onResponse(Call<ResponseBE> call, Response<ResponseBE> response) {
                ResponseBE resource = response.body();
                Boolean success = resource.success;
            }

            @Override
            public void onFailure(Call<ResponseBE> call, Throwable t) {
                call.cancel();
            }
        });
    }

}
