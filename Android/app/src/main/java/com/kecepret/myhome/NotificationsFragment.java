package com.kecepret.myhome;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kecepret.myhome.adapter.NotificationAdapter;
import com.kecepret.myhome.model.Lamp;
import com.kecepret.myhome.model.Notification;
import com.kecepret.myhome.model.NotificationResponse;
import com.kecepret.myhome.model.ResponseBE;
import com.kecepret.myhome.model.UserSession;
import com.kecepret.myhome.network.APIClient;
import com.kecepret.myhome.network.APIInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View rootView;
    private RecyclerView recycler;
    private NotificationAdapter adapter;
    private TextView emptyView;
    private List<Notification> notificationList;
    private Context context;

    private String username;
    UserSession session;

    APIInterface apiInterface;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        notificationList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_notifications, container, false);

        // User Session Manager
        session = new UserSession(rootView.getContext());

        recycler = rootView.findViewById(R.id.notification_recycler);
        emptyView = rootView.findViewById(R.id.notification_empty_view);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recycler.setLayoutManager(linearLayoutManager);

        username = session.getUsername();
        getNotifications(username);
    }

    private List<Notification> getNotificationList() {
        notificationList = new ArrayList<>();
        notificationList.add(new Notification(-1,"0", 1));
        notificationList.add(new Notification(25000,"ABCDE", 2));
        notificationList.add(new Notification( -1,"0", 1));
        notificationList.add(new Notification(100000,"ABCDE", 2));
        return notificationList;
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

    public void getNotifications(String username){
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<NotificationResponse> call = apiInterface.get_notification(username);

        call.enqueue(new Callback<NotificationResponse>() {

            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                NotificationResponse resource = response.body();

                if (!resource.getResults().isEmpty()) {
                    notificationList = resource.getResults();
                    adapter = new NotificationAdapter(notificationList, context);
                    recycler.setAdapter(adapter);

                    emptyView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }
}
