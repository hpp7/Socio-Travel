package com.cockpunchers.commutator2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SOS extends Activity {
public static final String PREFS_NUM = "MyNum";
String sosnum,currsos;
EditText sos;
TextView showsos;
Button upd;
@Override
protected void onCreate(Bundle savedInstanceState) {
// TODO Auto-generated method stub
super.onCreate(savedInstanceState);
setContentView(R.layout.sos);
sos=(EditText)findViewById(R.id.newsosno);
upd=(Button)findViewById(R.id.updatesos);
showsos=(TextView)findViewById(R.id.currsosno);
SharedPreferences settings = getSharedPreferences(PREFS_NUM, 0);
   currsos = settings.getString("Num", "No SOS number set");
   showsos.setText(currsos);
upd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) 
            {
            	sosnum=sos.getText().toString();
                clearErrors();
            	boolean cancel = false;
        		View focusView = null;
        		
        		if (TextUtils.isEmpty(sosnum)) {
        			sos.setError(getString(R.string.error_field_required));
        			focusView = sos;
        			cancel = true;
        		}
        		else
        		{
            SharedPreferences settings = getSharedPreferences(PREFS_NUM, 0);
               SharedPreferences.Editor editor = settings.edit();
               editor.putString("Num", sosnum);
            editor.commit();
            Intent intent = getIntent();
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    finish();
       startActivity(intent);
        		}
                
                }

            });
    }
    private void clearErrors(){
    	sos.setError(null);
    	
    }



}