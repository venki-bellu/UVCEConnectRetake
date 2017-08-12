package com.venkibellu.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ViewUsers extends AppCompatActivity {

    private class User {
        private String key, name, email, phone, status, yearOfJoining, branch;

        User(String name, String key) {
            this.name = name;
            this.key = key;
        }

        void setCompleteInformation(String email, String branch, String phone,
                                    String status, String year) {
            this.email = email;
            this.branch = branch;
            this.phone = phone;
            this.status = status;
            this.yearOfJoining = year;
        }

        String getKey() {
            return key;
        }

        String getName() {
            return name;
        }

        String getEmail() {
            return email;
        }

        String getPhone() {
            return phone;
        }

        String getStatus() {
            return status;
        }

        String getYearOfJoining() {
            return yearOfJoining;
        }

        String getBranch() {
            return branch;
        }
    }

    ArrayAdapter<User> adapter;
    ArrayList<User> users;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    LinearLayout progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        progress = (LinearLayout) findViewById(R.id.progressLayout);
        progress.setVisibility(View.VISIBLE);

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("Registered Users");

        users = new ArrayList<>();
        adapter = new UserListAdapter();

        populateUserList();
        populateListView();
    }

    private void populateListView() {
        ListView userListView = (ListView) findViewById(R.id.view_users_list_view);
        userListView.setAdapter(adapter);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User selectedUser = users.get(i);
                getUserInformation(selectedUser);
            }
        });
    }

    private void getUserInformation(final User selectedUser) {
        reference.addValueEventListener(new ValueEventListener() {
            String key = selectedUser.getKey();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String branch, email, phone, status, year;
                email = dataSnapshot.child(key).child("Email Id").getValue().toString();
                branch = dataSnapshot.child(key).child("Branch").getValue().toString();
                phone = dataSnapshot.child(key).child("Contact Number").getValue().toString();
                status = dataSnapshot.child(key).child("User Type").getValue().toString();
                year = dataSnapshot.child(key).child("Year Of Joining").getValue().toString();

                selectedUser.setCompleteInformation(email, branch, phone, status, year);
                showUserInformation(selectedUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showUserInformation(User user) {
        final AlertDialog.Builder userInfoDialog = new AlertDialog.Builder(ViewUsers.this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View view = inflater.inflate(R.layout.user_dialog, null);
        TextView emailTextView = (TextView) view.findViewById(R.id.dialog_email_field);
        TextView phoneTextView = (TextView) view.findViewById(R.id.dialog_phone_field);
        TextView branchTextView = (TextView) view.findViewById(R.id.dialog_branch_field);
        TextView statusTextView = (TextView) view.findViewById(R.id.dialog_status_field);
        TextView yearTextView = (TextView) view.findViewById(R.id.dialog_year_field);

        String email = "\u2022  " + user.getEmail();
        String phone = "\u2022  " + user.getPhone();
        String branch = "\u2022  " + user.getBranch();
        String status = "\u2022  " + user.getStatus();
        String year = "\u2022  " + user.getYearOfJoining();

        emailTextView.setText(email);
        phoneTextView.setText(phone);
        branchTextView.setText(branch);
        statusTextView.setText(status);
        yearTextView.setText(year);

        userInfoDialog.setView(view)
                .setTitle(user.getName())
                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setIcon(R.drawable.user_logo)
                .setCancelable(false)
                .show();
    }

    private class UserListAdapter extends ArrayAdapter<User> {

        public UserListAdapter() {
            super(ViewUsers.this, R.layout.list_users_row, users);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.list_users_row, parent, false);
            }

            User currentUser = users.get(position);
            TextView userNameTextView = (TextView) itemView.findViewById(R.id.user_name_text);

            userNameTextView.setText(currentUser.getName());

            return itemView;
        }
    }

    private void populateUserList() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String name = snapshot.child("Name").getValue().toString();
                        String key = snapshot.getKey();

                        users.add(new User(name, key));
                    }
                } catch (Exception e) {
                }

                formatNames();
                progress.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void formatNames() {
        for (int i = 0; i < users.size(); ++i) {
            String name = users.get(i).getName(), capitalizedName = "", key = users.get(i).getKey();
            capitalizedName = WordUtils.capitalizeFully(name);

            users.set(i, new User(capitalizedName, key));
        }

        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User user, User t1) {
                return user.getName().compareTo(t1.getName());
            }
        });
    }
}
