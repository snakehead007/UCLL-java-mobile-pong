package be.ucll.java.mobile.ucllpong;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.TextView;

import java.util.Random;

public class GameThread extends Thread {
    private static final String TAG = GameThread.class.getSimpleName();

    // Android context
    private final Context ctx;
    private final SurfaceHolder holder;

    // Labels
    private final TextView txtScore;

    // Deze thread draait in een oneindige lus totdat 'running' is false
    private boolean running;

    // Canvasses and paint
    private Canvas canvas;
    private final Paint redPaint;

    // Schermdimensies
    private int schermBreedte;
    private int schermHoogte;

    // De bal
    private float balX;
    private float balY;
    private int balHoogte;
    private int balBreedte;
    private float balBewegingX;
    private float balBewegingY;

    //Andere
    private final Random random;
    private int score;

    // Constructor
    public GameThread(Context ctx, SurfaceHolder holder) {
        this.ctx = ctx;
        this.holder = holder;

        txtScore = ((Activity) ctx).findViewById(R.id.txtScore);

        redPaint = new Paint();
        redPaint.setARGB(255, 255, 0, 0);

        random = new Random();
    }

    public void zetSchermdimenties(int hoogte, int breedte) {
        Log.d(TAG, "Schermdimenties. hoogte: " + hoogte + ", breedte: " + breedte);

        // Reset schermdimenties. Dit is alleen het geval wanneer het toestel gedraaid wordt.
        this.schermHoogte = hoogte;
        this.schermBreedte = breedte;

        // Overige initialisaties
        balBewegingX = 0f;
        balBewegingY = 0f;
        balHoogte = (schermBreedte / 20);
        balBreedte = (schermBreedte / 20);
        balX = (schermBreedte / 2) - (balBreedte / 2);
        balY = schermHoogte / 2; // Dit zal anders moeten worden als bal boven palet moet zweven

        score = 0;

        running = true;
    }

    @Override
    public void run() {
        while (running) {
            try {
                canvas = holder.lockCanvas();
                tekenBalEnPallet();
            } catch (Throwable t) {
                t.printStackTrace();
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    // Update the canvas. Verplaats de objecten.
    public void tekenBalEnPallet() {
        // 1. Work the numbers
        balX += balBewegingX;
        balY += balBewegingY;

        // 2. Draw on the canvas
        if (canvas != null) {
            // Wis het volledige scherm en zet een witte achtergrond
            canvas.drawRGB(250, 255, 255);

            // Teken de bal. Why rectangle ??? round circles dont have X or Y which makes it difficult to work with
            canvas.drawRoundRect(balX, balY, balX + balBreedte, balY + balHoogte, 45, 45, redPaint);

            // Teken het rode pallet

        }
    }

    public void processTouch(float posX, float posY) {
        // Je moet 1 keer het scherm aanraken om het spel te starten.
        if (balBewegingX == 0f && balBewegingY == 0f) {
            balBewegingX = genereerRandomFloat(-5.0f, 5.0f); // X mag positief of negatief zijn
            balBewegingY = genereerRandomFloat(-5.0f, -2.0f); // Y negatief = naar omhoog
            Log.d(TAG, "Balbeweging. X: " + balBewegingX + ", Y: " + balBewegingY);

            setScoreText(txtScore, ctx.getString(R.string.score) + ": " + score);
        }
    }

    private float genereerRandomFloat(float min, float max) {
        return min + (max - min) * random.nextFloat();
    }

    private void setScoreText(final TextView txtScore, final String textToDisplay) {
        ((Activity) ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtScore.setText(textToDisplay);
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
