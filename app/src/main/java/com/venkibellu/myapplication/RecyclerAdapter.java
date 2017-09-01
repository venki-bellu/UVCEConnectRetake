package com.venkibellu.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

@SuppressWarnings("FieldCanBeLocal")
class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.UserInfoHolder> {
    private static boolean justClicked;
    private final String NAME = "name", PHONE = "phone";
    private ArrayList<User> userList, permanentUserList;

    RecyclerAdapter(ArrayList<User> userList, ArrayList<User> permanentUserList) {
        this.userList = userList;
        this.permanentUserList = permanentUserList;
        justClicked = false;
    }

    @Override
    public RecyclerAdapter.UserInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_users_row, parent, false);

        return new UserInfoHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.UserInfoHolder holder, int position) {
        User user = userList.get(position);
        holder.bindUserInfo(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserInfoHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private TextView mTextView;
        private User selectedUser;

        UserInfoHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.user_name_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (justClicked) {
                return;
            }

            justClicked = true;
            getUserInformation(selectedUser, view.getContext());
        }

        void bindUserInfo(User user) {
            mTextView.setText(user.getName());
            selectedUser = user;
        }
    }

    // filter the search result based on the search query.
    void filter(String charSequence, String searchParam) {
        // tempUserList: stores the filtered result temporarily.
        ArrayList<User> tempUserList = new ArrayList<>();

        // if there is a query and usersList is not empty, then perform filtering.
        if (charSequence != null && permanentUserList != null) {
            String query = charSequence.toLowerCase();

            boolean fullMatch = false;
            if (query.contains(" ")) {
                fullMatch = true;
            }

            for (int i = 0; i < permanentUserList.size(); ++i) {
                String userName = permanentUserList.get(i).getName().toLowerCase();
                String phone = permanentUserList.get(i).getPhone();

                switch (searchParam) {
                    case NAME:
                        if (userNameMatches(userName, query, fullMatch)) {
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

            userList.clear();
            if (tempUserList.size() > 0) {
                for (User user : tempUserList) {
                    userList.add(user);
                }
            }

            notifyDataSetChanged();
        }
    }

    private boolean userPhoneMatches(String userPhone, String query) {
        return userPhone.contains(query);
    }

    /*
          Search Algorithm:
          - Initially convert the userName and query text to lower case.

          - if text query contains any space then perform a full match, i.e, character by
            character matching.

          - else, tokenize the userName by splitting into words delimited by spaces.

              - match the query string with each token, looking for a match.

              - if successful return true.
     */
    private boolean userNameMatches(String userName, String query, boolean fullMatch) {
        if (fullMatch) {
            return userName.contains(query);
        }

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


    // get the complete user information based on the selected user in list.
    private static void getUserInformation(final User selectedUser, final Context context) {

        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference()
                .child("Registered Users");

        final String key = selectedUser.getKey();
        final Query query = reference.orderByKey().equalTo(key);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String branch, email, status, year;

                // store the data of selected user's key.
                DataSnapshot snapshot = dataSnapshot.child(key);

                // parse each child from the data.
                try {
                    email = snapshot.child("Email Id").getValue().toString();
                    branch = snapshot.child("Branch").getValue().toString();
                    status = snapshot.child("User Type").getValue().toString();
                    year = snapshot.child("Year Of Joining").getValue().toString();
                } catch (NullPointerException e) {
                    Toast.makeText(context, "Data corrupted, please refresh", Toast.LENGTH_SHORT).show();
                    return;
                }

                selectedUser.setCompleteInformation(email, branch, status, year);
                query.removeEventListener(this);
                showUserInformation(selectedUser, context);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // show the dialog box, containing user information.
    private static void showUserInformation(final User user, Context context) {
        final AlertDialog.Builder userInfoDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);

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

        try {
            userInfoDialog.setView(view)
                    .setTitle(user.getName())
                    .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            justClicked = false;
                            dialogInterface.dismiss();
                        }
                    })
                    .setIcon(R.drawable.user_logo)
                    .setCancelable(false)
                    .show();
        } catch (Exception e) {
            Toast.makeText(context, "Oops! Something went wrong",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
