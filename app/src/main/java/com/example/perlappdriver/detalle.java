package com.example.perlappdriver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class detalle extends AppCompatActivity {
Spinner spinnerruta;
Spinner spinnerCamion;
Button registrar;
int valorRuta, valorCamion;
List<String> rutasobt =  new ArrayList<String>();
List<String> camionobt =  new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        spinnerruta=(Spinner) findViewById(R.id.ruta);
        registrar=(Button) findViewById(R.id.registrar);
        spinnerCamion=(Spinner) findViewById(R.id.spinner2);
        cargarrutas("http://perlapp.laviveshop.com/app/rutas.php");
        cargarcamiones("http://perlapp.laviveshop.com/app/camion.php");
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valorRuta=spinnerruta.getSelectedItemPosition();
                valorRuta=valorRuta+1;
                valorCamion=spinnerCamion.getSelectedItemPosition();
                valorCamion=valorCamion+1;
                String idChofer=getIntent().getStringExtra("id");
                registrarRuta("http://perlapp.laviveshop.com/app/consultarrutas.php",idChofer,valorRuta,valorCamion);
            }
        });
    }
    private void cargarrutas(String URL)
    {
     StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
         @Override
         public void onResponse(String response) {
             try {
                 JSONObject valores = new JSONObject(response);
                 JSONArray jsonArray=valores.getJSONArray("rutas");
                 for(int i=0;i<jsonArray.length();i++)
                 {
                     JSONObject jsonObject=jsonArray.getJSONObject(i);
                     String nombre=jsonObject.getString("nombre_ruta");
                     rutasobt.add(nombre);
                 }
                 spinnerruta.setAdapter(new ArrayAdapter<String>(detalle.this,android.R.layout.simple_spinner_dropdown_item,rutasobt));
             } catch (JSONException e) {
                 e.printStackTrace();
             }
         }
     }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {
             Toast.makeText(detalle.this,error.toString(),Toast.LENGTH_SHORT).show();
         }
     })
     {

     };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void cargarcamiones(String URL)
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject valores = new JSONObject(response);
                    JSONArray jsonArray=valores.getJSONArray("camion");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String numero=jsonObject.getString("numero");
                        camionobt.add(numero);
                    }
                    spinnerCamion.setAdapter(new ArrayAdapter<String>(detalle.this,android.R.layout.simple_spinner_dropdown_item,camionobt));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(detalle.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {

        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private  void registrarRuta(String URL, final String idChofer, final int ruta, final int camion)
    {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.isEmpty()) {
                    Log.d("valorresponse", response);
                    Intent intent = new Intent(getApplicationContext(), Mapa.class);
                    startActivity(intent);
                    finish();
                }else
                    {
                        Toast.makeText(getApplicationContext(),"No puedes registrar una ruta sino cierras la anterior",Toast.LENGTH_SHORT).show();
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error ya tiene una ruta este usuario",Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("identificador",idChofer);
                parametros.put("ruta", String.valueOf(ruta));
                parametros.put("camion", String.valueOf(camion));
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

