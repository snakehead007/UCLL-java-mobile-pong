package be.ucll.prog4.ucllpong;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.TextView;

import java.util.Random;

public class GameThread extends Thread {
    // Android context
    private final Context ctx;
    private final SurfaceHolder holder;

    // Labels
    private final TextView txtScore;

    // Deze thread draait in een oneindige lus totdat 'running' is false
    private boolean running;

    // Canvasses and paint
    private Canvas canvas;
    private Paint paint;

    // Schermdimensies
    private int schermBreedte;
    private int schermHoogte;

    // De bal
    private float balX;
    private float balY;
    private int balHoogte;
    private int balBreedte;
    private float balSnelheidX;
    private float balSnelheidY;

    //Andere
    private Random random;
    private int score;

    // Constructor
    public GameThread(Context ctx, SurfaceHolder holder) {
        this.ctx = ctx;
        this.holder = holder;

        txtScore = ((Activity) ctx).findViewById(R.id.txtScore);

        paint = new Paint();
        paint.setARGB(255, 255, 0, 0);

        random = new Random();
    }

    @Override
    public void run() {
        while (running) {
            try {
                canvas = holder.lockCanvas();
                update();
                draw();
            } catch (Throwable t) {
                t.printStackTrace();
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    public void initialiseer() {
        balSnelheidX = 0f;
        balSnelheidY = 0f;
        balHoogte = (schermBreedte / 20);
        balBreedte = (schermBreedte / 20);
        balX = (schermBreedte / 2) - (balBreedte / 2);
        balY = schermHoogte / 2; // Dit is fout. Dit zal iets anders moeten worden

        score = 0;

        running = true;
    }

    // Update the canvas. Verplaats de objecten.
    public void update() {
        // Verplaats bal in richting x of y
        balX += balSnelheidX;
        balY += balSnelheidY;

    }

    public void processTouch(float posX, float posY) {
        // Je moet 1 keer het scherm aanraken om het spel te starten.
        if (balSnelheidX == 0f && balSnelheidY == 0f) {
            balSnelheidX = genereerRandomFloat(-5.0f, 5.0f);
            balSnelheidY = genereerRandomFloat(-5.0f, -2.0f); // Negatief = naar omhoog

            setText(txtScore, ctx.getString(R.string.score) + ": " + score);
        }
    }

    public void zetSchermdimenties(int hoogte, int breedte) {
        Log.d("GT - Schermdimenties", "Hoogte: " + hoogte + ", breedte: " + breedte);

        // Reset schermdimenties. Dit is alleen het geval wanneer het toestel gedraaid wordt.
        this.schermHoogte = hoogte;
        this.schermBreedte = breedte;

        this.initialiseer();
    }

    private void draw() {
        if (canvas != null) {
            // Wis het scherm en zet een witte achtergrond
            canvas.drawRGB(250, 255, 255);

            // Teken de bal. Why rectangle ??? round circles dont have X or Y which makes it difficult to work with
            canvas.drawRoundRect(balX, balY, balX + balBreedte, balY + balHoogte, 45, 45, paint);

            // Teken het rode pallet

        }
    }

    private float genereerRandomFloat(float min, float max) {
        return min + (max - min) * random.nextFloat();
    }

    private void setText(final TextView text, final String value) {
        ((Activity) ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
