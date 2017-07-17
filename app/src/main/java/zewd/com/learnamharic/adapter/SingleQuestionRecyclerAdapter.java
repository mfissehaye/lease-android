package zewd.com.learnamharic.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;
import zewd.com.learnamharic.R;
import zewd.com.learnamharic.model.Exam;
import zewd.com.learnamharic.model.ExamSession;
import zewd.com.learnamharic.model.Question;

public class SingleQuestionRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int TYPE_QUESTION = 1;

    private final static int TYPE_OPTION = 2;

    private final static int TYPE_BUTTONS = 3;

    private Question question;

    private int itemCount;

    Context context;

    private ExamSession session;

    public SingleQuestionRecyclerAdapter(Context context, Question question) throws ExamSession.ExamDoesNotExistException {

        this.context = context;

        this.question = question;

        this.itemCount = question.isExplained() ? 3 : question.getOptions().size() + 2;

        this.session = ExamSession.getInstance(context, question.getExamId());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        RecyclerView.ViewHolder holder = null;

        switch (viewType) {

            case TYPE_QUESTION:

                view = LayoutInflater.from(context).inflate(R.layout.layout_question_text, parent, false);

                holder = new QuestionViewHolder(view);

                break;

            case TYPE_OPTION:

                view = LayoutInflater.from(context).inflate(R.layout.layout_question_option, parent, false);

                holder = new OptionViewHolder(view);

                break;

            case TYPE_BUTTONS:

                view = LayoutInflater.from(context).inflate(R.layout.layout_question_buttons, parent, false);

                holder = new ButtonsViewHolder(view);

                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof OptionViewHolder) {

            position--;

            OptionViewHolder h = (OptionViewHolder) holder;

            h.bind(position);

        } else if(holder instanceof QuestionViewHolder) {

            QuestionViewHolder h = (QuestionViewHolder) holder;

            h.bind(question);

        } else if(holder instanceof ButtonsViewHolder) {

            final ButtonsViewHolder h = (ButtonsViewHolder) holder;

            h.bind();
        }
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {

        if(position == 0) {

            return TYPE_QUESTION;

        } else if(position == itemCount - 1) {

            return TYPE_BUTTONS;

        } else {

            return TYPE_OPTION;
        }
    }

    private class OptionViewHolder extends RecyclerView.ViewHolder {

        TextView optionTextView;

        TextView bulletView;

        ImageView checkMark;

        View view;

        OptionViewHolder(View itemView) {

            super(itemView);

            view = itemView;

            optionTextView = (TextView) itemView.findViewById(R.id.option_text);

            bulletView = (TextView) itemView.findViewById(R.id.bullet);

            checkMark = (ImageView) itemView.findViewById(R.id.checkmark);
        }

        public void bind(int position) {

            bulletView.setText(position >= 0 && position < 27 ? String.valueOf((char) (position + 65)) : "");

            bulletView.setBackground(ContextCompat.getDrawable(context, R.drawable.circle_background_white));

            bulletView.setTextColor(ContextCompat.getColor(context, R.color.blueBlack));

            optionTextView.setText(question.getOptions().get(position));

            checkMark.setVisibility(View.INVISIBLE);

            List<Integer> attemptedOptionIndices = question.getAttemptedOptionsIndices();

            List<Integer> correctOptionIndices = question.getCorrectOptionIndices();

            if (question.isAttempted() && attemptedOptionIndices.contains(position)) {

                bulletView.setBackground(ContextCompat.getDrawable(context, R.drawable.circle_background_dark));

                bulletView.setTextColor(ContextCompat.getColor(context, R.color.white));
            }

            if (itemCount == 3 || (session.isSubmitted() && correctOptionIndices.contains(position))) {

                bulletView.setBackground(ContextCompat.getDrawable(context, R.drawable.circle_background_green));

                bulletView.setTextColor(ContextCompat.getColor(context, R.color.white));

                optionTextView.setTextColor(ContextCompat.getColor(context, R.color.blueBlack));

                checkMark.setImageResource(R.drawable.ic_check_black_24dp);

                checkMark.setColorFilter(ContextCompat.getColor(context, R.color.green));

                bulletView.setText(position >= 0 && position < 27 ? String.valueOf((char) (position + 65)) : "");

                checkMark.setVisibility(View.VISIBLE);

                optionTextView.setText(question.getOptions().get(position));

            } else if (session.isSubmitted() && !question.isAttemptCorrect() && attemptedOptionIndices.contains(position)) {

                bulletView.setBackground(ContextCompat.getDrawable(context, R.drawable.circle_background_red));

                bulletView.setTextColor(ContextCompat.getColor(context, R.color.white));

                checkMark.setImageResource(R.drawable.ic_clear_black_24dp);

                checkMark.setColorFilter(ContextCompat.getColor(context, R.color.red));

                checkMark.setVisibility(View.VISIBLE);

            } else if (session.isSubmitted()) {

                bulletView.setTextColor(ContextCompat.getColor(context, R.color.bluishGrey));

                optionTextView.setTextColor(ContextCompat.getColor(context, R.color.bluishGrey));
            }

            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if(!session.isSubmitted()) {

                        int clickedPosition = getAdapterPosition() - 1;

                        List<Integer> attemptedOptionIndices = question.getAttemptedOptionsIndices();

                        if(question.getNumberOfCorrectOptions() > 1) {

                            if (attemptedOptionIndices.contains(clickedPosition)) {

                                attemptedOptionIndices.remove(new Integer(clickedPosition));

                            } else {

                                attemptedOptionIndices.add(clickedPosition);
                            }

                        } else {

                            attemptedOptionIndices.clear();

                            attemptedOptionIndices.add(clickedPosition);
                        }

                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    private class QuestionViewHolder extends RecyclerView.ViewHolder {

        TextView questionTextView;

        ImageView playAudioButton;

        ImageView questionImageView;

        QuestionViewHolder(View itemView) {

            super(itemView);

            questionTextView = (TextView) itemView.findViewById(R.id.question_text);

            playAudioButton = (ImageView) itemView.findViewById(R.id.play_audio);

            questionImageView = (ImageView) itemView.findViewById(R.id.question_image);
        }

        private void bind(Question question) {

            questionTextView.setText(question.getQuestion());

            questionImageView.setVisibility(View.GONE);

            if(question.hasImage()) {

                String imagePath = Environment.getExternalStorageDirectory() + File.separator + "pass" + File.separator + question.getId();

                String[] supportedFormats = new String[] { ".jpg", ".png", ".jpeg" };

                for(int i = 0; i < supportedFormats.length; i++) {

                    imagePath += supportedFormats[i];

                    if ((new File(imagePath)).exists()) {

                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

                        questionImageView.setImageBitmap(bitmap);

                        questionImageView.setVisibility(View.VISIBLE);

                        break;
                    }
                }
            }

            if(question.isAudible()) {

                String audioPath = Environment.getExternalStorageDirectory() + File.separator + "pass" + File.separator + question.getId();

                String[] supportedFormats = new String[] { ".mp3", ".wav", ".ogg" };

                for(int i = 0; i < supportedFormats.length; i++) {

                    audioPath += supportedFormats[i];

                    if ((new File(audioPath)).exists()) {

                        playAudioButton.setVisibility(View.VISIBLE);

                        final MediaPlayer mp = MediaPlayer.create(context, Uri.parse(audioPath));

                        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {

                                playAudioButton.setImageResource(R.drawable.ic_volume_up_black_24dp);
                            }
                        });

                        playAudioButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                if(mp.isPlaying()) {

                                    mp.pause();

                                    playAudioButton.setImageResource(R.drawable.ic_volume_up_black_24dp);

                                } else {

                                    playAudioButton.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

                                    mp.start();
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    private class ButtonsViewHolder extends RecyclerView.ViewHolder {

        TextView explainButton;

        TextView explanationTextView;

        public ButtonsViewHolder(View itemView) {

            super(itemView);

            explainButton = (TextView) itemView.findViewById(R.id.show_explanation_button);

            explanationTextView = (TextView) itemView.findViewById(R.id.explanation);
        }

        public void bind() {

            explanationTextView.setText(question.getExplanation());

            if(question.isExplained()) {

                explainButton.setVisibility(View.GONE);

                explanationTextView.setVisibility(View.VISIBLE);

            } else if(session.isSubmitted()) {

                explainButton.setVisibility(View.VISIBLE);
            }

            explainButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    question.setExplained();

                    explainButton.setVisibility(View.GONE);

                    explanationTextView.setVisibility(View.VISIBLE);

                    itemCount = 3;

                    notifyDataSetChanged();
                }
            });
        }
    }
}
