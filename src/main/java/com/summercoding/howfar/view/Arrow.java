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

    private float rotation = 0f;

    public Arrow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);

        int size = Math.min(contentHeight, contentWidth);
        int arrowLength = size - size / 4;
        int arrowWidth = size / 6;
        int holeSize = size / 5;

        Point a = new Point(contentWidth / 2, contentHeight / 2 - arrowLength / 2);
        Point b = new Point(contentWidth / 2 - arrowWidth, contentHeight / 2 + arrowLength / 2);
        Point c = new Point(contentWidth / 2, contentHeight / 2 + arrowLength / 2 - holeSize);
        Point d = new Point(contentWidth / 2 + arrowWidth, contentHeight / 2 + arrowLength / 2);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(d.x, d.y);
        path.lineTo(a.x, a.y);

        canvas.rotate(rotation, contentWidth / 2, contentHeight / 2);
        canvas.drawPath(path, paint);
    }
}
