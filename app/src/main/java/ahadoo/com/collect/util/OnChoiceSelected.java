package ahadoo.com.collect.util;

import java.util.List;

import ahadoo.com.collect.model.SurveyChoice;

public interface OnChoiceSelected {
    void choicesSelected(List<SurveyChoice> choices);
}
