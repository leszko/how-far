package com.summercoding.howfar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

public class Arrow extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final Point pointA = new Point();
    private final Point pointB = new Point();
    private final Point pointC = new Point();
    private final Point pointD = new Point();

    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;

    private float rotation = 0f;

    public Arrow(Context context, AttributeSet attrs) {
        super(context, attrs);

        initPaint();
    }

    private void initPaint() {
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBottom = getPaddingBottom();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        int size = Math.min(contentHeight, contentWidth);
        int arrowLength = size - size / 4;
        int arrowWidth = size / 6;
        int holeSize = size / 5;

        pointA.set(contentWidth / 2, contentHeight / 2 - arrowLength / 2);
        pointB.set(contentWidth / 2 - arrowWidth, contentHeight / 2 + arrowLength / 2);
        pointC.set(contentWidth / 2, contentHeight / 2 + arrowLength / 2 - holeSize);
        pointD.set(contentWidth / 2 + arrowWidth, contentHeight / 2 + arrowLength / 2);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(pointA.x, pointA.y);
        path.lineTo(pointB.x, pointB.y);
        path.lineTo(pointC.x, pointC.y);
        path.lineTo(pointD.x, pointD.y);
        path.lineTo(pointA.x, pointA.y);

        canvas.rotate(rotation, contentWidth / 2, contentHeight / 2);
        canvas.drawPath(path, paint);
    }
}
