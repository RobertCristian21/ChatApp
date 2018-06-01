package com.example.user.chatapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.user.chatapp.Activities.LoginActivity;
import com.example.user.chatapp.Adapters.CustomUserAdapter;
import com.example.user.chatapp.R;
import com.example.user.chatapp.Contact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllUsersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllUsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllUsersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DatabaseReference dref= FirebaseDatabase.getInstance().getReference();
     FirebaseAuth mAuth=FirebaseAuth.getInstance();
     ArrayList<String> Users=new ArrayList<>();
    Button SingOut;
    ListView listView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AllUsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllUsersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllUsersFragment newInstance(String param1, String param2) {
        AllUsersFragment fragment = new AllUsersFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_all_users,container,false);

        listView=view.findViewById(R.id.list_view_allusers);

        SingOut =  view.findViewById(R.id.button_signout);

        FirebaseUser user=mAuth.getCurrentUser();
        if(user==null){
            startActivity(new Intent(getContext(),LoginActivity.class));
        }
        if(SingOut !=null) {
            SingOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAuth.signOut();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            });
        }

        return view;
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

    @Override
    public void onStart() {
        super.onStart();
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users.clear();
                if(mAuth!=null&&listView!=null) {
                    Users.clear();
                    for (DataSnapshot userSnapshot : dataSnapshot.child("Users").getChildren()) {
                        String user=userSnapshot.getValue(String.class);

                        if (!user.equals(mAuth.getCurrentUser().getEmail()))
                                Users.add(user);
                    }
                    final CustomUserAdapter adapterUsers = new CustomUserAdapter(getContext(),Users);
                    listView.setAdapter(adapterUsers);
                }
        }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

