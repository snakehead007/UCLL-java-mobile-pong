package be.ucll.prog4.ucllpong;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private GameView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gv = findViewById(R.id.vwGame);
    }

    @Override
    protected void onPause() {
        gv.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        gv.resume();
        super.onResume();
    }
}
