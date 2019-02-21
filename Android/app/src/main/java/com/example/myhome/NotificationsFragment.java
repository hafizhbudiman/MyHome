package com.example.myhome;

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

import com.example.myhome.adapter.NotificationAdapter;
import com.example.myhome.model.Notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NotificationsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private View rootView;
    private RecyclerView recycler;
    private NotificationAdapter adapter;
    private TextView emptyView;
    private List<Notification> notificationList;
    private Context context;

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
        notificationList = getNotificationList();
        adapter = new NotificationAdapter(notificationList, context);
        recycler.setAdapter(adapter);

        emptyView.setVisibility(View.GONE);
    }

    private List<Notification> getNotificationList() {
        notificationList = new ArrayList<>();
        notificationList.add(new Notification(-1,"0"));
        notificationList.add(new Notification(25000,"ABCDE"));
        notificationList.add(new Notification(-1,"0"));
        notificationList.add(new Notification(100000,"ABCDE"));
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
}
