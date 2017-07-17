package zewd.com.learnamharic.adapter;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import zewd.com.learnamharic.R;
import zewd.com.learnamharic.model.Exam;
import zewd.com.learnamharic.utils.PassDownloadManager;

public class ExamsAdapterOld extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_TITLE = 1;

    public static final int VIEW_TYPE_EXAM = 2;

    private final List<String> examCategories;

    private Context context;

    private List<List<Exam>> exams;

    public ExamsAdapterOld(Context context, List<String> examCategories, List<List<Exam>> exams) {

        this.context = context;

        this.examCategories = examCategories;

        this.exams = exams;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view;

        RecyclerView.ViewHolder holder = null;

        switch (viewType) {

            case VIEW_TYPE_TITLE:

                view = inflater.inflate(R.layout.exams_adapter_title, parent, false);

                holder = new TitleViewHolder(view);

                break;

            case VIEW_TYPE_EXAM:

                view = inflater.inflate(R.layout.exams_adapter_exam, parent, false);

                holder = new ExamViewHolder(view);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int positionGroup = 0;

        int examPosition = 1 + position;

        int examsCount = 0;

        for(; positionGroup < exams.size(); positionGroup++) {

            int savedPreviousExamsCount = examsCount;

            examsCount += exams.get(positionGroup).size();

            if(position < examsCount + positionGroup + 1) {

                examPosition = position - (positionGroup + 1 + savedPreviousExamsCount);

                break;
            }
        }

        if (holder instanceof TitleViewHolder) {

            ((TitleViewHolder) holder).bind(examCategories.get(positionGroup));

        } else if (holder instanceof ExamViewHolder) {

            Exam exam = exams.get(positionGroup).get(examPosition);

            ((ExamViewHolder) holder).bind(exam);
        }
    }

    @Override
    public int getItemCount() {

        int count = 0;

        for(int i = 0; i < exams.size(); i++) {

            count += exams.get(i).size();
        }

        return count + examCategories.size();
    }

    @Override
    public int getItemViewType(int position) {

        int type = VIEW_TYPE_EXAM;

        int previousGroupsExamItemsCount = 0;

        for(int i = 0; i < exams.size(); i++) {

            if(position == previousGroupsExamItemsCount + i) {

                type = VIEW_TYPE_TITLE;
            }

            previousGroupsExamItemsCount += exams.size();
        }

        return type;
    }

    public class ExamViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        ImageView icon;

        public ExamViewHolder(View itemView) {

            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.text);

            icon = (ImageView) itemView.findViewById(R.id.icon);
        }

        public void bind(final Exam exam) {

            textView.setText(exam.getTitle());

            Picasso.with(context).load(Constants.BASE_URL + "icon.png").into(icon);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    /*String examFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "pass" + File.separator + exam.getJsonFilePath();

                    if (new File(examFilePath).exists()) {

                        Intent intent = new Intent(context, ExamDetailsActivity.class);

                        intent.putExtra(ExamDetailsActivity.EXAM_TITLE, exam.getTitle());

                        intent.putExtra(ExamDetailsActivity.EXAM_DURATION, exam.getDuration());

                        intent.putExtra(ExamDetailsActivity.EXAM_NUMBER_OF_QUESTIONS, exam.getQuestionCount());

                        intent.putExtra(ExamDetailsActivity.EXAM_INSTRUCTIONS, exam.getInstructions());

//                        String examJsonFilePath = Environment.getExternalStorageDirectory() + File.separator + "pass" + File.separator + exam.getId() + File.separator + "_.json";

                        intent.putExtra(ExamDetailsActivity.EXAM_JSON_FILE_PATH, examFilePath);

                        context.startActivity(intent);

                    } else {

                        new PassDownloadManager(context, "MATH101", receiver).download();
                    }*/
                }
            });
        }
    }

    public class TitleViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;

        private ImageView arrowImageView;

        public TitleViewHolder(View itemView) {

            super(itemView);

            titleTextView = (TextView) itemView.findViewById(R.id.exam_title);

            arrowImageView = (ImageView) itemView.findViewById(R.id.arrow);
        }

        public void bind(String title) {

            titleTextView.setText(title);

            titleTextView.setText(title);

            titleTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition() + 1;

                    int nextLabelIndex = position;

                    for(;

                        ExamsAdapterOld.this.getItemViewType(nextLabelIndex) != VIEW_TYPE_TITLE && nextLabelIndex < getItemCount();

                        nextLabelIndex++) {}

                    notifyItemRangeChanged(position, nextLabelIndex);
                }
            });
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {

                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);

                DownloadManager.Query query = new DownloadManager.Query();

                query.setFilterById(downloadId);

                Cursor c = dm.query(query);

                if(c.moveToFirst()) {

                    String message = "";

                    switch(c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {

                        case DownloadManager.STATUS_SUCCESSFUL:

                            message = "Successfully Downloaded";

                            break;

                        case DownloadManager.STATUS_RUNNING:

                            message = "Download is running";

                            break;

                        case DownloadManager.STATUS_FAILED:

                            message = "Download Failed";

                            break;

                        case DownloadManager.ERROR_CANNOT_RESUME:

                            message = "Could not resume the download";

                            break;

                        case DownloadManager.ERROR_DEVICE_NOT_FOUND:

                            message = "Device could not be found";

                            break;

                        case DownloadManager.ERROR_FILE_ALREADY_EXISTS:

                            message = "File already exists";

                            break;

                        case DownloadManager.ERROR_FILE_ERROR:

                            message = "File error";

                            break;

                        case DownloadManager.ERROR_HTTP_DATA_ERROR:

                            message = "Http Data Error";

                            break;

                        case DownloadManager.ERROR_INSUFFICIENT_SPACE:

                            message = "There is no sufficient space";

                            break;

                        case DownloadManager.ERROR_TOO_MANY_REDIRECTS:

                            message = "Too many redirects";

                            break;

                        case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:

                            message = "Unhandled HTTP Code";

                            break;

                        case DownloadManager.ERROR_UNKNOWN:

                            message = "Unknown error";

                            break;

                        case DownloadManager.PAUSED_QUEUED_FOR_WIFI:

                            message = "Paused queued for Wifi";

                            break;

                        case DownloadManager.PAUSED_UNKNOWN:

                            message = "Paused for unknown reason";

                            break;

                        case DownloadManager.PAUSED_WAITING_TO_RETRY:

                            message = "Paused waiting to retry";

                            break;
                    }

                    Log.d(PassDownloadManager.class.getSimpleName(), message);
                }
            }
        }
    };
}