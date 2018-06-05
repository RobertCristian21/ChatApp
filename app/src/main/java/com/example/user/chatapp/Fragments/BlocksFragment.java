package com.example.user.chatapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.chatapp.Activities.LoginActivity;
import com.example.user.chatapp.Adapters.CustomBlockAdapter;
import com.example.user.chatapp.Adapters.CustomContactAdapter;
import com.example.user.chatapp.ContactOrBlock;
import com.example.user.chatapp.R;
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
 * {@link BlocksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlocksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlocksFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    DatabaseReference dref= FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    public ArrayList<String> BlockedUsers=new ArrayList<>();
    public ListView listView;

    public BlocksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlocksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlocksFragment newInstance(String param1, String param2) {
        BlocksFragment fragment = new BlocksFragment();
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
        final View view=inflater.inflate(R.layout.fragment_blocks,container,false);
        listView=view.findViewById(R.id.list_view_blocks);

        FirebaseUser user=mAuth.getCurrentUser();
        if(user==null){
            startActivity(new Intent(getContext(),LoginActivity.class));
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
                BlockedUsers.clear();
                if(mAuth!=null&&listView!=null) {
                    BlockedUsers.clear();
                    for (DataSnapshot userSnapshot : dataSnapshot.child("Blocks").getChildren()) {
                        ContactOrBlock contactOrBlock =userSnapshot.getValue(ContactOrBlock.class);

                        if (contactOrBlock.getUsername().equals(mAuth.getCurrentUser().getEmail()))
                            for(String e : contactOrBlock.getUsersList()) {
                                BlockedUsers.add(e);
                            }
                    }
                    if(BlockedUsers!=null&&getContext()!=null) {
                        final CustomBlockAdapter adapterUsers = new CustomBlockAdapter(getContext(), BlockedUsers);
                        listView.setAdapter(adapterUsers);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
