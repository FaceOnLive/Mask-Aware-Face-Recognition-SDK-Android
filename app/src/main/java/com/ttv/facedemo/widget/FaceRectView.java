package com.ttv.facedemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.Nullable;

import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.ttv.face.AgeInfo;
import com.ttv.face.FaceAttributeInfo;
import com.ttv.face.GenderInfo;
import com.ttv.face.LivenessInfo;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class FaceRectView extends View {
    private CopyOnWriteArrayList<DrawInfo> drawInfoList = new CopyOnWriteArrayList<>();

    private Paint paint;

    private static final int DEFAULT_FACE_RECT_THICKNESS = 6;

    public FaceRectView(Context context) {
        this(context, null);
    }

    public FaceRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawInfoList != null && drawInfoList.size() > 0) {
            for (int i = 0; i < drawInfoList.size(); i++) {
                drawFaceRect(canvas, drawInfoList.get(i), DEFAULT_FACE_RECT_THICKNESS, paint);
            }
        }
    }

    public void clearFaceInfo() {
        drawInfoList.clear();
        postInvalidate();
    }

    public void addFaceInfo(DrawInfo faceInfo) {
        drawInfoList.add(faceInfo);
        postInvalidate();
    }

    public void addFaceInfo(List<DrawInfo> faceInfoList) {
        drawInfoList.addAll(faceInfoList);
        postInvalidate();
    }

    public void drawRealtimeFaceInfo(List<DrawInfo> drawInfoList) {
        clearFaceInfo();
        if (drawInfoList == null || drawInfoList.size() == 0) {
            return;
        }
        addFaceInfo(drawInfoList);
    }

    public static class DrawInfo {
        private Rect rect;
        private int sex;
        private int age;
        private int maskInfo;
        private int liveness;
        private int color;
        private int isWithinBoundary;
        private String name = null;
        private boolean drawRectInfo;
        private Rect foreheadRect;
        private boolean showAttrInfo;
        private boolean rgbRect;

        public DrawInfo(Rect rect, int sex, int age, int liveness, int color, String name) {
            this.rect = rect;
            this.sex = sex;
            this.age = age;
            this.liveness = liveness;
            this.color = color;
            this.name = name;
        }

        public DrawInfo(Rect rect, int sex, int age, int liveness, int color, String name, int isWithinBoundary, Rect foreheadRect,
                        boolean showAttrInfo, boolean drawRectInfo, boolean rgbRect, int maskInfo) {
            this.rect = rect;
            this.sex = sex;
            this.age = age;
            this.maskInfo = maskInfo;
            this.liveness = liveness;
            this.color = color;
            this.name = name;
            this.isWithinBoundary = isWithinBoundary;
            this.drawRectInfo = drawRectInfo;
            this.foreheadRect = foreheadRect;
            this.showAttrInfo = showAttrInfo;
            this.rgbRect = rgbRect;
        }

        public DrawInfo(DrawInfo drawInfo) {
            if (drawInfo == null) {
                return;
            }
            this.rect = drawInfo.rect;
            this.sex = drawInfo.sex;
            this.age = drawInfo.age;
            this.liveness = drawInfo.liveness;
            this.color = drawInfo.color;
            this.name = drawInfo.name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Rect getRect() {
            return rect;
        }

        public void setRect(Rect rect) {
            this.rect = rect;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getLiveness() {
            return liveness;
        }

        public void setLiveness(int liveness) {
            this.liveness = liveness;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public int getMaskInfo() {return maskInfo;}

        public boolean isDrawRectInfo() {
            return drawRectInfo;
        }

        public void setDrawRectInfo(boolean drawRectInfo) {
            this.drawRectInfo = drawRectInfo;
        }

        public Rect getForeheadRect() {
            return foreheadRect;
        }

        public void setForeheadRect(Rect foreheadRect) {
            this.foreheadRect = foreheadRect;
        }

        public boolean isShowAttrInfo() {
            return showAttrInfo;
        }

        public void setFaceAttributeInfo(boolean showAttrInfo) {
            this.showAttrInfo = showAttrInfo;
        }

        public int getIsWithinBoundary() {
            return isWithinBoundary;
        }

        public void setIsWithinBoundary(int isWithinBoundary) {
            this.isWithinBoundary = isWithinBoundary;
        }
    }

    private static void drawFaceRect(Canvas canvas, FaceRectView.DrawInfo drawInfo, int faceRectThickness, Paint paint) {
        if (canvas == null || drawInfo == null) {
            return;
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(faceRectThickness);
        paint.setColor(drawInfo.getColor());
        paint.setAntiAlias(true);

        Path mPath = new Path();

        Rect rect = drawInfo.getRect();
        mPath.moveTo(rect.left, rect.top + rect.height() / 4);
        mPath.lineTo(rect.left, rect.top);
        mPath.lineTo(rect.left + rect.width() / 4, rect.top);

        mPath.moveTo(rect.right - rect.width() / 4, rect.top);
        mPath.lineTo(rect.right, rect.top);
        mPath.lineTo(rect.right, rect.top + rect.height() / 4);

        mPath.moveTo(rect.right, rect.bottom - rect.height() / 4);
        mPath.lineTo(rect.right, rect.bottom);
        mPath.lineTo(rect.right - rect.width() / 4, rect.bottom);

        mPath.moveTo(rect.left + rect.width() / 4, rect.bottom);
        mPath.lineTo(rect.left, rect.bottom);
        mPath.lineTo(rect.left, rect.bottom - rect.height() / 4);
        canvas.drawPath(mPath, paint);

        paint.setStrokeWidth(1);

        if (drawInfo.getName() == null) {
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setTextSize(rect.width() / 12);
            String str = (drawInfo.getSex() == GenderInfo.MALE ? "MALE" : (drawInfo.getSex() == GenderInfo.FEMALE ? "FEMALE" : "UNKNOWN"))
                    + ","
                    + (drawInfo.getAge() == AgeInfo.UNKNOWN_AGE ? "UNKNOWN" : drawInfo.getAge())
                    + ","
                    + (drawInfo.getLiveness() == LivenessInfo.ALIVE ? "REAL" : (drawInfo.getLiveness() == LivenessInfo.NOT_ALIVE ? "FAKE" : ""));
            canvas.drawText(str, rect.left, rect.top - 10, paint);
        } else {
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setTextSize(rect.width() / 12);
            canvas.drawText(drawInfo.getName(), rect.left, rect.top - 10, paint);
        }

        if (drawInfo.drawRectInfo && drawInfo.rgbRect) {
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            int textSize = rect.width() / 8;
            paint.setStrokeWidth(1);
            paint.setTextSize(textSize);
            int defX = rect.left;
            int defY = rect.bottom + rect.width() / 8;

            String strInfo1 = "Mask: " + (drawInfo.getMaskInfo() == 1 ? "Yes" : "No");
            canvas.drawText(strInfo1, defX, defY, paint);
        }
    }

}