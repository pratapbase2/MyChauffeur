package com.mychauffeurapp.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mychauffeurapp.R;
import com.mychauffeurapp.common.SessionManager;

import java.util.Calendar;

public class LogoutFragment extends Fragment {
   /* Calendar mcurrentTime = Calendar.getInstance();
    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    final int minute = mcurrentTime.get(Calendar.MINUTE);
    final int seconds=mcurrentTime.get(Calendar.SECOND);
    final int milliseconds=mcurrentTime.get(Calendar.MILLISECOND);
    final int date=mcurrentTime.get(Calendar.DATE);*/
    SQLiteDatabase myDB = null;
    String TableName = "MychauffeurAppTable";
    String whereClause = "UserId = ?";
    String[] whereArgs;
    //String UserId = "UserId";
    SQLiteDatabase mydb;
    SQLiteOpenHelper dbHelper;

    private SessionManager session;

    String userid;
    Activity context;
    /*public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

    }*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_screen, container,false);

        userid = getArguments().getString("UserId");
        whereArgs = new String[] { userid };

        //SQLiteDatabase mydb;
        Toast.makeText(context, userid, Toast.LENGTH_LONG).show();

        /*session = new SessionManager(getActivity());

        Cursor c= mydb.rawQuery("SELECT * FROM MychauffeurAppTable WHERE UserId='"+userid+"'", null);
        if(c.moveToFirst())
        {
            // Deleting record if found
            mydb.execSQL("DELETE FROM MychauffeurAppTable WHERE UserId='"+userid+"'",null);
            Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(context, "Error in deleting", Toast.LENGTH_LONG).show();
        }
        c.close();
        Intent in = new Intent(getActivity(), LoginScreen.class);
        startActivity(in);
        getActivity().finish();
*/
        return rootView;

       // return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Add this here:
        context = getActivity();

        //mydb = new SQLiteDatabase(getActivity());
        /*//mydb = dbHelper.getWritableDatabase();
        mydb.delete(TableName, whereClause , whereArgs);

        Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show();*/

    }
}
