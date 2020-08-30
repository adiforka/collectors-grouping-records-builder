package com.adison.baeldung_inspired;

import java.util.ArrayList;
import java.util.List;

//builder with a Java 14 record type class
public record BlogPost(String title, String author, BlogPostType type, int likes, List<String>comments) {

    public BlogPost(Builder builder) {
        this(builder.title, builder.author, builder.type, builder.likes, builder.comments);
    }

    public static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private String title;
        private String author;
        private BlogPostType type;
        private int likes;
        private final List<String> comments = new ArrayList<>();

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder type(BlogPostType type) {
            this.type = type;
            return this;
        }

        public Builder likes(int likes) {
            this.likes = likes;
            return this;
        }

        public Builder addComment(String comment) {
            this.comments.add(comment);
            return this;
        }

        public BlogPost build() {
            return new BlogPost(this);
        }

    }
}
