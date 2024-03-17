package com.example.pantallasampa;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
                enviarCorreoATodosLosUsuariosNoAdmins(remitente, asunto, contenido);
                break;
            case "Usuarios con hijos en una clase":
                enviarCorreoAUsuariosConHijosEnClaseEspecifica(remitente, asunto, contenido,curso);

                break;
            case "Persona específica":
                enviarCorreoAPersonaEspecifica(remitente, asunto, contenido);
                break;
            default:
                Toast.makeText(this, "Seleccione un tipo de correo", Toast.LENGTH_SHORT).show();
        }
    }

    // Lógica para enviar correo a todos los usuarios no admins
    private void enviarCorreoATodosLosUsuariosNoAdmins(String remitente, String asunto, String contenido) {
        // Obtener una referencia a la base de datos de usuarios
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("usuarios");

        // Realizar una consulta para obtener todos los usuarios no admins
        Query query = usersRef.orderByChild("roll").equalTo("user");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iterar sobre los usuarios y enviar correo a cada uno
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String destinatario = userSnapshot.child("email").getValue(String.class);
                    enviarCorreoIndividual(remitente, destinatario, asunto, contenido);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar el error en caso de que la consulta falle
                Toast.makeText(PantallaCrearCorreoAdmin.this, "Error al obtener usuarios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para enviar correo a un destinatario específico
    private void enviarCorreoIndividual(String remitente, String destinatario, String asunto, String contenido) {
        // Crear un objeto Correo con los datos ingresados
        // Establecer los atributos eliminado y eliminadoRemitente como false por defecto
        Correo correo = new Correo(null, remitente, destinatario, asunto, contenido, System.currentTimeMillis(), false, false);

        // Guardar el correo en la base de datos
        String correoId = correosRef.push().getKey(); // Obtener el ID único del correo
        correo.setCorreoId(correoId); // Asignar el correoId al objeto Correo
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

    private void enviarCorreoAUsuariosConHijosEnClaseEspecifica(String remitente, String asunto, String contenido, String clase) {
        // Obtener una referencia a la base de datos de usuarios
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("usuarios");

        // Realizar una consulta para obtener todos los usuarios que tienen al menos un hijo en la clase especificada
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iterar sobre los usuarios
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Verificar si el usuario tiene hijos
                    if (userSnapshot.hasChild("hijos")) {
                        // Iterar sobre los hijos del usuario
                        for (DataSnapshot hijoSnapshot : userSnapshot.child("hijos").getChildren()) {
                            String claseHijo = hijoSnapshot.child("curso").getValue(String.class);
                            // Verificar si el hijo asiste a la clase especificada
                            if (claseHijo.equals(clase)) {
                                // Enviar correo al usuario
                                String destinatario = userSnapshot.child("email").getValue(String.class);
                                enviarCorreoIndividual(remitente, destinatario, asunto, contenido);
                                // Salir del bucle interno, ya que solo necesitamos enviar un correo por usuario
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar el error en caso de que la consulta falle
                Toast.makeText(PantallaCrearCorreoAdmin.this, "Error al obtener usuarios", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void enviarCorreoAPersonaEspecifica(String asunto, String contenido, String destinatario) {
        // Obtener el correo electrónico del remitente
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Error: usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }
        String remitente = currentUser.getEmail();

        // Crear un objeto Correo con los datos ingresados
        // Establecer los atributos eliminado y eliminadoRemitente como false por defecto
        Correo correo = new Correo(null, remitente, destinatario, asunto, contenido, System.currentTimeMillis(), false, false);

        // Guardar el correo en la base de datos
        String correoId = correosRef.push().getKey(); // Obtener el ID único del correo
        correo.setCorreoId(correoId); // Asignar el correoId al objeto Correo
        correosRef.child(correoId).setValue(correo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PantallaCrearCorreoAdmin.this, "Correo enviado correctamente", Toast.LENGTH_SHORT).show();
                        // Limpiar los campos de texto después de enviar el correo
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




}
