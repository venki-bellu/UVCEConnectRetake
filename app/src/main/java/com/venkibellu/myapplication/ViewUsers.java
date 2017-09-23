package com.venkibellu.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.baoyz.widget.PullRefreshLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
    ViewUsers class to sync information about each user and
     - provide options to search by name or phone.
     - provide information about each user: name, branch, year of joining, email
       contact number, status
     - to sort
 */
public class ViewUsers extends AppCompatActivity {
    private RecyclerAdapter mAdapter;
    private ArrayList<User> userList, permanentUserList;
    private DatabaseReference reference;
    private AVLoadingIndicatorView avi;
    private PullRefreshLayout refreshLayout;
    android.support.v7.widget.SearchView searchView;

    // Required to toggle the sort and search parameters.
    private final String NAME = "name", PHONE = "phone";
    private final String ALPHABETIC = "AZ", KEY = "key";

    private String searchParam = NAME;
    private String sortToggleState = KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.show();

        reference = FirebaseDatabase.getInstance().getReference().child("Registered Users");

        userList = new ArrayList<>();
        permanentUserList = new ArrayList<>();

        mAdapter = new RecyclerAdapter(userList, permanentUserList);

        // sync the userList with Firebase.
        populateUserList();
        populateListView();

        refreshLayout = (PullRefreshLayout) findViewById(R.id.view_users_refresh);
        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // perform sync again.
                populateUserList();
            }
        });
    }

    /*
        sync data with Firebase, store the user name, phone, key in userList
        after formatting them.
     */
    private void populateUserList() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                permanentUserList.clear();

                // sync the name, contact number and key of each user.
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("Name").getValue().toString();
                    String phone = snapshot.child("Contact Number").getValue().toString();
                    String key = snapshot.getKey();

                    userList.add(new User(name, key, phone));
                }

                // Capitalize names and sort.
                formatNames();

                // Permanent list required for real time update on searching.
                for (User user : userList) {
                    permanentUserList.add(user);
                }

                mAdapter.notifyDataSetChanged();

                avi.hide();
                refreshLayout.setRefreshing(false);
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void hideKeyboard() {

        if (searchView != null && searchView.hasFocus()) {
            searchView.clearFocus();
        }

        View view = this.getCurrentFocus();

        // if view is null then keyboard was not open.
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // capitalise each name and sort the user list.
    private void formatNames() {
        for (int i = 0; i < userList.size(); ++i) {
            String name = userList.get(i).getName(),
                    capitalizedName = WordUtils.capitalizeFully(name),
                    key = userList.get(i).getKey(),
                    phone = userList.get(i).getPhone();

            userList.set(i, new User(capitalizedName, key, phone));
        }

        // sort by the previous toggle state.
        if (sortToggleState.equals(KEY)) {
            sort(ALPHABETIC);
        } else {
            sort(KEY);
        }
    }

    // sort based on the parameter, ALPHABETIC or KEY
    private void sort(final String sortParam) {
        hideKeyboard();

        switch (sortParam) {
            case ALPHABETIC:
                Collections.sort(userList, new Comparator<User>() {
                    @Override
                    public int compare(User user, User t1) {
                        return user.getName().compareTo(t1.getName());
                    }
                });
                break;

            case KEY:
                Collections.sort(userList, new Comparator<User>() {
                    @Override
                    public int compare(User user, User t1) {
                        int key1 = Integer.parseInt(user.getKey());
                        int key2 = Integer.parseInt(t1.getKey());

                        return key1 - key2;
                    }
                });
                break;
        }

        mAdapter.notifyDataSetChanged();
    }

    // populates the list.
    private void populateListView() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.view_user_recyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.menu_search_item);

        searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(item);

        searchView.setQueryHint("Search by " + searchParam);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                hideKeyboard();
                mAdapter.filter(query, searchParam);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.filter(newText, searchParam);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search_name:
                item.setChecked(true);

                searchParam = NAME;
                searchView.setInputType(InputType.TYPE_CLASS_TEXT);
                searchView.setQueryHint("Search by " + NAME);
                break;

            case R.id.menu_search_phone:
                item.setChecked(true);

                searchParam = PHONE;
                searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
                searchView.setQueryHint("Search by " + PHONE);
                break;

            case R.id.menu_sort:
                sort(sortToggleState);

                if (sortToggleState.equals(ALPHABETIC)) {
                    item.setIcon(R.drawable.ic_menu_sort_by_size);
                    sortToggleState = KEY;
                } else {
                    item.setIcon(R.drawable.ic_menu_sort_alphabetically);
                    sortToggleState = ALPHABETIC;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
