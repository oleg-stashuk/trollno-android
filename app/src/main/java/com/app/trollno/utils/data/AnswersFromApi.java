package com.app.trollno.utils.data;

import com.app.trollno.data.model.user_action.AnswersModel;

import java.util.ArrayList;
import java.util.List;

public class AnswersFromApi {
    private List<AnswersModel.Answers> answerListModel;

    private static volatile AnswersFromApi instance = null;
    public static AnswersFromApi getInstance() {
        if(instance == null) {
            instance = new AnswersFromApi();
        }
        return instance;
    }

    private AnswersFromApi() {
        answerListModel = new ArrayList<>();
    }

    public void saveAnswersInList(List<AnswersModel.Answers> answerList) {
        if (answerListModel.isEmpty()) {
            answerListModel.addAll(answerList);
        } else {
            for (AnswersModel.Answers answer : answerList) {
                if (! answerListModel.contains(answer)) {
                    answerListModel.add(answer);
                }
            }
        }
    }

    public List<AnswersModel.Answers> getAnswerList() {
        return answerListModel;
    }

    public int getListSize() {
        return answerListModel != null ? answerListModel.size() : 0;
    }

    public void removeAllDataFromList(PrefUtils prefUtils) {
        answerListModel.clear();
        prefUtils.saveCurrentPage(0);
    }
}
