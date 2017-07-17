package zewd.com.learnamharic.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import zewd.com.learnamharic.R;
import zewd.com.learnamharic.utils.DimensionUtils;

public class ProgressIndicatorView extends RelativeLayout {

    private final View progressingView;

    private final TextView textView;

    private int total;

    private int score;

    private int activeColor;

    private int inactiveColor;

    private Context context;

    public ProgressIndicatorView(Context context) {
        this(context, null);
    }

    public ProgressIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        this.context = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProgressIndicatorView, defStyleAttr, 0);

        total = a.getInteger(R.styleable.ProgressIndicatorView_total, 100);

        activeColor = a.getColor(R.styleable.ProgressIndicatorView_activeColor, ContextCompat.getColor(context, R.color.progressActiveColor));

        inactiveColor = a.getColor(R.styleable.ProgressIndicatorView_inactiveColor, ContextCompat.getColor(context, R.color.progressInactiveColor));

        a.recycle();

        setBackgroundColor(inactiveColor);

        progressingView = new View(context);

        progressingView.setBackgroundColor(activeColor);

        LayoutParams progressingViewParams = new LayoutParams(0, LayoutParams.MATCH_PARENT);

        textView = new TextView(context);

        textView.setTextColor(ContextCompat.getColor(context, R.color.white));

        textView.setText(String.format(context.getString(R.string.progress_text), score, total));

        LayoutParams textParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        textParams.addRule(ALIGN_PARENT_RIGHT);

        textParams.addRule(CENTER_VERTICAL);

        textParams.setMargins(0, 0, DimensionUtils.pxToDp(context, 5), 0);

        addView(progressingView, progressingViewParams);

        addView(textView, textParams);

        setTotal(50);

        setScore(23);
    }

    public void incrementScore() {

        score += score == total ? 0 : 1;

        updateUI();
    }

    public void decrementScore() {

        score -= score == 0 ? 0 : 1;

        updateUI();
    }

    public void setScore(int score) {

        this.score = score > total ? total : score;

        updateUI();
    }

    public void updateUI() {

        int width = (int) Math.ceil((float) score / total * DimensionUtils.getScreenWidth(context));

        progressingView.setLayoutParams(new LayoutParams(width, LayoutParams.MATCH_PARENT));

        textView.setText(String.format(context.getString(R.string.progress_text), score, total));

        invalidate();
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
