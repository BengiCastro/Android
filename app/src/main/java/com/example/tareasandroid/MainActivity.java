package com.example.tareasandroid;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionMenu;

public class MainActivity extends ListActivity {


    //DECLARAMOS CONSTANTES Y ELEMENTOS DEL LAYOUT

    public static final String C_MODO  = "modo" ;
    public static final int C_VISUALIZAR = 551 ;
    public static final int C_CREAR = 552 ;

    private AdaptadorBBDD dbAdapter;
    private Cursor cursor;
    private AlimentaLista tareasAdapter ;
    private ListView lista;

    private FloatingActionButton boton_nuevo;
    private FloatingActionMenu opciones;
    private Toolbar titulo;
    private String filtro = null;
    private String filtro_fechas = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //INSTANCIAMOS LOS ELEMENTOS

        lista = (ListView) findViewById(android.R.id.list);

        dbAdapter = new AdaptadorBBDD(this);
        dbAdapter.abrir();



        opciones = (FloatingActionMenu) findViewById(R.id.opciones);
        opciones.setClosedOnTouchOutside(true);

        //LLENAMOS EL LISVIEW CON LOS DATOS DE LA BBDD
        consultar_confiltro();

        titulo = (Toolbar) findViewById(R.id.toolbar);

        boton_nuevo = (FloatingActionButton) findViewById(R.id.boton_nuevo);


    }


    //CONSULTAR SIN FILTRO SQL
   /* private void consultar()
    {
        cursor = dbAdapter.getCursor();
        startManagingCursor(cursor);
        tareasAdapter = new AlimentaLista(this, cursor);
        lista.setAdapter(tareasAdapter);
    }*/


   // NUEVO CONSULTAR CON FILTROS
    private void consultar_confiltro()
    {
        cursor = dbAdapter.getCursor(filtro,filtro_fechas);
        startManagingCursor(cursor);
        tareasAdapter = new AlimentaLista(this, cursor);
        lista.setAdapter(tareasAdapter);
        opciones.close(true);
    }


    // SE ABRE EL DETALLE DE CADA ITEM DEL LISTVIEW

    private void visualizar(long id)
    {
        // Llamamos a la Actividad DetalleActivity indicando el modo visualización y el identificador del registro


        Intent i = new Intent(MainActivity.this, DetalleActivity.class);
        i.putExtra(C_MODO, C_VISUALIZAR);
        i.putExtra(AdaptadorBBDD.C_COLUMNA_ID, id);

        startActivityForResult(i, C_VISUALIZAR);
    }


    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);

        opciones.close(true);
        visualizar(id);
    }

    // FUNCION LLAMADA POR EL BOTON NUEVO, PERMITE AGREGAR NUEVA TAREA
    public void insertar(View v)
    {

        Intent i;
        i = new Intent(MainActivity.this, NuevaTareaActivity.class);
        i.putExtra(C_MODO, C_CREAR);
        startActivityForResult(i, C_CREAR);

    }

    //PERMITE HACER CONSULTA CON FILTRO SQL
    public void TareasTodas(View v)
    {

        titulo.setTitle("Todas las tareas");
        filtro=null;
        consultar_confiltro();
        //opciones.close(true);
    }

    //PERMITE HACER CONSULTA CON FILTRO SQL
    public void TareasPendientes(View v)
    {

        titulo.setTitle("Tareas Pendientes");
        filtro = AdaptadorBBDD.C_COLUMNA_ESTADO + " = 'Pendiente' " ;
        consultar_confiltro();
        //opciones.close(true);
    }

    //PERMITE HACER CONSULTA CON FILTRO SQL
    public void TareasRealizadas(View v)
    {

        titulo.setTitle("Tareas Realizadas");
        filtro = AdaptadorBBDD.C_COLUMNA_ESTADO + " = 'Realizada' " ;
        consultar_confiltro();
        //opciones.close(true);
    }


    //PERMITE HACER CONSULTA CON FILTRO SQL
    public void Filtro_FechasAscendente(View v)
    {

        titulo.setTitle("Ascendentes por Fecha");
        filtro_fechas = AdaptadorBBDD.C_COLUMNA_FECHA + " ASC" ;
        consultar_confiltro();
        //opciones.close(true);
    }


    //PERMITE HACER CONSULTA CON FILTRO SQL
    public void Filtro_FechasDescendente(View v)
    {

        titulo.setTitle("Descendentes por Fecha");
        filtro_fechas = AdaptadorBBDD.C_COLUMNA_FECHA + " DESC" ;
        consultar_confiltro();
        //opciones.close(true);
    }


    //CAMBIA LOS MODOS
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //
        // Nos aseguramos que es la petición que hemos realizado
        //

        switch(requestCode)
        {
            case C_CREAR:
                if (resultCode == RESULT_OK)
                    consultar_confiltro();

            case C_VISUALIZAR:
                if (resultCode == RESULT_OK)
                    consultar_confiltro();

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

}








