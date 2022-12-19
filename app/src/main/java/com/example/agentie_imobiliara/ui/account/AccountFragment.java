package com.example.agentie_imobiliara.ui.account;

import static com.example.agentie_imobiliara.model.User.encodePassword;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agentie_imobiliara.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class AccountFragment extends Fragment {

    FirebaseAuth authAction=FirebaseAuth.getInstance();

    public AccountFragment() {
        // Required empty public constructor
    }


    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        EditText new_pass1 = (EditText) view.findViewById(R.id.new_pass1);
        EditText new_pass2 = (EditText) view.findViewById(R.id.new_pass2);
        Button submit = (Button) view.findViewById(R.id.submitnewpass);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass_new1 = new_pass1.getText().toString();
                String pass_new2 = new_pass2.getText().toString();

                if(new_pass1.getText().toString().equals("") || new_pass2.getText().toString().equals(""))
                {
                    Toast.makeText(getContext(), "Please complete all fields!", Toast.LENGTH_SHORT).show();
                }
                else if(!Objects.equals(new_pass1.getText().toString(), new_pass2.getText().toString()))
                {
                    Toast.makeText(getContext(), "Passwords does not match!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    authAction.getCurrentUser().updatePassword(encodePassword(authAction.getCurrentUser().getEmail().toString(), pass_new1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(), "Passwords changed successfully!", Toast.LENGTH_SHORT).show();
                                authAction.signOut();
                                getActivity().finish();
                            }
                            else{
                                Toast.makeText(getContext(),""+ task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        return  view;
    }
}