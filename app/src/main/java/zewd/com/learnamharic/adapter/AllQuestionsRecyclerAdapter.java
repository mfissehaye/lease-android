package zewd.com.learnamharic.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import zewd.com.learnamharic.QuestionActivity;
import zewd.com.learnamharic.R;
import zewd.com.learnamharic.model.Exam;
import zewd.com.learnamharic.model.ExamSession;
import zewd.com.learnamharic.model.Question;

public class AllQuestionsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ExamSession session;

    private Context context;

    private static final int TYPE_LABEL = 0;

    private static final int TYPE_QUESTION = 1;

    private List<Question> questions;

    public AllQuestionsRecyclerAdapter(Context context, ExamSession session) {

        this.context = context;

        this.session = session;

//        this.questions =  session.getExam().getQuestions();
        this.questions = session.getQuestions();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == TYPE_LABEL) {

            View view = LayoutInflater.from(context).inflate(R.layout.label_in_question_recycler, parent, false);

            return new LabelViewHolder(view);

        } else {

            View view = LayoutInflater.from(context).inflate(R.layout.single_question_in_recycler, parent, false);

            return new QuestionViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final int currentQuestionIndex = session.getCurrent();

        if(holder instanceof QuestionViewHolder) {

            QuestionViewHolder h = (QuestionViewHolder) holder;

            if(currentQuestionIndex == 0) {

                if(position == 1) {

                    position--;

                } else if(position > 2) {

                    position -= 2;
                }

            } else {

                if(position < currentQuestionIndex + 2) {

                    position -= 1;

                } else if(position == currentQuestionIndex + 2) {

                    position -= 2;

                } else {

                    position -= 3;
                }
            }

            h.bind(position);

        } else if(holder instanceof LabelViewHolder) {

            LabelViewHolder h = (LabelViewHolder) holder;

            String labelText;

            if(currentQuestionIndex == 0) {

                if(position == 0) {

                    labelText = context.getString(R.string.current);

                } else {

                    labelText = context.getString(R.string.next_all_caps);
                }
            } else {

                if(position == 0) {

                    labelText = context.getString(R.string.previous_all_caps);

                } else if(position == currentQuestionIndex + 1) {

                    labelText = context.getString(R.string.current);

                } else {

                    labelText = context.getString(R.string.next_all_caps);
                }
            }

            h.labelTextView.setText(labelText);
        }
    }

    @Override
    public int getItemCount() {

        int currentQuestionIndex = session.getCurrent();

        return currentQuestionIndex == 0 || currentQuestionIndex == questions.size() - 1 ? questions.size() + 2 : questions.size() + 3;
    }

    @Override
    public int getItemViewType(int position) {

        int currentQuestionIndex = session.getCurrent();

        if(currentQuestionIndex == 0) {

            if(position == 0 || position == 2) {

                return TYPE_LABEL;

            } else {

                return TYPE_QUESTION;
            }

        } else {

            if(position == 0 || position == currentQuestionIndex + 1 || position == currentQuestionIndex + 3) {

                return TYPE_LABEL;

            } else {

                return TYPE_QUESTION;
            }
        }
    }

    private class QuestionViewHolder extends RecyclerView.ViewHolder {

        private TextView bulletTextView;

        private TextView questionTextView;

        private LinearLayout container;

        private QuestionViewHolder(View itemView) {

            super(itemView);

            container = (LinearLayout) itemView.findViewById(R.id.single_question_container);

            bulletTextView = (TextView) itemView.findViewById(R.id.bullet);

            questionTextView = (TextView) itemView.findViewById(R.id.option_text);
        }

        private void bind(int position) {

            Question question = questions.get(position);

            final int currentQuestionIndex = session.getCurrent();

            bulletTextView.setText(String.format(context.getString(R.string.bullet_text), position + 1));

            container.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    int p = getAdapterPosition();

                    if(currentQuestionIndex == 0) {

                        if(p == 1) {

                            p--;

                        } else if(p > 2) {

                            p -= 2;
                        }

                    } else {

                        if(p < currentQuestionIndex + 2) {

                            p -= 1;

                        } else if(p == currentQuestionIndex + 2) {

                            p -= 2;

                        } else {

                            p -= 3;
                        }
                    }

                    ((QuestionActivity) context).scrollViewPagerToPosition(p + 1);

                    notifyDataSetChanged();
                }
            });

            questionTextView.setText(question.getQuestion());

            resetHighlighted();

            if(question.isAttempted()) {

                highlightAsAttempted();
            }

            if(session.isSubmitted()) {

                if(question.isAttemptCorrect()) {

                    highlightAsCorrect();

                } else {

                    highlightAsWrong();
                }
            }
        }

        private void highlightAsCorrect() {

            container.setBackgroundColor(ContextCompat.getColor(context, R.color.light_green));

            questionTextView.setTextColor(ContextCompat.getColor(context, R.color.darkerBluishGrey));

            bulletTextView.setTextColor(ContextCompat.getColor(context, R.color.darkerBluishGrey));
        }

        private void highlightAsWrong() {

            container.setBackgroundColor(ContextCompat.getColor(context, R.color.light_red));

            questionTextView.setTextColor(ContextCompat.getColor(context, R.color.darkerBluishGrey));

            bulletTextView.setTextColor(ContextCompat.getColor(context, R.color.darkerBluishGrey));
        }

        private void highlightAsAttempted() {

            container.setBackgroundColor(ContextCompat.getColor(context, R.color.mediumBluishGrey));

            questionTextView.setTextColor(ContextCompat.getColor(context, R.color.darkerBluishGrey));

            bulletTextView.setTextColor(ContextCompat.getColor(context, R.color.darkerBluishGrey));
        }

        private void resetHighlighted() {

            container.setBackgroundColor(ContextCompat.getColor(context, R.color.white));

            questionTextView.setTextColor(ContextCompat.getColor(context, R.color.bluishGrey));

            bulletTextView.setTextColor(ContextCompat.getColor(context, R.color.bluishGrey));
        }
    }

    private class LabelViewHolder extends RecyclerView.ViewHolder {

        private TextView labelTextView;

        private LabelViewHolder(View itemView) {

            super(itemView);

            labelTextView = (TextView) itemView.findViewById(R.id.label_text);
        }
    }
}
