package com.apps.trollino.data.model.comment;

import com.apps.trollino.R;
import com.apps.trollino.data.model.PagerModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CommentModel {

    @SerializedName("pager")
    @Expose
    private PagerModel pagerModel;

/*
"{
  ""rows"": [
    {
        ""cid"": ""1"",
        ""pid"": """",
        ""subject"": ""hgf hjghjf j kjg jk   jhgkjg…"",
        ""comment_body"": ""hgf hjghjf j kjg jk   jhgkjg jh jhg"",
        ""thread"": ""01/"",
        ""changed"": ""пт, 10/09/2020 - 16:10"",
        ""flagged"": ""0"",
        ""count"": ""0""
    },
    {
        ""cid"": ""29"",
        ""pid"": ""17"",
        ""subject"": """",
        ""comment_body"": ""Ответ на комментарий. Данное значение не должно быть пустым"",
        ""thread"": ""0a.00/"",
        ""changed"": ""пн, 10/12/2020 - 16:54"",
        ""flagged"": ""0"",
        ""count"": ""0"",
        ""uid"": ""70"",
        ""name"": ""qa-restuser"",
        ""field_user_picture"": ""/sites/default/files/allow_avatars/woman-6.jpg"",
        ""newsappm_comment_answers_count"": ""0""
    },
    {
        ""cid"": ""3"",
        ""pid"": """",
        ""subject"": """",
        ""comment_body"": ""<p>Данное значение не должно быть пустым</p>\n"",
        ""thread"": ""03/"",
        ""changed"": ""чт, 09/24/2020 - 16:47""
    },
    {
        ""cid"": ""4"",
        ""pid"": ""2"",
        ""subject"": ""Sooo cool"",
        ""comment_body"": ""<p>Sooo cool</p>"",
        ""thread"": ""02.00/"",
        ""changed"": ""чт, 09/24/2020 - 16:49""
    },
    {
        ""cid"": ""5"",
        ""pid"": """",
        ""subject"": """",
        ""comment_body"": ""<p>Данное значение не должно быть пустым</p>\n"",
        ""thread"": ""04/"",
        ""changed"": ""чт, 09/24/2020 - 16:57""
    },
    {
        ""cid"": ""6"",
        ""pid"": ""2"",
        ""subject"": """",
        ""comment_body"": ""<p>Данное значение не должно быть пустым</p>\n"",
        ""thread"": ""02.01/"",
        ""changed"": ""чт, 09/24/2020 - 16:57""
    },
    {
        ""cid"": ""7"",
        ""pid"": ""4"",
        ""subject"": """",
        ""comment_body"": ""<p>Данное значение не должно быть пустым</p>\n"",
        ""thread"": ""02.00.00/"",
        ""changed"": ""чт, 09/24/2020 - 16:57""
    }
],
  ""pager"": {
  ""current_page"": 0,
  ""total_items"": ""5"",
  ""total_pages"": 3,
  ""items_per_page"": ""2""
 }
}"
 */






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

    public CommentModel(boolean isReadUserComment, String titleUserComment, String commentUserComment, String likeCountUserComment, boolean isHasNewCommentUserComment, String timeUserComment) {
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


    public static List<CommentModel> makeUserCommentList() {
        List<CommentModel> userCommentList = new ArrayList<>();
        userCommentList.add(new CommentModel(false, "Заголовок_1", "Комментарий_1", "+5",
                true, "12 ч. назад"));

        userCommentList.add(new CommentModel(false, "Заголовок_2", "Комментарий_2", "+100",
                true, "1 д. назад"));

        userCommentList.add(new CommentModel(true, "Заголовок_3", "Комментарий_3", "",
                false, "7 д. назад"));


        return userCommentList;
    }



    public CommentModel(int userImage, String userName, String time, String comment, boolean isUserLikeIt, String likeCount, boolean isCommentHasAnswer) {
        this.userImage = userImage;
        this.userName = userName;
        this.comment = comment;
        this.isUserLikeIt = isUserLikeIt;
        this.likeCount = likeCount;
        this.timeUserComment = time;
        this.isCommentHasAnswer = isCommentHasAnswer;
    }



    public static List<CommentModel> makeCommentsListToPostParent() {
        List<CommentModel> commentsListToPost = new ArrayList<>();
        String comment = "I absolutely adore the original Hairspray (RIP Divine), and the stage version was great...but the movie suffered from some less-than-stellar casting choices, especially when it came to Tracey’s parents. John Travolta just didn’t bring the energy that either Divine did in the original or Harvey Fierstein did on stage, and he had zero chemistry with Christopher Walken. As a result, “Timeless to Me” felt incredibly insincere. The movie was still good, but it could have been so much better IMHO.";
        String comment2 = "I'm with AvalonAngel, Travolta was the worst in Hairspray. I spend the whole movie thinking it was just Travolta in a fat suit & drag.When it's remade they definitely should have a real drag queen play Edna.";
        String comment3 = "I love both but Coraline has a great book too so I voted for it.";

        commentsListToPost.add(new CommentModel(R.drawable.ic_person, "Василий", "1 ч. назад", comment,
                true, "14", false));
        commentsListToPost.add(new CommentModel(R.drawable.ic_person, "Иван", "3 ч. назад", comment2,
                false, "2", true));
        commentsListToPost.add(new CommentModel(R.drawable.ic_person, "Николай", "7 ч. назад", comment3,
                false, "200", true));

        return commentsListToPost;
    }


    public static List<CommentModel> makeCommentsListToPostChild() {
        List<CommentModel> commentsListToPost = new ArrayList<>();
        String comment = "I love both but Coraline has a great book too so I voted for it.";
        String comment2 = "true life is such a masterpiece!";
        String comment3 = "LOL. The first couple of episodes of my Sweet 16 were so outrageous and I got hooked, but after about 4 episodes, it was all just the same. True Life was so much better.";

        commentsListToPost.add(new CommentModel(R.drawable.ic_person, "Даша", "12 ч. назад", comment,
                true, "3", false));
        commentsListToPost.add(new CommentModel(R.drawable.ic_person, "Маша", "12 ч. назад", comment2,
                false, "5", true));
        commentsListToPost.add(new CommentModel(R.drawable.ic_person, "Иван", "12 ч. назад", comment3,
                false, "20", true));
        commentsListToPost.add(new CommentModel(R.drawable.ic_person, "Иван", "12 ч. назад", comment2,
                false, "20", true));
        commentsListToPost.add(new CommentModel(R.drawable.ic_person, "Иван", "12 ч. назад", comment2,
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
