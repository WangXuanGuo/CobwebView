package com.cobwebview.axin.cobwebview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @创建者 王选国
 * @创建时间 2017/5/23 14:49
 * @描述 ${蜘蛛网控件}
 */

public class CobwebView extends View {

    private Paint mPaint;
    private Paint mPaint2;
    private Paint mPaint3;
    private Paint textPaint;
    private float maxValue = 300;     //图形的最大值  默认为300
//    private int minValue = 0;       //图形的最小值  默认为0
    private int griddingNumber = 5; //网格的数量 默认为5
    private int polygonNumber = 6;  //多边形的数量 默认为6
    private int circleRadius = 10;  //圆点半径

    //    TextView

    private Integer[] values;
    private String[] designation;
    private double mUnitRange;  //单位弧度

    public CobwebView(Context context) {
        this(context, null);
    }

    public CobwebView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CobwebView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(SizeUtils.sp2px(context, 12));
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        //初始化
        init();
    }

    private void init() {
        initPaint();
        //        mPaint3.setStrokeWidth(10);
    }

    //初始化画笔
    private void initPaint() {
        //网格画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        //覆盖范围画笔
        mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setColor(Color.BLUE);
        mPaint2.setStyle(Paint.Style.FILL);
        mPaint2.setAlpha(100);
        mPaint2.setStrokeWidth(2);
        //点的画笔
        mPaint3 = new Paint();
        mPaint3.setAntiAlias(true);
        mPaint3.setColor(Color.BLUE);
        mPaint3.setStyle(Paint.Style.FILL);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getWidth() / 2, getHeight() / 2);
        //单位弧度
        mUnitRange = 2 * Math.PI / polygonNumber;

        //画多边形每个顶点到中心的基准线
        drawLine(canvas);

        //蜘蛛网线
        drawPolygon(canvas);

        //画值覆盖的区域以及值上的点
        drawArea(canvas);

        //画文字
        drawText(canvas);


    }

    private void drawLine(Canvas canvas) {
        for (int i = 0; i < polygonNumber; i++) {
            //            int x = (int) (Math.cos(Math.toRadians(i * 60)) * getWidth() / 2);
            //            int y = (int) (Math.sin(Math.toRadians(i * 60)) * getWidth() / 2);
            int x = (int) (Math.cos(i * mUnitRange) * (getWidth() / 2 - 100));
            int y = (int) (Math.sin(i * mUnitRange) * (getWidth() / 2 - 100));

            canvas.drawLine(0, 0, x, y, mPaint);
        }
    }

    private void drawPolygon(Canvas canvas) {
        for (int j = 0; j <= griddingNumber; j++) {
            Path path = new Path();
            for (int i = 0; i < polygonNumber; i++) {
                int x = (int) (Math.cos(i * mUnitRange) * j * (getWidth() / 2 - 100)/ griddingNumber);
                int y = (int) (Math.sin(i * mUnitRange) * j * (getWidth() / 2 - 100)/ griddingNumber);
                if (i == 0) {
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
            }
            path.close();
            canvas.drawPath(path, mPaint);
        }
    }

    private void drawArea(Canvas canvas) {
        if (values != null && values.length == polygonNumber) {
            Path path = new Path();
            float unitValue = (float) (getWidth() / 2 - 100) / maxValue;
            for (int i = 0; i < values.length; i++) {
                int x = (int) (Math.cos(i * mUnitRange) * values[i] * unitValue);
                int y = (int) (Math.sin(i * mUnitRange) * values[i] * unitValue);
                canvas.drawCircle(x, y, circleRadius, mPaint3);
                if (i == 0) {
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
            }
            path.close();
            canvas.drawPath(path, mPaint2);
        }
    }

    private void drawText(Canvas canvas) {
        if (designation != null && designation.length == polygonNumber) {
            for (int i = 0; i < polygonNumber; i++) {
                int x = (int) (Math.cos(i * mUnitRange) * (getWidth() / 2 - 100));
                int y = (int) (Math.sin(i * mUnitRange) * (getWidth() / 2 - 100));
                //            canvas.drawText(designation[i], x, y, textPaint);
                if (mUnitRange * i >= 0 && mUnitRange * i <= Math.PI / 2) {//第4象限
                                        float dis = textPaint.measureText(designation[i]);//文本长度
                    canvas.drawText(designation[i], x, y + dis, textPaint);
                } else if (mUnitRange * i >= 3 * Math.PI / 2 && mUnitRange * i <= Math.PI * 2) {//第3象限
//                    float dis = textPaint.measureText(designation[i]);//文本长度
                    canvas.drawText(designation[i], x, y, textPaint);
                } else if (mUnitRange * i > Math.PI / 2 && mUnitRange * i <= Math.PI) {//第2象限
                    float dis = textPaint.measureText(designation[i]);//文本长度
                    canvas.drawText(designation[i], x - dis, y + dis, textPaint);
                } else if (mUnitRange * i >= Math.PI && mUnitRange * i < 3 * Math.PI / 2) {//第1象限
                    float dis = textPaint.measureText(designation[i]);//文本长度
                    canvas.drawText(designation[i], x - dis, y, textPaint);
                }
            }
        }
    }

    //蜘蛛网图的最大值
    public CobwebView setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        return this;
    }

    /**
     * 多边形的边数量 默认6代表六边形
     * @param polygonNumber 多边形的边数量
     * @return
     */
    public CobwebView setPolygonNumber(int polygonNumber) {
        this.polygonNumber = polygonNumber;
        return this;
    }

    //网格数量

    /**
     * 设置网格数量
     * @param griddingNumber 网格数量
     * @return
     */
    public CobwebView setGriddingNumber(int griddingNumber) {
        this.griddingNumber = griddingNumber;
        return this;
    }

    /**
     * 设置圆点半径
     * @param circleRadius 圆点半径
     * @return
     */
    public CobwebView setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
        return this;
    }

    /**
     * 多边形每个角的名称
     * @param designation 名称的String数组
     * @return
     */
    public CobwebView setDesignation(String[] designation) {
        this.designation = designation;
        return this;
    }

    /**
     * 多边形没变对应的值
     * @param values 值的Integer数组
     * @return
     */
    public CobwebView setValues(Integer[] values) {
        this.values = values;
        return this;
    }

    public void invalidateView() {
        invalidate();
    }
}
