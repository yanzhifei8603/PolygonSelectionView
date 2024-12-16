package com.sjzrbjx.tablayout.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class PolygonSelectionView extends View {
    private Paint Pathpaint;    //画多边形边框的画笔
    private Paint CirclePaint;  //画顶点圆形的画笔
    private Paint TextPaint;  //画顶点标号的画笔
    private int TextSize=24;
    private final static int CircleRadius=30;   //顶点圆的半径
    private final static int InitPolygonSize =500;
    private Path path;  //画多边形的路径
    private List<float[]> Coordinate;   //多边形四个定点坐标列表
    private int  DrawFlag =-1;  //标记是否重新原始画框
    private int IndexFlag =0;   //标记选定的顶点编号
    private int FirstDrawFlag =1;
    private int weight,height;
    public PolygonSelectionView(Context context) {
        super(context);
        Init();
    }

    public PolygonSelectionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Init();
    }

    public PolygonSelectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init();
    }

    public PolygonSelectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Init();
    }

    private void Init() {
        //坐标列表
        Coordinate =new ArrayList<float[]>();

        //边框路径
        path=new Path();

        //边框路径画笔
        Pathpaint =new Paint();
        Pathpaint.setStyle(Paint.Style.STROKE);
        Pathpaint.setAntiAlias(true);
        Pathpaint.setColor(Color.WHITE);
        Pathpaint.setStrokeWidth(5f);

        //顶点拖动圆点画笔
        CirclePaint=new Paint();
        CirclePaint.setStyle(Paint.Style.FILL);
        CirclePaint.setColor(Color.YELLOW);

        //顶点编号画笔
        TextPaint=new Paint();
        TextPaint.setColor(Color.RED);
        TextPaint.setTextSize(24);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(FirstDrawFlag ==1){
            InitPolygon();
            FirstDrawFlag =0;
        }
        canvas.drawPath(path, Pathpaint);
        if(!Coordinate.isEmpty()){
            int i=1;
            for(float[] vertex: Coordinate){
                canvas.drawCircle(vertex[0],vertex[1],CircleRadius, CirclePaint);
                canvas.drawText(String.valueOf(i),vertex[0]-TextSize/3,vertex[1]+TextSize/3,TextPaint);
                i++;
            }
        }
    }

    private void InitPolygon() {
        //显示一个初始化的多边形
        int x=((weight/2)-(InitPolygonSize/2));
        int y=height/2-InitPolygonSize/2;
        Log.d("TAG......", "onSizeChanged: "+x+","+y);
        float[] vertex1= {x,y};
        Coordinate.add(vertex1);
        float[] vertex2= {x+InitPolygonSize,y};
        Coordinate.add(vertex2);
        float[] vertex3= {x+InitPolygonSize,y+InitPolygonSize};
        Coordinate.add(vertex3);
        float[] vertex4= {x,y+InitPolygonSize};
        Coordinate.add(vertex4);
        DrawPolygon();

    }
    private void DrawPolygon(){
        path.reset();
        path.moveTo(Coordinate.get(0)[0], Coordinate.get(0)[1]);
        path.lineTo(Coordinate.get(1)[0], Coordinate.get(1)[1]);
        path.lineTo(Coordinate.get(2)[0], Coordinate.get(2)[1]);
        path.lineTo(Coordinate.get(3)[0], Coordinate.get(3)[1]);
        path.lineTo(Coordinate.get(0)[0], Coordinate.get(0)[1]);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                TouchDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                TouchMove(event);
                break;
            case MotionEvent.ACTION_UP:
                TouchUP(event);
                break;
        }
        invalidate();
        return true;
//        return super.onTouchEvent(event);
    }

    private void TouchUP(MotionEvent event) {
        if(DrawFlag ==1){
            Coordinate.get(IndexFlag)[0]=event.getX();
            Coordinate.get(IndexFlag)[1]=event.getY();
            DrawPolygon();
        }
    }

    private void TouchDown(MotionEvent event) {
        if(!Coordinate.isEmpty()){
            int i=0;
            for(float[] a: Coordinate){
                float x,y;
                x=event.getX();
                y=event.getY();
                if((x>a[0]-CircleRadius*2&&x<a[0]+CircleRadius*2)&&(y>a[1]-CircleRadius*2&&y<a[1]+CircleRadius*2)){
                    DrawFlag =1;
                    IndexFlag =i;
                    break;
                }
                i++;
                DrawFlag =-1;
            }
        }
        if(DrawFlag ==-1){
            path.reset();
            float size= InitPolygonSize;
            float[] g=new float[2];
            float x1=event.getX();
            float y1=event.getY();
            float x2,y2,x3,y3,x4,y4;
            x2=x1+size;
            y2=y1;
            x3=x2;
            y3=y2+size;
            x4=x1;
            y4=y1+size;
            Coordinate.clear();
            g[0]=x1;
            g[1]=y1;
            Coordinate.add(g);
            g=new float[2];
            g[0]=x2;
            g[1]=y2;
            Coordinate.add(g);
            g=new float[2];
            g[0]=x3;
            g[1]=y3;
            Coordinate.add(g);
            g=new float[2];
            g[0]=x4;
            g[1]=y4;
            Coordinate.add(g);
            //画四边形
            path.moveTo(x1,y1);
            path.lineTo(x2,y2);
            path.lineTo(x3,y3);
            path.lineTo(x4,y4);
            path.lineTo(x1,y1);
        }

    }
    private void TouchMove(MotionEvent event) {
        if(DrawFlag ==1){
            Coordinate.get(IndexFlag)[0]=event.getX();
            Coordinate.get(IndexFlag)[1]=event.getY();
            DrawPolygon();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.weight=w;
        this.height=h;
    }

    public List<float[]> getCoordinate() {
        return Coordinate;
    }
}
