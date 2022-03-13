package com.team28.qlnhathuoc.utils;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

public class AutoCompleteEditText extends androidx.appcompat.widget.AppCompatAutoCompleteTextView {
    public AutoCompleteEditText(Context context) {
        super(context);
    }

    public AutoCompleteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoCompleteEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused && getFilter() != null) {
            performFiltering(getText(), 0);
        }
    }

}
