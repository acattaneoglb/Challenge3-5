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

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContactListFragment extends ListFragment {

    public final static int REQUEST_NEW_CONTACT = 1;

    ContactAdapter mAdapter;

    public ContactListFragment() {
    }

    public void addContact(ContactModel contact) {
        mAdapter.add(contact);
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

    private void prepareListView() {
        List<ContactModel> entries = new ArrayList<>();
        mAdapter = new ContactAdapter(getActivity(), entries);
        setListAdapter(mAdapter);
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

    protected void callCreateContactActivity() {
        Intent intent = new Intent(getActivity(), CreateContactActivity.class);
        startActivityForResult(intent, REQUEST_NEW_CONTACT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        Boolean handled = false;

        switch (menuId) {
            case R.id.add_contact:
                callCreateContactActivity();
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
        if (requestCode == REQUEST_NEW_CONTACT) {
            if (resultCode == Activity.RESULT_OK) {
                addContact((ContactModel) data.getParcelableExtra(CreateContactFragment.EXTRA_CONTACT));
            }
        }
    }
}
