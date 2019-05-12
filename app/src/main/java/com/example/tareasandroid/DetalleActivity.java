package com.example.tareasandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DetalleActivity extends AppCompatActivity {


    private AdaptadorBBDD dbAdapter;
    private Cursor cursor;

    //
    // Modo del formulario
    //
    private int modo ;

    //
    // Identificador del registro que se edita cuando la opción es MODIFICAR
    //
    private long id ;

    //
    // Elementos de la vista
    //
    private EditText nombre;
    private EditText detalle;
    private EditText fecha;
    private EditText estado;
    private EditText alarma;
    private FloatingActionButton boton_eliminar;
    private FloatingActionButton boton_ejecutar;
    private String index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        this.setTitle("Detalles de la tarea");

        Intent intent = getIntent();
        Bundle extra = intent.getExtras();

        if (extra == null) return;

        //
        // Obtenemos los elementos de la vista
        //
        nombre = (EditText) findViewById(R.id.tarea_nombre);

        detalle = (EditText) findViewById(R.id.tarea_detalle);
        fecha = (EditText) findViewById(R.id.tarea_fecha);
        estado = (EditText) findViewById(R.id.tarea_estado);
        alarma = (EditText) findViewById(R.id.tarea_alarma);

        boton_eliminar = (FloatingActionButton) findViewById(R.id.boton_eliminar);
        boton_eliminar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                borrar(id);
            }
        });

        boton_ejecutar = (FloatingActionButton) findViewById(R.id.boton_ejecutar);
        boton_ejecutar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                ejecutar(id);
            }
        });

        //
        // Creamos el adaptador
        //
        dbAdapter = new AdaptadorBBDD(this);
        dbAdapter.abrir();

        //
        // Obtenemos el identificador del registro si viene indicado
        //
        if (extra.containsKey(AdaptadorBBDD.C_COLUMNA_ID))
        {
            id = extra.getLong(AdaptadorBBDD.C_COLUMNA_ID);

            consultar(id);
        }

        //
        // Establecemos el modo del formulario
        //
        establecerModo(extra.getInt(MainActivity.C_MODO));

    }

    private void establecerModo(int m)
    {
        this.modo = m ;

        if (modo == MainActivity.C_VISUALIZAR)
        {
            //this.setTitle(nombre.getText().toString());
            this.setEdicion(false);
        }
    }

    private void consultar(long id)
    {
        //
        // Consultamos los datos a mostrar segun ID
        //
        cursor = dbAdapter.getRegistro(id);

        nombre.setText(cursor.getString(cursor.getColumnIndex(AdaptadorBBDD.C_COLUMNA_NOMBRE)));
        detalle.setText(cursor.getString(cursor.getColumnIndex(AdaptadorBBDD.C_COLUMNA_DETALLE)));
        fecha.setText(cursor.getString(cursor.getColumnIndex(AdaptadorBBDD.C_COLUMNA_FECHA)));
        estado.setText(cursor.getString(cursor.getColumnIndex(AdaptadorBBDD.C_COLUMNA_ESTADO)));
        alarma.setText(cursor.getString(cursor.getColumnIndex(AdaptadorBBDD.C_COLUMNA_ALARMA)));
        index = cursor.getString(cursor.getColumnIndex(AdaptadorBBDD.C_COLUMNA_ID));

        if(cursor.getString(cursor.getColumnIndex(AdaptadorBBDD.C_COLUMNA_ESTADO)).equals("Realizada")){
            boton_ejecutar.hide();boton_eliminar.setBackgroundColor(4);
            estado.setTextColor(Color.parseColor("#229B0f"));
        }
        else estado.setTextColor(Color.parseColor("#FF0000"));

    }

    private void setEdicion(boolean opcion)
    {
        nombre.setEnabled(opcion);
        detalle.setEnabled(opcion);
        fecha.setEnabled(opcion);
        estado.setEnabled(opcion);
        alarma.setEnabled(opcion);
    }


    //ELIMINA REGISTRO SEGUN ID
    private void borrar(final long id)
    {
        /**
         * Borramos el registro con confirmación
         */
        AlertDialog.Builder dialogEliminar = new AlertDialog.Builder(this);

        dialogEliminar.setIcon(android.R.drawable.ic_dialog_alert);
        dialogEliminar.setTitle("Eliminar Tarea");
        dialogEliminar.setMessage("¿Seguro que desea eliminar la tarea?");
        dialogEliminar.setCancelable(false);

        dialogEliminar.setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int boton) {
                dbAdapter.delete(id);
                Toast.makeText(DetalleActivity.this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
                /**
                 * Devolvemos el control
                 */
                setResult(RESULT_OK);
                finish();
            }
        });

        dialogEliminar.setNegativeButton(android.R.string.no, null);

        dialogEliminar.show();

    }


    //CAMBIA ESTADO DE LA TAREA A REALIZADO
    private void ejecutar(final long id)
    {
        /**
         * Borramos el registro con confirmación
         */
        AlertDialog.Builder dialogConfirmar = new AlertDialog.Builder(this);

        dialogConfirmar.setIcon(android.R.drawable.ic_dialog_info);
        dialogConfirmar.setTitle("Ejecutar Tarea");
        dialogConfirmar.setMessage("¿Ejecutar la tarea seleccionada?");
        dialogConfirmar.setCancelable(false);

        dialogConfirmar.setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int boton) {
                dbAdapter.update(id);
                Toast.makeText(DetalleActivity.this, "Tarea ejecutada", Toast.LENGTH_SHORT).show();
                /**
                 * Devolvemos el control
                 */
                //setResult(RESULT_OK);
                boton_ejecutar.hide();
                estado.setText("Realizada");
                estado.setTextColor(Color.parseColor("#229B0f"));
                //finish();
            }
        });

        dialogConfirmar.setNegativeButton(android.R.string.no, null);

        dialogConfirmar.show();

    }

}
