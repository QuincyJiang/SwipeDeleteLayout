package ospace.com.swipedeletelayout;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by jiang on 2017/2/23.
 */

public class SwipeDeleteItem extends FrameLayout {
    private View delete_view;
    private View content_view;
    private int content_view_height;
    private int content_view_width;
    private int delete_view_height;
    private int delete_view_width;
private ViewDragHelper helper;
    private ItemState itemState = ItemState.CLOSE;
    private float startX;
    private float startY;

    public ItemState getItemState() {
        return itemState;
    }

    public SwipeDeleteItem(Context context) {
        super(context);
        init();
    }

    public SwipeDeleteItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }



    public SwipeDeleteItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private  void init(){
        helper = ViewDragHelper.create(this,callback);
    };

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        content_view = getChildAt(0);
        delete_view = getChildAt(1);
    }
             /*
                此方法在onmeasure方法执行完毕后调用，可用来话获取view宽高
              */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w,h,oldw,oldh);
        content_view_height = content_view.getMeasuredHeight();
        content_view_width = content_view.getMeasuredWidth();
        delete_view_height = delete_view.getMeasuredHeight();
        delete_view_width = delete_view.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        content_view.layout(0,0,content_view_width,content_view_height);
       delete_view.layout(content_view.getRight(),0,content_view.getRight()+delete_view_width,delete_view_height);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
if(!SwipeLayoutManager.getInstance().isShouldSwipe(SwipeDeleteItem.this)){
    SwipeLayoutManager.getInstance().closeCurrentLayout();
    requestDisallowInterceptTouchEvent(true);
    return  true;
}
       switch (event.getAction()){
           case MotionEvent.ACTION_DOWN:
               startX = event.getX();
               startY = event.getY();
               break;
           case MotionEvent.ACTION_MOVE:
               float moveX = event.getX();
               float moveY  = event.getY();
               float deltaX = moveX-startX;
               float deltaY  = moveY-startY;
               startX = event.getX();
               startY = event.getY();
               /*
               如果横向移动更多 请求父控件不要拦截触摸事件
                */
               if(Math.abs(deltaX)>Math.abs(deltaY)){
                   requestDisallowInterceptTouchEvent(true);

               }

           break;
       }
        helper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(!SwipeLayoutManager.getInstance().isShouldSwipe(SwipeDeleteItem.this)){
            return true;
        }
        return helper.shouldInterceptTouchEvent(ev);

    }

    private  ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child==content_view||child==delete_view;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return delete_view_width;
        }


        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if(child==content_view){
                if(left>0){
                    left=0;
                }else if(left<-delete_view_width){
                    left = -delete_view_width;
                }
            }else if(child==delete_view){
                if(left>content_view_width){
                    left=content_view_width;
                }else if(left<content_view_width-delete_view_width){
                    left = content_view_width-delete_view_width;
                }
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if(changedView==content_view){
                delete_view.layout(delete_view.getLeft()+dx,delete_view.getTop()+dy,
                        delete_view.getRight()+dx,delete_view.getBottom()+dy);
            }else if(changedView==delete_view){
                content_view.layout(content_view.getLeft()+dx,content_view.getTop()+dy,
                        content_view.getRight()+dx,content_view.getBottom()+dy);
            };
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
           if(content_view.getLeft()>-delete_view_width/2){
               close();

           }else{
               open();

               SwipeLayoutManager.getInstance().setSwipeDeleteItem(SwipeDeleteItem.this);
           }
        }
    };

    public void close(){
        itemState=ItemState.CLOSE;
        SwipeLayoutManager.getInstance().clearCurrentLayout();

        helper.smoothSlideViewTo(content_view,0,content_view.getTop());
       ViewCompat.postInvalidateOnAnimation(SwipeDeleteItem.this);
    }
    public void open(){
        itemState=ItemState.OPEN;
helper.smoothSlideViewTo(content_view,-delete_view_width,content_view.getTop());
        ViewCompat.postInvalidateOnAnimation(SwipeDeleteItem.this);
    }

    @Override
    public void computeScroll() {
        if(helper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(SwipeDeleteItem.this);
        }
    }
}
