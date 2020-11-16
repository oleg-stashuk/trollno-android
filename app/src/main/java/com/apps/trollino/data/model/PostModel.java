package com.apps.trollino.data.model;

import java.util.ArrayList;
import java.util.List;

public class PostModel {
    private String postTitle;
    private List<OneElementPost> postList;

    public PostModel(String postTitle, List<OneElementPost> postList) {
        this.postTitle = postTitle;
        this.postList = postList;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public List<OneElementPost> getPostList() {
        return postList;
    }


    public static class OneElementPost {
        private String titleElement;
        private String linkImageElement;
        private String linkResourceElement;
        private String linkVideoElement;
        private String descriptionElement;

        public OneElementPost(String titleElement, String linkImageElement, String linkResourceElement, String linkVideoElement, String descriptionElement) {
            this.titleElement = titleElement;
            this.linkImageElement = linkImageElement;
            this.linkResourceElement = linkResourceElement;
            this.linkVideoElement = linkVideoElement;
            this.descriptionElement = descriptionElement;
        }

        public String getTitleElement() {
            return titleElement;
        }

        public String getLinkImageElement() {
            return linkImageElement;
        }

        public String getLinkResourceElement() {
            return linkResourceElement;
        }

        public String getLinkVideoElement() {
            return linkVideoElement;
        }

        public String getDescriptionElement() {
            return descriptionElement;
        }

        public static List<OneElementPost> makePostElementsList() {
            List<OneElementPost> postElementsList = new ArrayList<>();
            postElementsList.add(new OneElementPost("Она кошка, которую зовут Кошка", "RRRRRR", "miaow_maew", "", "Химера любит грызть бумагу"));
            postElementsList.add(new OneElementPost("Заголовок 2", "", "", "RRRRR", "Розовые облака"));
            postElementsList.add(new OneElementPost("", "", "", "", "Химера очень понравилась пользователям"));
            postElementsList.add(new OneElementPost("Заголовок 3", "RRRRRR", "miaow_maew", "", "Химера Конец"));

            return postElementsList;
        }
    }

}
