package zewd.com.learnamharic.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class DimensionUtils {

    public static int pxToDp(Context context, int pixels) {

        float scale = context.getResources().getDisplayMetrics().density;

        return (int) (pixels * scale + 0.5f);
    }

    public static int getScreenWidth(Context context) {

        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Display display = manager.getDefaultDisplay();

        Point size = new Point();

        display.getSize(size);

        return size.x;
    }
}
