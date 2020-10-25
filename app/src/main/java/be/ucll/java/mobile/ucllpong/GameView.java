package be.ucll.java.mobile.ucllpong;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = GameView.class.getSimpleName();

    private GameThread gt;

    // Constructor
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        getHolder().addCallback(this);
        setFocusable(true);

        // Do not call reset() here since the screen dimensions are undefined at constructor time
    }

    /* --------------  Event handling ------------------------------ */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Action MOVE is triggered when a finger is down and rests on the screen
        // Action DOWN is triggered when a finger OR A MOUSE CLICK is down
        if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d(TAG, "X: " + Math.round(event.getX()) + ", Y: " + Math.round(event.getY()));
            gt.processTouch(event.getX(), event.getY());
        }
        return true;
    }

    /* --------------   SurfaceHolder.Callback methods ------------ */

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        reset();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Stop the thread in a controlled way. gt.stop() is deprecated.
        pause();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // This typically occurs when the screen is turned (portrait <=> landscape)
        if (gt != null) {
            gt.zetSchermdimenties(getHeight(), getWidth());
        }
    }

    /* --------------  Reset application  ----------------------- */

    public void reset() {
        // Stop the current thread if any.
        pause();

        // Create a new thread
        gt = new GameThread(getContext(), getHolder());
        gt.zetSchermdimenties(getHeight(), getWidth());
        gt.start();
    }

    public void pause() {
        if (gt != null && gt.isRunning()) gt.setRunning(false);
    }

    public void resume() {
        if (gt != null && !gt.isRunning()) gt.setRunning(true);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // BAD PRACTICE !!!
        // Do not code or refresh your canvas here since you would code it on the MAIN thread
        // instead of doing it on a separate "Game" thread.
    }
}
