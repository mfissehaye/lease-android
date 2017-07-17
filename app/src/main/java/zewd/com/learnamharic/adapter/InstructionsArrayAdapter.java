package zewd.com.learnamharic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import zewd.com.learnamharic.R;

public class InstructionsArrayAdapter extends ArrayAdapter<String> {

    private final String[] instructions;

    private final Context context;

    public InstructionsArrayAdapter(Context context, String[] items) {

        super(context, R.layout.instructions_list_item, R.id.text);

        this.context = context;

        this.instructions = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view;

        if(convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.instructions_list_item, parent, false);

            TextView instructionText = (TextView) view.findViewById(R.id.text);

            String instruction = instructions[position];

            instructionText.setText(instruction);

        } else {

            view = convertView;
        }

        return  view;
    }

    @Override
    public int getCount() {
        return instructions.length;
    }
}
