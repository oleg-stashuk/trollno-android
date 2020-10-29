package com.apps.trollino.model;

import com.apps.trollino.R;

import java.util.ArrayList;
import java.util.List;

public class UserCommentActivityModel {
    private boolean isReadComment;
    private String title;
    private String comment;
    private String likeCount;
    private boolean isHasNewComment;
    private String timeUserComment;

    private int userImage;
    private String userName;
    private boolean isUserLikeIt;
    private boolean isCommentHasAnswer;

    public UserCommentActivityModel(boolean isReadUserComment, String titleUserComment, String commentUserComment, String likeCountUserComment, boolean isHasNewCommentUserComment, String timeUserComment) {
        this.isReadComment = isReadUserComment;
        this.title = titleUserComment;
        this.comment = commentUserComment;
        this.likeCount = likeCountUserComment;
        this.isHasNewComment = isHasNewCommentUserComment;
        this.timeUserComment = timeUserComment;
    }

    public boolean isReadUserComment() {
        return isReadComment;
    }

    public String getTitle() {
        return title;
    }

    public String getComment() {
        return comment;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public boolean isHasNewComment() {
        return isHasNewComment;
    }

    public String getTime() {
        return timeUserComment;
    }


    public static List<UserCommentActivityModel> makeUserCommentList() {
        List<UserCommentActivityModel> userCommentList = new ArrayList<>();
        userCommentList.add(new UserCommentActivityModel(false, "Заголовок_1", "Комментарий_1", "+5",
                true, "12 ч. назад"));

        userCommentList.add(new UserCommentActivityModel(false, "Заголовок_2", "Комментарий_2", "+100",
                true, "1 д. назад"));

        userCommentList.add(new UserCommentActivityModel(true, "Заголовок_3", "Комментарий_3", "",
                false, "7 д. назад"));


        return userCommentList;
    }



    public UserCommentActivityModel(int userImage, String userName, String time, String comment, boolean isUserLikeIt, String likeCount, boolean isCommentHasAnswer) {
        this.userImage = userImage;
        this.userName = userName;
        this.comment = comment;
        this.isUserLikeIt = isUserLikeIt;
        this.likeCount = likeCount;
        this.timeUserComment = time;
        this.isCommentHasAnswer = isCommentHasAnswer;
    }



    public static List<UserCommentActivityModel> makeCommentsListToPostParent() {
        List<UserCommentActivityModel> commentsListToPost = new ArrayList<>();
        String comment = "I absolutely adore the original Hairspray (RIP Divine), and the stage version was great...but the movie suffered from some less-than-stellar casting choices, especially when it came to Tracey’s parents. John Travolta just didn’t bring the energy that either Divine did in the original or Harvey Fierstein did on stage, and he had zero chemistry with Christopher Walken. As a result, “Timeless to Me” felt incredibly insincere. The movie was still good, but it could have been so much better IMHO.";
        String comment2 = "I'm with AvalonAngel, Travolta was the worst in Hairspray. I spend the whole movie thinking it was just Travolta in a fat suit & drag.When it's remade they definitely should have a real drag queen play Edna.";
        String comment3 = "I love both but Coraline has a great book too so I voted for it.";

        commentsListToPost.add(new UserCommentActivityModel(R.drawable.ic_person, "Василий", "1 ч. назад", comment,
                true, "14", false));
        commentsListToPost.add(new UserCommentActivityModel(R.drawable.ic_person, "Иван", "3 ч. назад", comment2,
                false, "2", true));
        commentsListToPost.add(new UserCommentActivityModel(R.drawable.ic_person, "Николай", "7 ч. назад", comment3,
                false, "200", true));

        return commentsListToPost;
    }


    public static List<UserCommentActivityModel> makeCommentsListToPostChild() {
        List<UserCommentActivityModel> commentsListToPost = new ArrayList<>();
        String comment = "I love both but Coraline has a great book too so I voted for it.";
        String comment2 = "true life is such a masterpiece!";
        String comment3 = "LOL. The first couple of episodes of my Sweet 16 were so outrageous and I got hooked, but after about 4 episodes, it was all just the same. True Life was so much better.";

        commentsListToPost.add(new UserCommentActivityModel(R.drawable.ic_person, "Даша", "12 ч. назад", comment,
                true, "3", false));
        commentsListToPost.add(new UserCommentActivityModel(R.drawable.ic_person, "Маша", "12 ч. назад", comment2,
                false, "5", true));
        commentsListToPost.add(new UserCommentActivityModel(R.drawable.ic_person, "Иван", "12 ч. назад", comment3,
                false, "20", true));

        return commentsListToPost;
    }

    public boolean isReadComment() {
        return isReadComment;
    }

    public String getTimeUserComment() {
        return timeUserComment;
    }

    public int getUserImage() {
        return userImage;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isUserLikeIt() {
        return isUserLikeIt;
    }

    public boolean isCommentHasAnswer() {
        return isCommentHasAnswer;
    }
}
