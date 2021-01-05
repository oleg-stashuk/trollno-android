package com.apps.trollino.utils.data;

import com.apps.trollino.data.model.comment.CommentModel;

import java.util.ArrayList;
import java.util.List;

public class CommentListToUserActivityFromApi {
    private List<CommentModel.Comments> commentListModel;

    private static volatile CommentListToUserActivityFromApi instance = null;
    public static CommentListToUserActivityFromApi getInstance() {
        if(instance == null) {
            instance = new CommentListToUserActivityFromApi();
        }
        return instance;
    }

    private CommentListToUserActivityFromApi() {
        commentListModel = new ArrayList<>();
    }

    public void saveCommentInList(List<CommentModel.Comments> commentList) {
        if (commentListModel.isEmpty()) {
            commentListModel.addAll(commentList);
        } else {
            for (CommentModel.Comments comment : commentList) {
                if (! commentListModel.contains(comment)) {
                    commentListModel.add(comment);
                }
            }
        }
    }

    public List<CommentModel.Comments> getCommentList() {
        return commentListModel;
    }

    public void removeAllDataFromList(PrefUtils prefUtils) {
        commentListModel.clear();
        prefUtils.saveCurrentPage(0);
    }
}
