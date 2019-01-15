package de.djuelg.vostore.Mappa;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import de.djuelg.vostore.R;

/**
 * Essa classe é utilizada como tela inicial. Possui uma animação e faz transição após 3 segundos para a MainActivity
 */
public class SplashActivity extends AppCompatActivity implements Runnable {

    ImageView top;
    Animation fromlogo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

       //Fazendo cast e instanciando a animação
        top = findViewById(R.id.logosplash);
        fromlogo = AnimationUtils.loadAnimation(this, R.anim.fromlogo);
        top.setAnimation(fromlogo);

    //Determinando o tempo de 3 segundos para entrar na próxima activity
        Handler handler = new Handler();
        handler.postDelayed(this, 3000);



    }

    //Usando intent no método run
    public void run(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}