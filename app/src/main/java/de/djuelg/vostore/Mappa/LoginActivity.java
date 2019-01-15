package de.djuelg.vostore.Mappa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.tfb.fbtoast.FBToast;

import de.djuelg.vostore.Firebase.ConfiguracaoFirebase;
import de.djuelg.vostore.Firebase.Usuario;

import de.djuelg.vostore.R;
import de.djuelg.vostore.helper.Preferencias;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private Button btnLogin,btnGoogle, btnRegistro;
    private EditText senhausuario,emailusuario;
    public static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth,firebaseAuth;
    private GoogleApiClient googleApiClient;
   // private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login);
        //verificarUsuarioLogado();

        //Fazendo o Cast dos botões e campos

        senhausuario = findViewById(R.id.senhaid);
        emailusuario = findViewById(R.id.emailid);
        //databaseReference = FirebaseDatabase.getInstance().getReference();
        btnGoogle = findViewById(R.id.btn_google);
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        //Instanciando o servidor de dados
       // mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        // Adicionando uma ação ao evento do click

        // Adicionando uma ação ao evento do click

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginGoogle();


            }
        });


    }

     boolean validarsenha(){
        String contraseña;
        contraseña = senhausuario.getText().toString();
        if(contraseña.length()>=6 && contraseña.length()<=16){
            return true;
        }else return false;
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(LoginActivity.this, MainActivityMappa.class);
        startActivity(intent);
        finish();


    }
    private void verificarUsuarioLogado(){



    }

    private void abrirTelaPrincipal() {


            Intent intent = new Intent(LoginActivity.this,MainActivityMappa.class);
            startActivity(intent);


    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    //Login com o Google
    private void loginGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            saveUser(currentUser, acct.getEmail());
                        } else
                            Toast.makeText(LoginActivity.this, "Authentication with Google failed.",
                                    Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed
                //Log.e(TAG, "Google Sign In failed.");
            }
        }
        //Sem essa instrução , o login do facebook não irá funcionar
        //else
            //mCallbackManager.onActivityResult(requestCode, resultCode, data);
        // mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }
    private void saveUser (FirebaseUser firebaseUser, String email){
        FBToast.successToast(LoginActivity.this,"Login efetuado com sucesso !", FBToast.LENGTH_SHORT);
        FirebaseUser currentUser = firebaseUser;
        Preferencias preferencias = new Preferencias(LoginActivity.this);
        preferencias.savePreferences(getString(R.string.id_user_app), firebaseUser.getUid());
        preferencias.savePreferences(getString(R.string.username_app), firebaseUser.getDisplayName());
        if (email != null) preferencias.savePreferences(getString(R.string.email_app), email);

        Usuario user = new Usuario();
        user.setId(firebaseUser.getUid());
        user.setNome(currentUser.getDisplayName());
        if (email != null)
            user.setEmail(email);
        user.saveUser();
        updateUI();


    }
    //Abrir tela seguinte
    private  void updateUI(){
        // Toast.makeText(Login.this, "Login Realizado", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, MainActivityMappa.class);
        startActivity(intent);
        finish();

    }
}

