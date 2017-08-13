package com.venkibellu.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

        User(String name, String key, String phone) {
            this.name = name;
            this.key = key;
            this.phone = phone;
        }

        void setCompleteInformation(String email, String branch,
                                    String status, String year) {
            this.email = email;
            this.branch = branch;
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

    private ArrayAdapter<User> adapter;
    private ArrayList<User> userList, permanentUserList;
    private DatabaseReference reference;
    private LinearLayout progress;
    private final String NAME = "name", PHONE = "phone";
    private String searchParam = NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        progress = (LinearLayout) findViewById(R.id.progressLayout);
        progress.setVisibility(View.VISIBLE);

        reference = FirebaseDatabase.getInstance().getReference().child("Registered Users");

        userList = new ArrayList<>();
        permanentUserList = new ArrayList<>();
        adapter = new UserListAdapter();

        populateUserList();
        populateListView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();
                populateUserList();
            }
        });
    }

    private void populateUserList() {
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                permanentUserList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("Name").getValue().toString();
                    String phone = snapshot.child("Contact Number").getValue().toString();
                    String key = snapshot.getKey();

                    userList.add(new User(name, key, phone));
                }

                formatNames();

                for (User user : userList) {
                    permanentUserList.add(user);
                }

                adapter.notifyDataSetChanged();
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void formatNames() {
        for (int i = 0; i < userList.size(); ++i) {
            String name = userList.get(i).getName(),
                    capitalizedName = WordUtils.capitalizeFully(name),
                    key = userList.get(i).getKey(),
                    phone = userList.get(i).getPhone();

            userList.set(i, new User(capitalizedName, key, phone));
        }

        Collections.sort(userList, new Comparator<User>() {
            @Override
            public int compare(User user, User t1) {
                return user.getName().compareTo(t1.getName());
            }
        });
    }

    private void populateListView() {
        ListView userListView = (ListView) findViewById(R.id.view_users_list_view);
        userListView.setAdapter(adapter);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i < userList.size()) {
                    User selectedUser = userList.get(i);
                    getUserInformation(selectedUser);
                }
            }
        });
    }

    private class UserListAdapter extends ArrayAdapter<User> implements Filterable {

        UserListAdapter() {
            super(ViewUsers.this, R.layout.list_users_row, userList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.list_users_row, parent, false);
            }

            TextView userNameTextView = (TextView) itemView.findViewById(R.id.user_name_text);

            if (position < userList.size()) {
                User currentUser = userList.get(position);
                userNameTextView.setText(currentUser.getName());
            }

            return itemView;
        }

        Filter userFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                ArrayList<User> tempUserList = new ArrayList<>();

                if (charSequence != null && permanentUserList != null) {
                    for (int i = 0; i < permanentUserList.size(); ++i) {
                        String query = charSequence.toString().toLowerCase();
                        String userName = permanentUserList.get(i).getName().toLowerCase();
                        String phone = permanentUserList.get(i).getPhone();

                        switch (searchParam) {
                            case NAME:
                                if (userNameMatches(userName, query)) {
                                    tempUserList.add(permanentUserList.get(i));
                                }
                                break;

                            case PHONE:
                                if (userPhoneMatches(phone, query)) {
                                    tempUserList.add(permanentUserList.get(i));
                                }
                                break;
                        }
                    }

                    filterResults.values = tempUserList;
                    filterResults.count = tempUserList.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                ArrayList<User> results = (ArrayList<User>) filterResults.values;

                if (filterResults.count > 0) {
                    userList.clear();
                    for (User user : results) {
                        userList.add(user);
                    }
                    notifyDataSetChanged();
                } else {
                    userList.clear();
                    notifyDataSetChanged();
                }
            }
        };

        @NonNull
        @Override
        public Filter getFilter() {
            return userFilter;
        }
    }

    public boolean userPhoneMatches(String userPhone, String query) {
        return userPhone.contains(query);
    }

    public boolean userNameMatches(String userName, String query) {
        String[] nameTokens = userName.split(" ");

        for (String tokens : nameTokens) {
            int i = 0;

            while (i < tokens.length() && i < query.length()) {
                if (tokens.charAt(i) != query.charAt(i)) {
                    break;
                }

                ++i;
            }

            if (i == query.length()) {
                return true;
            }
        }

        return false;
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

                selectedUser.setCompleteInformation(email, branch, status, year);
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

        TextView emailTextView = (TextView) view.findViewById(R.id.dialog_email_field),
                phoneTextView = (TextView) view.findViewById(R.id.dialog_phone_field),
                branchTextView = (TextView) view.findViewById(R.id.dialog_branch_field),
                statusTextView = (TextView) view.findViewById(R.id.dialog_status_field),
                yearTextView = (TextView) view.findViewById(R.id.dialog_year_field),
                keyTextView = (TextView) view.findViewById(R.id.dialog_key_field);

        String email = "\u2022  " + user.getEmail(),
                phone = "\u2022  " + user.getPhone(),
                branch = "\u2022  " + user.getBranch(),
                status = "\u2022  " + user.getStatus(),
                year = "\u2022  " + user.getYearOfJoining(),
                key = "\u2022  Key : " + user.getKey();

        emailTextView.setText(email);
        phoneTextView.setText(phone);
        branchTextView.setText(branch);
        statusTextView.setText(status);
        yearTextView.setText(year);
        keyTextView.setText(key);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.menu_search_item);

        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView)
                MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search_name:
                Toast.makeText(getApplicationContext(), "Search by Name", Toast.LENGTH_SHORT).show();
                searchParam = NAME;
                break;

            case R.id.menu_search_phone:
                Toast.makeText(getApplicationContext(), "Search by Phone", Toast.LENGTH_SHORT).show();
                searchParam = PHONE;
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
