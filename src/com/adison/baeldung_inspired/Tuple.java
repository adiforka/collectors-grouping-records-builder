package com.adison.baeldung_inspired;

//equals/hashcode implemented at compile-time by default for all fields
public record Tuple(BlogPostType type, String author) {
}
