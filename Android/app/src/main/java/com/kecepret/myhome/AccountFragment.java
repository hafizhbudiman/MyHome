package com.kecepret.myhome;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.kecepret.myhome.model.UserSession;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private View rootView;

    private GoogleSignInClient mGoogleSignInClient;

    private String fullName;
    private String userName;
    private String email;
    private String phoneNumber;
    private String address;

    private TextView tvFullName;
    private TextView tvUserName;
    private TextView tvEmail;
    private TextView tvPhoneNumber;
    private TextView tvAddress;

    UserSession session;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_account, container, false);

        // User Session Manager
        session = new UserSession(rootView.getContext());

        // Update fragment text accordingly
        fullName = session.getFullName();
        userName = session.getUsername();
        email = session.getEmail();
        phoneNumber = session.getPhoneNumber();
        address = session.getAddress();

        tvFullName = rootView.findViewById(R.id.name);
        tvUserName = rootView.findViewById(R.id.username);
        tvEmail = rootView.findViewById(R.id.email);
        tvPhoneNumber = rootView.findViewById(R.id.phone);
        tvAddress = rootView.findViewById(R.id.address);

        tvFullName.setText(fullName);
        tvUserName.setText(userName);
        tvEmail.setText(email);
        tvPhoneNumber.setText(phoneNumber);
        tvAddress.setText(address);

        Button logOutButton = (Button) rootView.findViewById(R.id.logout);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                signOut();

                Intent i = new  Intent(v.getContext(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        return rootView;
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    @Override
    public void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        super.onStart();
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
