package be.ucll.java.mobile.ucllpong;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    // Reference to GameView to pass the Pause and Resume state change
    // so the Thread can equally pause/resume
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
        super.onResume();
        gv.resume();
    }
}
