package co.mobilemakers.contactstudio2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContactListFragment extends ListFragment {

    public final static int REQUEST_NEW_CONTACT = 1;
    public final static int REQUEST_EDIT_CONTACT = 2;

    ContactAdapter mAdapter;

    DatabaseHelper mDBHelper = null;

    ContactModel oldContact;

    public DatabaseHelper getDBHelper() {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        }
        return mDBHelper;
    }

    public ContactListFragment() {
    }

    public void addContact(ContactModel contact) {
        mAdapter.add(contact);
    }

    public void deleteContact() {
        mAdapter.remove(oldContact);
    }

    public void updateContact(ContactModel newContact) {
        mAdapter.update(oldContact, newContact);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    protected void callContactActivityToCreate() {
        Intent intent = new Intent(getActivity(), ContactInfoActivity.class);
        intent.setAction(ContactInfoFragment.ACTION_NEW_CONTACT);
        startActivityForResult(intent, REQUEST_NEW_CONTACT);
    }

    protected void callContactActivityToUpdate(ContactModel contact) {
        oldContact = contact;
        Intent intent = new Intent(getActivity(), ContactInfoActivity.class);
        intent.setAction(ContactInfoFragment.ACTION_EDIT_CONTACT);
        intent.putExtra(ContactInfoFragment.EXTRA_CONTACT, contact);
        startActivityForResult(intent, REQUEST_EDIT_CONTACT);
    }

    protected void prepareListView() {
        List<ContactModel> entries;
        try {
            entries = getDBHelper().getDocumentDao().queryForAll();
        } catch (SQLException e) {
            entries = new ArrayList<>();
            e.printStackTrace();
        }
        mAdapter = new ContactAdapter(getActivity(), mDBHelper, entries);
        setListAdapter(mAdapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callContactActivityToUpdate(mAdapter.getItem(position));
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_contact_list_fragment, menu);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prepareListView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        Boolean handled = false;

        switch (menuId) {
            case R.id.add_contact:
                callContactActivityToCreate();
                handled = true;
                break;
        }

        if (!handled) {
            handled = super.onOptionsItemSelected(item);
        }

        return handled;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_NEW_CONTACT:
                if (resultCode == Activity.RESULT_OK) {
                    addContact((ContactModel) data.getParcelableExtra(ContactInfoFragment.EXTRA_CONTACT));
                }
                break;
            case REQUEST_EDIT_CONTACT:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getStringExtra(ContactInfoFragment.EXTRA_ACTION_TAKEN)
                            .equals(ContactInfoFragment.RESULT_ACTION_UPDATE)) {
                        updateContact((ContactModel) data.getParcelableExtra(ContactInfoFragment.EXTRA_CONTACT));
                    }
                    else if (data.getStringExtra(ContactInfoFragment.EXTRA_ACTION_TAKEN)
                            .equals(ContactInfoFragment.RESULT_ACTION_DELETE)) {
                        deleteContact();
                    }
                }
        }
    }

    @Override
    public void onDestroy() {
        if (mDBHelper != null) {
            OpenHelperManager.releaseHelper();
            mDBHelper = null;
        }
        super.onDestroy();
    }
}
