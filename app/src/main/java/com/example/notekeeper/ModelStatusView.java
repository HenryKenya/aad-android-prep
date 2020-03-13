package com.example.notekeeper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class ModelStatusView extends View {
    public static final int EDIT_MODE_MODULE_COUNT = 7;
    public static final int INVALID_INDEX = -1;
    private boolean[] mModuleStatus;
    private float outlineWidth;
    private float shapeSize;
    private float shapeSpacing;
    private Rect[] moduleRectanges;
    private int outlineColor;
    private Paint paintOutline;
    private int fillColor;
    private Paint paintFill;
    private float radius;
    private int maxHorizontalModules;

    public ModelStatusView(Context context) {
        super(context);
        init(null, 0);
    }

    public ModelStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ModelStatusView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        if (isInEditMode()) {
            setupEditModeValues();
        }

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ModelStatusView, defStyle, 0);

        outlineColor = a.getColor(R.styleable.ModelStatusView_outlineColor, Color.BLACK);

        a.recycle();

        outlineWidth = 6f;
        shapeSize = 144f;
        shapeSpacing = 33f;
        radius = (shapeSize - outlineWidth) / 2;

        // outlineColor = Color.BLACK;
        paintOutline = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintOutline.setStyle(Paint.Style.STROKE);
        paintOutline.setStrokeWidth(outlineWidth);
        paintOutline.setColor(outlineColor);

        fillColor = getContext().getResources().getColor(R.color.pluralsight_orange);
        paintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintFill.setStyle(Paint.Style.FILL);
        paintFill.setColor(fillColor);

    }

    private void setupEditModeValues() {
        boolean[] exampleModuleValues = new boolean[EDIT_MODE_MODULE_COUNT];
        int middle = EDIT_MODE_MODULE_COUNT / 2;
        for (int i = 0; i < middle; i++)
            exampleModuleValues[i] = true;

        setmModuleStatus(exampleModuleValues);
    }

    private void setUpModelRectangle(int width) {

        int availableWidth = width - getPaddingRight() - getPaddingLeft();
        int horizontalModulesThatCanFit = (int) (availableWidth / (shapeSpacing + shapeSize));
        int maximumModules = Math.min(horizontalModulesThatCanFit, mModuleStatus.length);

        moduleRectanges = new Rect[mModuleStatus.length];

        for (int moduleIndex = 0; moduleIndex < moduleRectanges.length; moduleIndex++) {
            int column = moduleIndex % maximumModules;
            int row = moduleIndex / maximumModules;

            int x = getPaddingLeft() + (int) (column * (shapeSize + shapeSpacing));
            int y = getPaddingTop() + (int) (row * (shapeSize + shapeSpacing));

            moduleRectanges[moduleIndex] = new Rect(x, y, x + (int) shapeSize, y + (int) shapeSize);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        setUpModelRectangle(w);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int moduleIndex = 0; moduleIndex < moduleRectanges.length; moduleIndex++) {

            float x = moduleRectanges[moduleIndex].centerX();
            float y = moduleRectanges[moduleIndex].centerY();

            // filled in circle only for completed module
            if (mModuleStatus[moduleIndex]) {
                canvas.drawCircle(x, y, radius, paintFill);
            }
            // outline
            canvas.drawCircle(x, y, radius, paintOutline);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                int moduleIndex = findItemAtPoint(event.getX(), event.getY());
                onModuleSelected(moduleIndex);
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void onModuleSelected(int moduleIndex) {
        if (moduleIndex == INVALID_INDEX)
            return;
        mModuleStatus[moduleIndex] = !mModuleStatus[moduleIndex];
        invalidate();
    }

    private int findItemAtPoint(float x, float y) {
        int moduleIndex = INVALID_INDEX;
        for (int i = 0; i < moduleRectanges.length; i++) {
            if (moduleRectanges[i].contains((int) x, (int) y)) {
                moduleIndex = i;
                break;
            }
        }
        return moduleIndex;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth;
        int desiredHeight;

        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        int availableWidth = specWidth - getPaddingLeft() - getPaddingRight();
        int horizontalModulesThatCanFit = (int) (availableWidth / (shapeSize + shapeSpacing));
        maxHorizontalModules = Math.min(horizontalModulesThatCanFit, mModuleStatus.length);

        desiredWidth = (int) ((maxHorizontalModules * (shapeSize + shapeSpacing)) - (shapeSpacing));
        desiredWidth += getPaddingRight() + getPaddingLeft();

        int rows = (mModuleStatus.length - 1) / maxHorizontalModules + 1;
        desiredHeight = (int) ((rows * (shapeSize + shapeSpacing)) - shapeSpacing);
        desiredHeight += getPaddingBottom() + getPaddingTop();

        int width = resolveSizeAndState(desiredWidth, widthMeasureSpec, 0);
        int height = resolveSizeAndState(desiredHeight, heightMeasureSpec, 0);

        setMeasuredDimension(width, height);
    }

    public boolean[] getmModuleStatus() {
        return mModuleStatus;
    }

    public void setmModuleStatus(boolean[] mModuleStatus) {
        this.mModuleStatus = mModuleStatus;
    }
}
