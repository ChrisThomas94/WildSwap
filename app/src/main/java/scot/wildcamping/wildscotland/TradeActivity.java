package scot.wildcamping.wildscotland;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Chris on 06-Feb-16.
 */
public class TradeActivity extends Activity {

    private static Bitmap imageOriginal, imageScaled;
    private static Bitmap imageOriginal2, imageScaled2;

    private static Matrix matrix;
    private static Matrix matrix2;

    private ImageView dialer;
    private ImageView dialer2;

    private Button cancelTrade;
    private Button sendTrade;

    private int dialerHeight, dialerWidth;
    private int dialerHeight2, dialerWidth2;

    private GestureDetector detector;
    private GestureDetector detector2;

    // needed for detecting the inversed rotations
    private boolean[] quadrantTouched;
    private boolean allowRotating;

    ArrayList<LatLng> cluster = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            cluster = extras.getParcelableArrayList("cluster");
            System.out.println("Trade" + cluster);
        }

        // load the image only once
        if (imageOriginal == null) {
            imageOriginal = BitmapFactory.decodeResource(getResources(), R.drawable.spinner25);
        }

        if (imageOriginal2 == null) {
            imageOriginal2 = BitmapFactory.decodeResource(getResources(), R.drawable.spinner25);
        }

        // initialize the matrix only once
        if (matrix == null) {
            matrix = new Matrix();
        } else {
            // not needed, you can also post the matrix immediately to restore the old state
            matrix.reset();
        }

        if (matrix2 == null) {
            matrix2 = new Matrix();
        } else {
            // not needed, you can also post the matrix immediately to restore the old state
            matrix2.reset();
        }

        detector = new GestureDetector(this, new MyGestureDetector());
        detector2 = new GestureDetector(this, new MyGestureDetector2());

        // there is no 0th quadrant, to keep it simple the first value gets ignored
        quadrantTouched = new boolean[] { false, false, false, false, false };
        allowRotating = true;

        //initializing views
        dialer = (ImageView) findViewById(R.id.send_Spinner);
        dialer2 = (ImageView) findViewById(R.id.receive_Spinner);
        cancelTrade = (Button) findViewById(R.id.btnCancel_Trade);
        sendTrade = (Button) findViewById(R.id.btnSend_Trade);

        //set listeners
        dialer.setOnTouchListener(new MyOnTouchListener());
        dialer2.setOnTouchListener(new MyOnTouchListener2());

        cancelTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        MainActivity_Spinner.class);
                startActivity(intent);
                finish();
            }
        });

        sendTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dialer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // method called more than once, but the values only need to be initialized one time
                if (dialerHeight == 0 || dialerWidth == 0) {
                    dialerHeight = dialer.getHeight();
                    dialerWidth = dialer.getWidth();

                    // resize
                    Matrix resize = new Matrix();
                    resize.postScale((float) Math.min(dialerWidth, dialerHeight) / (float) imageOriginal.getWidth(), (float) Math.min(dialerWidth, dialerHeight) / (float) imageOriginal.getHeight());
                    imageScaled = Bitmap.createBitmap(imageOriginal, 0, 0, imageOriginal.getWidth(), imageOriginal.getHeight(), resize, false);

                    // translate to the image view's center
                    float translateX = dialerWidth / 2 - imageScaled.getWidth() / 2;
                    float translateY = dialerHeight / 2 - imageScaled.getHeight() / 2;
                    matrix.postTranslate(translateX, translateY);

                    dialer.setImageBitmap(imageScaled);
                    dialer.setImageMatrix(matrix);
                }
            }
        });

        dialer2.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // method called more than once, but the values only need to be initialized one time
                if (dialerHeight2 == 0 || dialerWidth2 == 0) {
                    dialerHeight2 = dialer2.getHeight();
                    dialerWidth2 = dialer2.getWidth();

                    // resize
                    Matrix resize = new Matrix();
                    resize.postScale((float)Math.min(dialerWidth2, dialerHeight2) / (float)imageOriginal2.getWidth(), (float)Math.min(dialerWidth2, dialerHeight2) / (float)imageOriginal2.getHeight());
                    imageScaled2 = Bitmap.createBitmap(imageOriginal2, 0, 0, imageOriginal2.getWidth(), imageOriginal2.getHeight(), resize, false);

                    // translate to the image view's center
                    float translateX = dialerWidth2 / 2 - imageScaled2.getWidth() / 2;
                    float translateY = dialerHeight2 / 2 - imageScaled2.getHeight() / 2;
                    matrix2.postTranslate(translateX, translateY);

                    dialer2.setImageBitmap(imageScaled2);
                    dialer2.setImageMatrix(matrix2);
                }
            }
        });

    }


    private class MyOnTouchListener implements View.OnTouchListener {

        private double startAngle;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    // reset the touched quadrants
                    for (int i = 0; i < quadrantTouched.length; i++) {
                        quadrantTouched[i] = false;
                    }

                    allowRotating = false;

                    startAngle = getAngle(event.getX(), event.getY());
                    break;

                case MotionEvent.ACTION_MOVE:
                    double currentAngle = getAngle(event.getX(), event.getY());
                    rotateDialer((float) (startAngle - currentAngle));
                    startAngle = currentAngle;
                    break;

                case MotionEvent.ACTION_UP:
                    allowRotating = true;
                    break;
            }

            // set the touched quadrant to true
            quadrantTouched[getQuadrant(event.getX() - (dialerWidth / 2), dialerHeight - event.getY() - (dialerHeight / 2))] = true;

            detector.onTouchEvent(event);

            return true;
        }

    }

    private class MyOnTouchListener2 implements View.OnTouchListener {

        private double startAngle;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    // reset the touched quadrants
                    for (int i = 0; i < quadrantTouched.length; i++) {
                        quadrantTouched[i] = false;
                    }

                    allowRotating = false;

                    startAngle = getAngle2(event.getX(), event.getY());
                    break;

                case MotionEvent.ACTION_MOVE:
                    double currentAngle = getAngle2(event.getX(), event.getY());
                    rotateDialer2((float) (startAngle - currentAngle));
                    startAngle = currentAngle;
                    break;

                case MotionEvent.ACTION_UP:
                    allowRotating = true;
                    break;
            }

            // set the touched quadrant to true
            quadrantTouched[getQuadrant(event.getX() - (dialerWidth2 / 2), dialerHeight2 - event.getY() - (dialerHeight2 / 2))] = true;

            detector2.onTouchEvent(event);

            return true;
        }

    }

    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // get the quadrant of the start and the end of the fling
            int q1 = getQuadrant(e1.getX() - (dialerWidth / 2), dialerHeight - e1.getY() - (dialerHeight / 2));
            //int q1 = Math.atan2(e1.getX(), e1.getY());
            int q2 = getQuadrant(e2.getX() - (dialerWidth / 2), dialerHeight - e2.getY() - (dialerHeight / 2));
            //int q2 = Math.atan2(e2.getX(), e2.getY());

            // the inversed rotations
            if ((q1 == 2 && q2 == 2 && Math.abs(velocityX) < Math.abs(velocityY))
                    || (q1 == 3 && q2 == 3)
                    || (q1 == 1 && q2 == 3)
                    || (q1 == 4 && q2 == 4 && Math.abs(velocityX) > Math.abs(velocityY))
                    || ((q1 == 2 && q2 == 3) || (q1 == 3 && q2 == 2))
                    || ((q1 == 3 && q2 == 4) || (q1 == 4 && q2 == 3))
                    || (q1 == 2 && q2 == 4 && quadrantTouched[3])
                    || (q1 == 4 && q2 == 2 && quadrantTouched[3])) {

                dialer.post(new FlingRunnable(-1 * (velocityX + velocityY)));
            } else {
                // the normal rotation
                dialer.post(new FlingRunnable(velocityX + velocityY));
            }

            return true;
        }

    }

    private class MyGestureDetector2 extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // get the quadrant of the start and the end of the fling
            int q1 = getQuadrant(e1.getX() - (dialerWidth2 / 2), dialerHeight2 - e1.getY() - (dialerHeight2 / 2));
            //int q1 = Math.atan2(e1.getX(), e1.getY());
            int q2 = getQuadrant(e2.getX() - (dialerWidth2 / 2), dialerHeight2 - e2.getY() - (dialerHeight2 / 2));
            //int q2 = Math.atan2(e2.getX(), e2.getY());

            // the inversed rotations
            if ((q1 == 2 && q2 == 2 && Math.abs(velocityX) < Math.abs(velocityY))
                    || (q1 == 3 && q2 == 3)
                    || (q1 == 1 && q2 == 3)
                    || (q1 == 4 && q2 == 4 && Math.abs(velocityX) > Math.abs(velocityY))
                    || ((q1 == 2 && q2 == 3) || (q1 == 3 && q2 == 2))
                    || ((q1 == 3 && q2 == 4) || (q1 == 4 && q2 == 3))
                    || (q1 == 2 && q2 == 4 && quadrantTouched[3])
                    || (q1 == 4 && q2 == 2 && quadrantTouched[3])) {

                dialer2.post(new FlingRunnable2(-1 * (velocityX + velocityY)));
            } else {
                // the normal rotation
                dialer2.post(new FlingRunnable2(velocityX + velocityY));
            }

            return true;
        }

    }

    private class FlingRunnable implements Runnable {

        private float velocity;

        public FlingRunnable(float velocity) {
            this.velocity = velocity;
        }

        @Override
        public void run() {
            if (Math.abs(velocity) > 5 && allowRotating) {
                rotateDialer(velocity / 75);
                velocity /= 1.0666F;

                // post this instance again
                dialer.post(this);
            }
        }
    }

    private class FlingRunnable2 implements Runnable {

        private float velocity;

        public FlingRunnable2(float velocity) {
            this.velocity = velocity;
        }

        @Override
        public void run() {
            if (Math.abs(velocity) > 5 && allowRotating) {
                rotateDialer2(velocity / 75);
                velocity /= 1.0666F;

                // post this instance again
                dialer2.post(this);
            }
        }
    }

    private double getAngle(double xTouch, double yTouch) {
        double x = xTouch - (dialerWidth / 2d);
        double y = dialerHeight - yTouch - (dialerHeight / 2d);

        switch (getQuadrant(x, y)) {
            case 1:
                return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 2:
                return 180 - Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 3:
                return 180 + (-1 * Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
            case 4:
                return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            default:
                return 0;
        }
    }

    private double getAngle2(double xTouch, double yTouch) {
        double x = xTouch - (dialerWidth2 / 2d);
        double y = dialerHeight2 - yTouch - (dialerHeight2 / 2d);

        switch (getQuadrant(x, y)) {
            case 1:
                return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 2:
                return 180 - Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 3:
                return 180 + (-1 * Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
            case 4:
                return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            default:
                return 0;
        }
    }

    private static int getQuadrant(double x, double y) {
        if (x >= 0) {
            return y >= 0 ? 1 : 4;
        } else {
            return y >= 0 ? 2 : 3;
        }
    }

    private void rotateDialer(float degrees) {
        matrix.postRotate(degrees, dialerWidth / 2, dialerHeight / 2);

        dialer.setImageMatrix(matrix);
    }

    private void rotateDialer2(float degrees) {
        matrix2.postRotate(degrees, dialerWidth2 / 2, dialerHeight2 / 2);

        dialer2.setImageMatrix(matrix2);
    }


}

