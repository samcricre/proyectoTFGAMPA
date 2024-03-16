package com.example.pantallasampa;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PantallaCrearCorreoAdmin extends AppCompatActivity {

    private EditText destinatarioEditText;
    private EditText asuntoEditText;
    private EditText contenidoEditText;
    private Button enviarButton;
    private RadioGroup radioGroup;
    private Spinner spinnerTipoCorreo;
    private EditText editTextCurso;

    private DatabaseReference correosRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_crear_correo_admin);

        destinatarioEditText = findViewById(R.id.editTextDestinatario);
        asuntoEditText = findViewById(R.id.editTextAsunto);
        contenidoEditText = findViewById(R.id.editTextContenido);
        enviarButton = findViewById(R.id.buttonEnviar);
        spinnerTipoCorreo = findViewById(R.id.spinnerTipoCorreo);
        editTextCurso = findViewById(R.id.editTextCurso);

        // Obtener una referencia a la base de datos de Firebase
        correosRef = FirebaseDatabase.getInstance().getReference().child("correos");

        enviarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarCorreo();
            }
        });

        // Habilitar o deshabilitar el EditText de curso según el tipo de correo seleccionado
        spinnerTipoCorreo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                if (selectedOption.equals("Filtrar por curso")) {
                    editTextCurso.setEnabled(true);
                } else {
                    editTextCurso.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacemos nada aquí
            }
        });
    }

    private void enviarCorreo() {
        // Obtener el tipo de correo seleccionado
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        String tipoCorreo = radioButton.getText().toString();

        String destinatario = destinatarioEditText.getText().toString();
        String asunto = asuntoEditText.getText().toString();
        String contenido = contenidoEditText.getText().toString();
        String curso = editTextCurso.getText().toString();

        // Obtener el correo electrónico del remitente
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String remitente = currentUser.getEmail();

        // Validar que los campos no estén vacíos
        if (TextUtils.isEmpty(destinatario) || TextUtils.isEmpty(asunto) || TextUtils.isEmpty(contenido)) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar el tipo de correo y tomar acciones correspondientes
        switch (tipoCorreo) {
            case "Todos los usuarios no admins":
                // Lógica para enviar correo a todos los usuarios no admins
                // Implementa la lógica según tus requisitos
                break;
            case "Usuarios con hijos en una clase":
                // Lógica para enviar correo a usuarios con hijos en una clase específica
                // Implementa la lógica según tus requisitos
                break;
            case "Persona específica":
                // Lógica para enviar correo a una persona específica
                // Implementa la lógica según tus requisitos
                break;
            default:
                Toast.makeText(this, "Seleccione un tipo de correo", Toast.LENGTH_SHORT).show();
        }
    }
}
