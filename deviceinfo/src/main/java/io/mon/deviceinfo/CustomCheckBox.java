package io.mon.deviceinfo;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by Son on 2/11/2017.
 */

public class CustomCheckBox extends RelativeLayout implements inCheckBoxCustom {

    private LayoutInflater mInflater;
    Context context;
    View views;
    private ProgressBar pro_memory;
    private TextView tvUsed;
    private TextView tvAll;
    private LinearLayout layout_info;
    private ImageView imgIcon;
    private TextView tvTitle;
    private ImageView imgCheck;
    private ImageView imgText;
    private FrameLayout btnCheck;

    public CustomCheckBox(Context context) {
        super(context);
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public CustomCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mInflater = LayoutInflater.from(context);
        init(attrs);
    }

    public CustomCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mInflater = LayoutInflater.from(context);
        init(attrs);
    }

    boolean ischecked = false;
    int icon;
    int iconoff;

    public void init(AttributeSet attrs) {
        TypedArray attr = getContext().obtainStyledAttributes(attrs, R.styleable.customCheckbox);
        icon = attr.getResourceId(R.styleable.customCheckbox_imgTextOn, 0);
        iconoff = attr.getResourceId(R.styleable.customCheckbox_imgTextOff, 0);
        views = mInflater.inflate(R.layout.layout_custom_checkbox, this, true);
        imgIcon = (ImageView) views.findViewById(R.id.imgText);
        imgCheck = (ImageView) views.findViewById(R.id.imgCheck);
        imgIcon.setImageResource(iconoff);
        btnCheck = (FrameLayout) views.findViewById(R.id.btnCheck);
        imgCheck.setVisibility(GONE);
        btnCheck.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ischecked) {
                    imgCheck.setVisibility(GONE);
                    ischecked = false;
                    imgIcon.setImageResource(iconoff);
                } else {
                    imgIcon.setImageResource(icon);
                    ischecked = true;
                    imgCheck.setVisibility(VISIBLE);
                }
            }
        });

    }

    @Override
    public void setChecked(boolean checked) {
        ischecked = checked;
        if (checked) {
            imgCheck.setVisibility(VISIBLE);
            imgIcon.setImageResource(icon);
        } else {
            imgIcon.setImageResource(iconoff);
            imgCheck.setVisibility(GONE);
        }
    }

    @Override
    public boolean getChecked() {
        return ischecked;
    }

    @Override
    public void isChecked() {

    }
}
