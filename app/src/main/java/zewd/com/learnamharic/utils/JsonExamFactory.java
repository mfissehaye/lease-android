package zewd.com.learnamharic.utils;

import android.content.Context;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import zewd.com.learnamharic.model.Exam;
import zewd.com.learnamharic.model.ExamSession;
import zewd.com.learnamharic.model.Question;

public class JsonExamFactory implements ExamFactory {

    public static final File appDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "pass");

    private static final String ID_LABEL = "id";

    private static final String TITLE_LABEL = "title";

    private static final String TIME_LABEL = "time";

    private static final String QUESTIONS_LABEL = "questions";

    private static final String TEXT_LABEL = "text";

    private static final String OPTIONS_LABEL = "options";

    private static final String CORRECT_OPTION_INDICES_LABEL = "correct-option-indices";

    private static final String HAS_IMAGE_LABEL = "has-image";

    private static final String IS_AUDIBLE_LABEL = "audible";

    private static final String YEAR_LABEL = "year";

    private static final String EXPLANATION_LABEL = "explanation";

    protected String jsonFilePath;

    private String zipFilePath;

    public JsonExamFactory(String examId) throws ExamSession.ExamDoesNotExistException {

        jsonFilePath = appDir + File.separator + examId + ".json";

        zipFilePath = appDir + File.separator + examId + ".zip";

        if (!(new File(zipFilePath)).exists()) {

            throw new ExamSession.ExamDoesNotExistException();
        }
    }

    protected Exam processJson(String json) throws JSONException {

        Exam exam = new Exam();

        JSONObject obj = new JSONObject(json);

        int id = obj.getInt(ID_LABEL);

        String title = obj.getString(TITLE_LABEL);

        int time = obj.getInt(TIME_LABEL);

        int year = obj.getInt(YEAR_LABEL);

        JSONArray questionsArray = obj.getJSONArray(QUESTIONS_LABEL);

        List<Question> questions = new ArrayList<>();

        for (int i = 0; i < questionsArray.length(); i++) {

            JSONObject questionObject = questionsArray.getJSONObject(i);

            int questionId = questionObject.getInt(ID_LABEL);

            String questionText = questionObject.getString(TEXT_LABEL);

            String explanation = questionObject.getString(EXPLANATION_LABEL);

            JSONArray correctOptionIndicesJsonArray = questionObject.getJSONArray(CORRECT_OPTION_INDICES_LABEL);

            List<Integer> correctOptionIndices = new ArrayList<>();

            for (int k = 0; k < correctOptionIndicesJsonArray.length(); k++) {

                correctOptionIndices.add(correctOptionIndicesJsonArray.getInt(0));
            }

            boolean hasImage = questionObject.getBoolean(HAS_IMAGE_LABEL);

            boolean isAudible = questionObject.getBoolean(IS_AUDIBLE_LABEL);

            JSONArray optionsArray = questionObject.getJSONArray(OPTIONS_LABEL);

            List<String> options = new ArrayList<>();

            for (int j = 0; j < optionsArray.length(); j++) {

                JSONObject optionObject = optionsArray.getJSONObject(j);

                int optionId = optionObject.getInt(ID_LABEL);

                String optionText = optionObject.getString(TEXT_LABEL);

                options.add(optionText);
            }

            Question question = new Question();

            question.setQuestion(questionText);

            question.setOptions(options);

            question.setCorrectOptionIndices(correctOptionIndices);

            question.setHasImage(hasImage);

            question.setAudible(isAudible);

            question.setExplanation(explanation);

            questions.add(question);
        }

        exam.setTitle(title);

        exam.setDuration(time);

//        exam.setQuestions(questions);

        exam.setYearTag(year);

        return exam;
    }

    public Exam getExamFromZip() throws IOException, ExamSession.ExamDoesNotExistException {

        String json = null;

        ZipFile zipFile = new ZipFile(new File(zipFilePath).getAbsolutePath());

        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath));

        ZipEntry entry;

        while ((entry = zis.getNextEntry()) != null) {

            String filePath = Environment.getExternalStorageDirectory() + File.separator + "pass" + File.separator + entry.getName();

            if((new File(filePath)).exists()) {

                continue;
            }

            int bufferSize = 2048;

            byte buffer[] = new byte[bufferSize];

            int count;

            InputStream entryZis = zipFile.getInputStream(entry);

            if (entry.getName().endsWith(".jpg") || entry.getName().endsWith(".mp3")) {

                FileOutputStream os = new FileOutputStream(filePath);

                BufferedOutputStream dest = new BufferedOutputStream(os, bufferSize);

                while ((count = entryZis.read(buffer, 0, bufferSize)) != -1) {

                    dest.write(buffer, 0, count);
                }

                dest.flush();

                dest.close();

            } else if (entry.getName().endsWith(".json")) {

                Scanner s = new Scanner(entryZis).useDelimiter("\\A");

                json = s.hasNext() ? s.next() : "";
            }
        }

        zis.close();

        try {

            return processJson(json);

        } catch (JSONException e) {

            e.printStackTrace();

            throw new ExamSession.ExamDoesNotExistException();
        }
    }

    public String getJson(Context context) {

        String json = null;

        try {

            InputStream is = new FileInputStream(new File(jsonFilePath));

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");

        } catch (IOException e) {

            e.printStackTrace();
        }

        return json;
    }

    @Override
    public Exam getExam(Context context) {

        try {

            return processJson(getJson(context));

        } catch (JSONException e) {

            e.printStackTrace();
        }
        return null;
    }
}
