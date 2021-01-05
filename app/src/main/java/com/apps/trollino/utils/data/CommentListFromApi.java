package com.apps.trollino.utils.data;

import com.apps.trollino.data.model.comment.CommentModel;

import java.util.ArrayList;
import java.util.List;

public class CommentListFromApi {
    private List<CommentModel.Comments> commentListModel;

    private static volatile CommentListFromApi instance = null;
    public static CommentListFromApi getInstance() {
        if(instance == null) {
            instance = new CommentListFromApi();
        }
        return instance;
    }

    private CommentListFromApi() {
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
