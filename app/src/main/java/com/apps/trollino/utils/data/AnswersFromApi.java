package com.apps.trollino.utils.data;

import com.apps.trollino.data.model.comment.CommentModel;

import java.util.ArrayList;
import java.util.List;

public class AnswersFromApi {
    private List<CommentModel.Comments> answerListModel;

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

    public void saveAnswersInList(List<CommentModel.Comments> commentList) {
        if (answerListModel.isEmpty()) {
            answerListModel.addAll(commentList);
        } else {
            for (CommentModel.Comments comment : commentList) {
                if (! answerListModel.contains(comment)) {
                    answerListModel.add(comment);
                }
            }
        }
    }

    public List<CommentModel.Comments> getAnswerList() {
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
