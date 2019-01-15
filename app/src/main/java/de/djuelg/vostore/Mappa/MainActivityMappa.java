package de.djuelg.vostore.Mappa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.djuelg.vostore.Firebase.ConfiguracaoFirebase;
import de.djuelg.vostore.R;
import de.djuelg.vostore.presentation.ui.activities.MainActivity;

public class MainActivityMappa extends AppCompatActivity {

    private Button representante, restrito,site2,site3;
    private TextView sair, mappafrases, txt_autor;
    private FirebaseAuth auth;
    private FirebaseDatabase database ;
    private DatabaseReference ref, ref2;
    private ValueEventListener valueEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mappa);


        representante = findViewById(R.id.btn_restrito);
        site2 = findViewById(R.id.btn_site2);
        txt_autor = findViewById(R.id.txt_autor);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        ref = database.getReference("mensagem").child("frase");
        ref2 = database.getReference("mensagem").child("autor");
        site3 = findViewById(R.id.btn_site3);
        sair = findViewById(R.id.sair);
        mappafrases = findViewById(R.id.mappafrases);




        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        representante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityMappa.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        site2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityMappa.this, Site2.class);
                startActivity(intent);
                finish();
            }
        });
        site3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityMappa.this, Site3.class);
                startActivity(intent);
                finish();
            }
        });

        ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Mensagens mensagens = new Mensagens();
                String mensagem = dataSnapshot.getValue(String.class);
                mensagens.setMensagem(mensagem);
                mappafrases.setText(mensagem);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref2.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Mensagens mensagens = new Mensagens();
                String autor = dataSnapshot.getValue(String.class);
                mensagens.setAutor(autor);
                txt_autor.setText(autor);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
