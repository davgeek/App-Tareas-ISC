package com.davgeek.tareasisc;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class SingleMenuItemActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_menu_item);
		
		TextView txt = (TextView) findViewById(R.id.materia);
		TextView txt2 = (TextView) findViewById(R.id.tarea);
		TextView txt3 = (TextView) findViewById(R.id.fecha);

        Bundle bundle = getIntent().getExtras();
        
		txt.setText(bundle.getString("TAG_MATERIA"));
		txt2.setText(bundle.getString("TAG_TAREA"));
		txt3.setText(bundle.getString("TAG_FECHA"));

	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.single_menu_item, menu);
		return true;
	}*/

}
