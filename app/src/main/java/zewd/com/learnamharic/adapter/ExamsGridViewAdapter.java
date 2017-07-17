package zewd.com.learnamharic.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.util.List;

import zewd.com.learnamharic.R;
import zewd.com.learnamharic.model.Exam;

public class ExamsGridViewAdapter extends BaseAdapter {

    private List<Exam> exams;

    private Context context;

    public ExamsGridViewAdapter(Context context, List<Exam> exams) {
        this.exams = exams;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.exams.size();
    }

    @Override
    public Object getItem(int i) {
        return exams.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View convertView;

        if(view == null) {

            Exam exam = exams.get(i);

            convertView = inflater.inflate(R.layout.layout_grid_adapter_single, null);

            TextView textView = (TextView) convertView.findViewById(R.id.text);

            ImageView icon = (ImageView) convertView.findViewById(R.id.icon);

            textView.setText(exam.getTitle());

            String iconPath = Environment.getExternalStorageDirectory() + File.separator + "pass" + File.separator + exam.getIconFileURL();

            if((new File(iconPath)).exists()) {

                Bitmap bitmap = BitmapFactory.decodeFile(iconPath);

                icon.setImageBitmap(bitmap);
            }

        } else {

            convertView = view;
        }

        return convertView;
    }
}
