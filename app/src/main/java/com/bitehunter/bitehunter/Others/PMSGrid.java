package com.bitehunter.bitehunter.Others;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by Pasan .M. Semage on 2018-01-19.
 */

public class PMSGrid extends GridView {

    boolean expanded = false;

    public PMSGrid(Context context) {
        super(context);
    }

    public PMSGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PMSGrid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isExpanded(){
        return expanded;
    }
    public void setExpanded(boolean expended){
        this.expanded = expended;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isExpanded())
        {
            int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        }
        else
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
