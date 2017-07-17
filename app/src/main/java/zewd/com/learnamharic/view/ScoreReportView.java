package zewd.com.learnamharic.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import zewd.com.learnamharic.R;

public class ScoreReportView extends LinearLayout {

    String percentages = "33,33,33";

    public ScoreReportView(Context context) {
        this(context, null);
    }

    public ScoreReportView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScoreReportView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScoreReportView, defStyleAttr, 0);

        percentages = a.getString(R.styleable.ScoreReportView_percentages);

        String[] splittedArray = TextUtils.split(percentages, ",");

        a.recycle();

        int[] backgrounds = new int[] {

                R.drawable.big_button_background_green_rounded_left,

                R.drawable.big_button_background_blue_black,

                R.drawable.big_button_background_light_blue_black_rounded_right };

        for(int i = 0; i < splittedArray.length; i++) {

            int percentage = Integer.parseInt(splittedArray[i]);

            View v = new View(context);

            v.setBackground(ContextCompat.getDrawable(context, backgrounds[ i % backgrounds.length ]));

            LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);

            params.weight = percentage;

            addView(v, params);
        }
    }
}
