package com.davgeek.tareasisc;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends ListActivity {

	int backpress = 0; //Variable para el boton regresar
	
	private Context context;
	
	Boolean isInternetPresent = false; //Variables para el metodo que checa la conexion.
	ConnectionDetector cd;
	
	private static String url = "http://davgeek.com/tareas/";
		
	private static final String TAG_TAREA = "tarea";
	public static final String TAG_MATERIA = "materia";
	private static final String TAG_FECHA = "fecha_entrega";
	
	//Lista de arrelogs desplegables
	ArrayList<HashMap<String, String>> jsonlist = new ArrayList<HashMap<String,String>>();
	
	//PullToRefreshListView listView = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_listview);
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if(isInternetPresent){
        	new GetJSONActivity(MainActivity.this).execute();
        }else{
        	showAlertDialog(MainActivity.this, "No hay conexion a Internet",
                    "Intenta revisar tu conexion a internet.", false);
        }     
     
    }

    @Override
    public void onBackPressed() {
    	
    	backpress+=1;
    	
    	cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if(isInternetPresent){
        	showAlertDialog(MainActivity.this, "Recuerda!","Realizar y entregar todas tus tareas.", false);
        }else{
        	backpress+=1;
        }     
    	
        if (backpress>1) {
            this.finish();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Metodo que muestra ventana de alerta
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
 
        // Estableciendo titulo
        alertDialog.setTitle(title);
 
        // Estableciendo mensaje
        alertDialog.setMessage(message);
         
        // Estableciendo icono
        alertDialog.setIcon((status) ? R.drawable.tarea : R.drawable.error);
 
        // Mostrando el dialogo
        alertDialog.show();
    }
    
 // Añadiendo funcionalidad a las opciones de menú
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()) {
            case R.id.actualizar:
               cd = new ConnectionDetector(getApplicationContext());
               isInternetPresent = cd.isConnectingToInternet();
               if(isInternetPresent){
            	   Toast.makeText(getApplicationContext(), "Actualizando", Toast.LENGTH_SHORT).show();
               		
               }else{
               	showAlertDialog(MainActivity.this, "No se puede Actualizar",
                           "Intenta revisar tu conexion a internet.", false);
               }
               return true;
            case R.id.action_settings:
               Toast.makeText(getApplicationContext(), "Acerca De", Toast.LENGTH_SHORT).show();
               Intent i = new Intent(this, AcerdaDe.class );
               startActivity(i);
               return true;
            default:
               return super.onOptionsItemSelected(item);
        }
    }

    //Clase que crea los hilos
    private class GetJSONActivity extends AsyncTask<String, Void, String>{
    	public GetJSONActivity(ListActivity activity){
    		context = activity;
    	}
    	

		
		protected String doInBackground(final String... params) {
			JSONParser jParser = new JSONParser();
			
			//obteniendo JSON
			JSONArray json = jParser.GetJSONfromUrl(url);
			
			jsonlist.clear();//Limpia la Lista antes de mostrar
			
			for (int i = 0; i < json.length(); i++) {
				
				try {
					JSONObject c = json.getJSONObject(i);
					
					String materia = c.getString(TAG_MATERIA);
					String vtarea = c.getString(TAG_TAREA);
					String fecha = c.getString(TAG_FECHA);
					
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(TAG_MATERIA, materia);
					map.put(TAG_TAREA, vtarea);
					map.put(TAG_FECHA, fecha);
					jsonlist.add(map);
					} catch (JSONException e) {
						e.printStackTrace();
						return "Error creando variables";
						}
				}
			
			return "Exito";
		}


		@Override
		protected void onPostExecute(String succes) {

			ListAdapter adapter = new SimpleAdapter(context, jsonlist, R.layout.items, new String[] { TAG_MATERIA, TAG_TAREA, TAG_FECHA}, new int[] {R.id.Materia, R.id.Tareas, R.id.Fecha});
			setListAdapter(adapter);
			// Seleccionando un solo item de la lista
	        ListView lv = getListView();
	 
	        // Ejecetando un activity para cada item
	        lv.setOnItemClickListener(new OnItemClickListener() {
	 
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                // Tomando datos del item seleccionado
	                String materia1 = ((TextView) view.findViewById(R.id.Materia)).getText().toString();
	                String tarea1 = ((TextView) view.findViewById(R.id.Tareas)).getText().toString();
	                String fecha1 = ((TextView) view.findViewById(R.id.Fecha)).getText().toString();
	                 
	                // Iniciando el intent
	                Intent in = new Intent(getApplicationContext(), SingleMenuItemActivity.class);
	                in.putExtra("TAG_MATERIA", materia1);
	                in.putExtra("TAG_TAREA", tarea1);
	                in.putExtra("TAG_FECHA", fecha1);
	                startActivity(in);
	            }

	        });
		}
		
		
    	
    }
    
    
    
}
