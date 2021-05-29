package me.buddhabhu.canvasexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

public class MyCanvasView extends View {

    private Paint mPaint;
    private Path mPath;
    private Canvas mCanvas;
    private Bitmap mBitmap;

    private int mDrawColor;
    private int mBackgroundColor;

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private Rect mFrame;

    private void init(AttributeSet attrs) {

        if(true) {
            mBackgroundColor = ResourcesCompat.getColor(getResources(),
                    R.color.opaque_orange, null);
            mDrawColor = ResourcesCompat.getColor(getResources(),
                    R.color.opaque_yellow, null);

            // Holds the path we are currently drawing
            mPath = new Path();
            // Set up the Paint with which to draw
            mPaint = new Paint();
            mPaint.setColor(mDrawColor);

            // Smoothes out the edges of what is drawn without affecting shape
            mPaint.setAntiAlias(false);

            mPaint.setDither(true);
            mPaint.setStyle(Paint.Style.STROKE);      // default: FILL
            mPaint.setStrokeJoin(Paint.Join.ROUND);   // default: MITER
            mPaint.setStrokeCap(Paint.Cap.ROUND);     // default: BUTT
            mPaint.setStrokeWidth(12);                // default: Hairline width(very thin)
        }

    }

    public MyCanvasView(Context context) {
        super(context);
        init(null);
    }

    public MyCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MyCanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Create Bitmap, create canvas with bitmap, fill canvas with color
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        // Fill bitmap with background color
        mCanvas.drawColor(mBackgroundColor);

        // Calculate the rect a frame around the picture.
        int inset = 40;
        mFrame = new Rect(inset, inset, w - inset, h - inset);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw the bitmap that stores the path the user has drawn.
        // Initially the user has not drawn anything
        // so we see only the colored bitmap.
        canvas.drawBitmap(mBitmap, 0, 0, null);

        // Draw a frame around the picture.
        canvas.drawRect(mFrame, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        // Invalidate() is inside the case statements because there are many
        // other types of motion events passed into this listener,
        // and we don't want to invalidate the view for those.

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                // No need to invalidate because we are not drawing anything.
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                // No need to invalidate because we are not drawing anything.
                break;
            default:
                // Do nothing

        }
        return true;
    }

    private void touchStart(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if(dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            // Reset mX and mY to the last drawn point.
            mX = x;
            mY = y;
            // Save the path in the extra bitmap,
            // which we access through its canvas.
            mCanvas.drawPath(mPath, mPaint);
        }
    }

    private void touchUp() {
        mPath.reset();
    }
}
