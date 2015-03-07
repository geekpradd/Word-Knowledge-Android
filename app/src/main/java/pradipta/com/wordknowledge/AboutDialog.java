package pradipta.com.wordknowledge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;




public class AboutDialog extends DialogFragment {
    //For this method override click on DialogFragment above > Generate> Override
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder theDialog = new AlertDialog.Builder(getActivity()); //Create the new Dialog using the Builder
        //This is a fragment.. getActivity() returns the activity from which the fragment was called.. (Like this except it's a fragment)
        theDialog.setTitle("About");
        theDialog.setMessage("Created By Pradipta.\n Copyright 2015.");
        theDialog.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast newToast = Toast.makeText(getActivity(),"Thanks for clicking Ok!",Toast.LENGTH_LONG);
                newToast.show();
            }
        });
        theDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(),"Cancel isn't a good thing..",Toast.LENGTH_LONG).show();
            }
        });
        return  theDialog.create();
    }
}
