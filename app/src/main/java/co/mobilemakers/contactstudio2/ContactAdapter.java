package co.mobilemakers.contactstudio2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * TODO: Using info from http://developer.android.com/training/camera/photobasics.html
 *
 * Created by ariel.cattaneo on 05/02/2015.
 */
public class ContactAdapter extends ArrayAdapter<ContactModel> {

    Context mContext;
    List<ContactModel> mContactList;
    DatabaseHelper mDBHelper;

    public ContactAdapter(Context context, DatabaseHelper dbHelper, List<ContactModel> todoList) {
        super(context, R.layout.contact_entry, todoList);

        mDBHelper = dbHelper;
        mContext = context;
        mContactList = todoList;
    }

    private void displayContentInRowView(final int position, View rowView) {
        if (rowView != null) {
            ContactModel contact = mContactList.get(position);
            TextView textName = (TextView)rowView.findViewById(R.id.text_name);
            String nickname = contact.getNickname();
            if (nickname.isEmpty()) {
                textName.setText(contact.getName());
            }
            else {
                textName.setText(nickname);
            }

            String photoURL = contact.getPhotoURL();
            if (!photoURL.isEmpty()) {
                ImageView photoView = (ImageView) rowView.findViewById(R.id.image_photo);
                ImageUtils.setPic(photoView, photoURL);
            }
        }
    }

    private View reuseOrGenerateRowView(View convertView, ViewGroup parent) {
        View rowView;
        if (convertView != null) {
            rowView = convertView;
        }
        else {
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.contact_entry, parent, false);
        }
        return rowView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;

        rowView = reuseOrGenerateRowView(convertView, parent);

        displayContentInRowView(position, rowView);

        return rowView;
    }

    @Override
    public void add(ContactModel contact) {
        super.add(contact);

        try {
            Dao<ContactModel, Integer> documentDao = mDBHelper.getDocumentDao();
            documentDao.create(contact);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
