package com.example.pantallasampa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PantallaCrearEventos extends AppCompatActivity {

    //Variables de las pagina
    private CardView btFechaInicio, btFechaFin, btHoraInicio, btHoraFin;
    private EditText titulo, descripcion, textoFechaInicio, textoFechaFin, textoHoraInicio, textoHoraFin;
    private CheckBox duracionTodoDia;
    private Spinner selectCurso;
    private String spinnerOptionSelected = new String();//Inicializamos para que no de error


    //Variables para guardar las fechas
    private int diaInicio, mesInicio,anioInicio,diaFin,mesFin,anioFin,horaInicio,minutoInicio,horaFin,minFin;
    private boolean diaEntero = false;
    private boolean selected = false;

    private DatabaseReference dr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_crear_eventos);

        //Inicializamos todas las variables
        btFechaInicio = findViewById(R.id.btFechaInicio);
        btFechaFin = findViewById(R.id.btFechaFin);
        btHoraInicio = findViewById(R.id.btHoraInicio);
        btHoraFin = findViewById(R.id.btHoraFin);
        titulo = findViewById(R.id.titulo);
        descripcion = findViewById(R.id.descripcion);
        textoFechaInicio = findViewById(R.id.fechaInicio);
        textoFechaFin = findViewById(R.id.fechaFin);
        textoHoraInicio = findViewById(R.id.horaInicio);
        textoHoraFin  = findViewById(R.id.horaFin);
        duracionTodoDia = findViewById(R.id.checkBoxTodoDia);

        //Desabilitamos los fecha y hora para que no se pueda escribir en ellos
        textoFechaFin.setEnabled(false);
        textoFechaInicio.setEnabled(false);
        textoHoraFin.setEnabled(false);
        textoHoraInicio.setEnabled(false);

        //Creamos el spinner de los cursos
        selectCurso = findViewById(R.id.cursoSelectCrearEvent);
        cargarCursos();//cargamos los cursos al Spinner

        //Damos la referencia de la instancia a la variable database reference
        dr = FirebaseDatabase.getInstance().getReference();

        //En el caso de que se haga click en el chech box
        duracionTodoDia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //En el caso de que se haya realizado el check
                if(isChecked){

                    //Las horas se predefinen para que duren el día o días enteros
                    textoHoraInicio.setText("00:00");
                    textoHoraFin.setText("23:59");
                    horaInicio = 0;
                    horaFin = 23;
                    minutoInicio = 0;
                    minFin = 59;

                    //Deshabilita las vistas de hora
                    btHoraInicio.setEnabled(false);
                    btHoraFin.setEnabled(false);

                    //Indicamos que está chequeado
                    diaEntero = true;

                }else{

                    //CheckBox desmarcado
                    //Si el CheckBox no está marcado, habilita las vistas de hora
                    btHoraInicio.setEnabled(true);
                    btHoraFin.setEnabled(true);

                    //Indicamos que deja de estar chequeado
                    diaEntero = false;

                }

            }
        });

        //Creamos un listenner en el spinner
        selectCurso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Si se ha seleccionado una de las opciones se guarda en esta variable y se señala que ha sido seleccionada
                spinnerOptionSelected = parent.getItemAtPosition(position).toString();
                selected = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Si no hay ninguno (No hace nada porque no se puede no seleccionar ninguno)
                selected = false;
            }
        });


    }

    //Si le damos a Cancelar
    public void cancelarCreacionTask (View view){
        Intent i = new Intent(this,PantallaEventos.class);
        i.putExtra("emailUser", getIntent().getStringExtra("emailUser"));
        startActivity(i);
    }

    //Si le damos al icono al lado de fecha inicio aparece un date picker del cual se selecciona fecha
    //Esto pasa en el fecha final también. Aparte están limitados al día de hoy
    public void fechaInicio(View view){

        final Calendar calendario = Calendar.getInstance();
        diaInicio = calendario.get(Calendar.DAY_OF_MONTH);
        mesInicio = calendario.get(Calendar.MONTH);
        anioInicio = calendario.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int diaDeMes) {
                anioInicio = year;
                mesInicio = month+1;
                diaInicio = diaDeMes;

                textoFechaInicio.setText(convertirFormatoFecha(diaInicio+"/"+mesInicio+"/"+anioInicio));
            }
        },diaInicio,mesInicio,anioInicio);

        //Limitación de día minimo
        datePickerDialog.getDatePicker().setMinDate(calendario.getTimeInMillis());
        datePickerDialog.show();

    }


    public void fechaFin(View view){

        final Calendar calendario = Calendar.getInstance();
        diaFin = calendario.get(Calendar.DAY_OF_MONTH);
        mesFin = calendario.get(Calendar.MONTH);
        anioFin = calendario.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int diaDeMes) {
                anioFin = year;
                mesFin = month+1;
                diaFin = diaDeMes;

                textoFechaFin.setText(convertirFormatoFecha(diaFin+"/"+mesFin+"/"+anioFin));
            }
        },diaFin,mesFin,anioFin);

        datePickerDialog.getDatePicker().setMinDate(calendario.getTimeInMillis());

        datePickerDialog.show();

    }

    //Similar al datepicker que hemos utilizado en fechas, pero en este caso para las horas
    public void horaInicio(View view){

        final Calendar calendario = Calendar.getInstance();
        horaInicio = calendario.get(Calendar.HOUR_OF_DAY);
        minutoInicio = calendario.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hora, int minuto) {

                horaInicio = hora;
                minutoInicio = minuto;
                textoHoraInicio.setText(convertirFormatoHora(horaInicio+":"+minutoInicio));
            }
        }, horaInicio, minutoInicio,false);
        timePickerDialog.show();
    }

    public void horaFin(View view) {

        final Calendar calendario = Calendar.getInstance();
        horaFin = calendario.get(Calendar.HOUR_OF_DAY);
        minFin = calendario.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hora, int minuto) {

                horaFin = hora;
                minFin = minuto;
                textoHoraFin.setText(convertirFormatoHora(horaFin + ":" + minFin));
            }
        }, horaFin, minFin, false);
        timePickerDialog.show();
    }

    //Cuando de damos click en guardar
    public void btGuardar(View view){

        //Se almacenan todos los strings de los edittexts
        String fechaInicioTxt = textoFechaInicio.getText().toString();
        String fechaFinTxt = textoFechaFin.getText().toString();
        String horaInicioTxt = textoHoraInicio.getText().toString();
        String horaFinTxt = textoHoraFin.getText().toString();

        //Se crea un nuevo envento
        Evento nuevoEvento;

        //Se tiene que pasar la validación
        if(!validacion(fechaInicioTxt,fechaFinTxt,horaInicioTxt,horaFinTxt))
            return;

        //Si se valida se crea el nuevo evento
        nuevoEvento = new Evento(titulo.getText().toString(),descripcion.getText().toString(),spinnerOptionSelected,fechaInicioTxt,fechaFinTxt,horaInicioTxt,horaFinTxt);


        //Conseguimos la key de eventos y lo metemos en la bae de datos
        String key = dr.child("eventos").push().getKey();
        dr.child("eventos").child(key).setValue(nuevoEvento).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(PantallaCrearEventos.this, "Evento creado", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PantallaCrearEventos.this, PantallaEventos.class);
                intent.putExtra("emailUser", getIntent().getStringExtra("emailUser"));
                startActivity(intent);
            }
        });
    }

    //Validación de los datos introducidos
    private boolean validacion(String fechaInicioTxt, String fechaFinTxt, String horaInicioTxt, String horaFinTxt){
        //Filtro de que ningún campo esté vacío
        if(titulo.getText().toString().isEmpty() || descripcion.getText().toString().isEmpty() || !selected || fechaFinTxt.isEmpty() || fechaInicioTxt.isEmpty() || horaInicioTxt.isEmpty() || horaFinTxt.isEmpty()){
            Toast.makeText(this, "Todos los parámetros son necesarios", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Vemos si la fecha final no es menor que la fecha inicial
        if(anioFin < anioInicio){
            Toast.makeText(this, "La fecha final tiene que ser mayor a la fecha inicial", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            if (anioFin == anioInicio){
                if(mesFin < mesInicio){
                    Toast.makeText(this, "La fecha final tiene que ser mayor a la fecha inicial", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else{
                    if(mesFin == mesInicio){
                        if(diaFin < diaInicio){
                            Toast.makeText(this, "La fecha final tiene que ser mayor a la fecha inicial", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
                }
            }
        }

        //Comprobamos que la hora inicio sea menor que la hora final si este evento empieza y acaba el mismo día
        if(horaFin < horaInicio && ((diaInicio == diaFin) && (anioInicio == anioFin) && (mesInicio == mesFin))&&diaEntero){
            Toast.makeText(this, "La hora final debe ser mayor a la hora inicial", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            if (horaFin == horaInicio && ((diaInicio == diaFin) && (anioInicio == anioFin) && (mesInicio == mesFin))&&diaEntero){
                if(minFin <= minutoInicio){
                    Toast.makeText(this, "La hora inicial debe ser mayor a la hora final", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }

        return true;
    }

    //Método que nos sirve para rellevar el Spinner con sus opciones
    private void cargarCursos(){
        String [] cursos = {"1ºA", "1ºB", "1ºC","2ºA", "2ºB", "2ºC","3ºA", "3ºB", "3ºC","4ºA", "4ºB", "4ºC","5ºA", "5ºB", "5ºC","6ºA", "6ºB", "6ºC"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(PantallaCrearEventos.this, android.R.layout.simple_spinner_item, cursos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectCurso.setAdapter(adapter);
    }

    public String convertirFormatoFecha(String fecha) {
        try {
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("d/M/yyyy");
            SimpleDateFormat formatoSalida = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaParseada = formatoEntrada.parse(fecha);
            return formatoSalida.format(fechaParseada);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String convertirFormatoHora(String hora) {
        try {
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("H:m");
            SimpleDateFormat formatoSalida = new SimpleDateFormat("HH:mm");
            Date horaParseada = formatoEntrada.parse(hora);
            return formatoSalida.format(horaParseada);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}