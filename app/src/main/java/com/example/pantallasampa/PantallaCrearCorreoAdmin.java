package com.example.pantallasampa;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class PantallaCrearCorreoAdmin extends AppCompatActivity {

    private EditText destinatarioEditText;
    private EditText asuntoEditText;
    private EditText contenidoEditText;
    private Button enviarButton;
    private Spinner spinnerTipoCorreo;
    private EditText editTextCurso;
    private TextView irAPantallaTextView;

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
        irAPantallaTextView = findViewById(R.id.textView16);

        // Obtener una referencia a la base de datos de Firebase
        correosRef = FirebaseDatabase.getInstance().getReference().child("correos");

        // Inicializar el spinner con los datos apropiados
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipos_correo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoCorreo.setAdapter(adapter);

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
                if (selectedOption.equals("Enviar a todos los usuarios del curso")) {
                    editTextCurso.setEnabled(true);
                    destinatarioEditText.setEnabled(false); // Deshabilitar el campo de destinatario
                } else {
                    editTextCurso.setEnabled(false);
                    destinatarioEditText.setEnabled(true); // Habilitar el campo de destinatario
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacemos nada aquí
            }
        });

        // Configurar el onClickListener para el TextView irAPantalla
        irAPantallaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAPantalla();
            }
        });
    }

    private void enviarCorreo() {
        String tipoCorreo = spinnerTipoCorreo.getSelectedItem().toString();

        String destinatario = destinatarioEditText.getText().toString();
        String asunto = asuntoEditText.getText().toString();
        String contenido = contenidoEditText.getText().toString();
        String curso = editTextCurso.getText().toString();

        // Obtener el correo electrónico del remitente
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String remitente = currentUser.getEmail();

        // Validar que los campos no estén vacíos
        if (TextUtils.isEmpty(asunto) || TextUtils.isEmpty(contenido)) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar el tipo de correo y tomar acciones correspondientes
        switch (tipoCorreo) {
            case "Enviar a todos los usuarios":
                enviarCorreoATodosLosUsuarios(remitente, asunto, contenido);
                break;
            case "Enviar a todos los usuarios del curso":
                if (TextUtils.isEmpty(curso)) {
                    Toast.makeText(this, "Por favor, introduzca un curso", Toast.LENGTH_SHORT).show();
                } else {
                    enviarCorreoAUsuariosConHijosEnClaseEspecifica(remitente, asunto, contenido, curso);
                }
                break;
            case "Enviar a una persona":
                if (TextUtils.isEmpty(destinatario)) {
                    Toast.makeText(this, "Por favor, introduzca un destinatario", Toast.LENGTH_SHORT).show();
                } else {
                    enviarCorreoAPersonaEspecifica(remitente, destinatario, asunto, contenido);
                }
                break;
            default:
                Toast.makeText(this, "Seleccione un tipo de correo", Toast.LENGTH_SHORT).show();
        }
    }


    private void enviarCorreoATodosLosUsuarios(String remitente, String asunto, String contenido) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("usuarios");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String destinatario = userSnapshot.child("email").getValue(String.class);
                    enviarCorreoIndividual(remitente, destinatario, asunto, contenido);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PantallaCrearCorreoAdmin.this, "Error al obtener usuarios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enviarCorreoIndividual(String remitente, String destinatario, String asunto, String contenido) {
        Correo correo = new Correo(null, remitente, destinatario, asunto, contenido, System.currentTimeMillis(), false, false);

        String correoId = correosRef.push().getKey();
        correo.setCorreoId(correoId);
        correosRef.child(correoId).setValue(correo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PantallaCrearCorreoAdmin.this, "Correo enviado correctamente", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PantallaCrearCorreoAdmin.this, "Error al enviar el correo", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void enviarCorreoAUsuariosConHijosEnClaseEspecifica(String remitente, String asunto, String contenido, String curso) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("usuarios");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if (userSnapshot.hasChild("hijos")) {
                        for (DataSnapshot hijoSnapshot : userSnapshot.child("hijos").getChildren()) {
                            String claseHijo = hijoSnapshot.child("curso").getValue(String.class);
                            if (claseHijo.equals(curso)) {
                                String destinatario = userSnapshot.child("email").getValue(String.class);
                                enviarCorreoIndividual(remitente, destinatario, asunto, contenido);
                                // Agregar mensaje Toast para indicar que se envió un correo
                                Toast.makeText(PantallaCrearCorreoAdmin.this, "Correo enviado a: " + destinatario, Toast.LENGTH_SHORT).show();
                                break;
                            } else {
                                // Agregar mensaje Toast para indicar que no se encontraron usuarios para el curso especificado
                                Toast.makeText(PantallaCrearCorreoAdmin.this, "No se encontraron usuarios para el curso especificado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        // Agregar mensaje Toast para indicar que el usuario no tiene hijos registrados
                        Toast.makeText(PantallaCrearCorreoAdmin.this, "El usuario no tiene hijos registrados", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PantallaCrearCorreoAdmin.this, "Error al obtener usuarios", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void enviarCorreoAPersonaEspecifica(String remitente, String destinatario, String asunto, String contenido) {
        Correo correo = new Correo(null, remitente, destinatario, asunto, contenido, System.currentTimeMillis(), false, false);

        String correoId = correosRef.push().getKey();
        correo.setCorreoId(correoId);
        correosRef.child(correoId).setValue(correo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PantallaCrearCorreoAdmin.this, "Correo enviado correctamente", Toast.LENGTH_SHORT).show();
                        destinatarioEditText.setText("");
                        asuntoEditText.setText("");
                        contenidoEditText.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PantallaCrearCorreoAdmin.this, "Error al enviar el correo", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void irAPantalla() {
        Intent intent = new Intent(this, PantallaMensajesAdmin.class);
        startActivity(intent);
    }
}
