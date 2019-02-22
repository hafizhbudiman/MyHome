package com.kecepret.myhome;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
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

import com.kecepret.myhome.model.Result;
import com.kecepret.myhome.model.TokenResponse;
import com.kecepret.myhome.network.APIClient;
import com.kecepret.myhome.network.APIInterface;

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

    APIInterface apiInterface;

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

        terraceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    apiInterface = APIClient.getClient().create(APIInterface.class);
                    Call<TokenResponse> call = apiInterface.doGetTokens();
                    call.enqueue(new Callback<TokenResponse>() {
                        @Override
                        public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {

                            String displayResponse = "";

                            TokenResponse resource = response.body();
                            Integer count = resource.count;
                            List<Result> resultList = resource.results;

                            displayResponse +=  count + " Total\n";

//                            for (Result result : resultList) {
//                                displayResponse += result.id + " " + result.code + " " + result.balance + " " + result.used + "\n";
//                            }

                            String message;

                            message = "ASU" + displayResponse;
                            Toast.makeText(getActivity(), message,
                                    Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<TokenResponse> call, Throwable t) {
                            String message;
                            message = "ASU";
                            Toast.makeText(getActivity(), message,
                                    Toast.LENGTH_LONG).show();
                            call.cancel();
                        }
                    });

//                    message = "Terrace lamp turned on";
                } else {
                    String message;
                    message = "Terrace lamp turned off";
                    Toast.makeText(getActivity(), message,
                            Toast.LENGTH_LONG).show();
                }
//                Toast.makeText(getActivity(), message,
//                        Toast.LENGTH_LONG).show();
            }

        });

        livingRoomSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String message;
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
}
