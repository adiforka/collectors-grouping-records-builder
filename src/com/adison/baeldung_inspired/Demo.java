package com.adison.baeldung_inspired;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;

public class Demo {
    private static final BlogPost POST1 = BlogPost.builder()
            .title("Shenanigans")
            .author("Charlie Runkle")
            .type(BlogPostType.NEWS)
            .likes(123_342)
            .addComment("Fantastic stuff")
            .addComment("You suck")
            .build();

    private static final BlogPost POST2 = BlogPost.builder()
            .title("Hamlet")
            .author("Britney Spears")
            .type(BlogPostType.GUIDE)
            .likes(54_324)
            .addComment("Best one out there")
            .build();

    private static final BlogPost POST3 = BlogPost.builder()
            .title("Sedes")
            .author("Mojito Maximo")
            .type(BlogPostType.REVIEW)
            .likes(34_323)
            .addComment("Sacre bleu")
            .build();

    private static final BlogPost POST4 = BlogPost.builder()
            .title("Kovacs All Alone")
            .author("Takeshi Kovacs")
            .type(BlogPostType.NEWS)
            .likes(1_012_323)
            .addComment("What have you done, Takeshi Kovacs??")
            .addComment("Unsubscribed!")
            .build();

    private static final BlogPost POST5 = BlogPost.builder()
            .title("untitled")
            .author("n/a")
            .type(BlogPostType.REPORT)
            .likes(0)
            .addComment("Void matter")
            .addComment("Futility supreme")
            .build();

    private static final List<BlogPost> posts = Arrays.asList(POST1, POST2, POST3, POST4, POST5);

    public static void main(String[] args) {

        //grouping by single column
        Map<BlogPostType, List<BlogPost>> postsByType = posts.stream()
                .collect(groupingBy(BlogPost::type));

        postsByType.forEach((k, v) -> System.out.println(k + " : " + v));
        System.out.println("-----------------------------------------------");

        //grouping by complex map key type
        Map<Tuple, List<BlogPost>> postsByTypeAndAuthor = posts.stream()
                .collect(groupingBy(post -> new Tuple(post.type(), post.author())));

        postsByTypeAndAuthor.forEach((k, v) -> System.out.println(k + " : " + v));
        System.out.println("-----------------------------------------------");

        //grouping by multiple fields
        Map<String, Map<BlogPostType, List<BlogPost>>> postsByAuthorThenType = posts.stream()
                .collect(groupingBy(BlogPost::author, groupingBy(BlogPost::type)));

        postsByAuthorThenType.forEach((k, v) -> System.out.println(k + " : " + v));
        System.out.println("-----------------------------------------------");

        //grouping by type, getting average likes for type
        Map<BlogPostType, Double> likesByType = posts.stream()
                .collect(groupingBy(BlogPost::type, averagingInt(BlogPost::likes)));

        likesByType.forEach((k, v) -> System.out.println(k + " : " + v));
        System.out.println("-----------------------------------------------");

        //getting max/min from grouped results
        Map<BlogPostType, Optional<BlogPost>> maxLikesPerType = posts.stream()
                .collect(groupingBy(BlogPost::type, maxBy(comparingInt(BlogPost::likes))));

        maxLikesPerType.forEach((k, v) -> v.ifPresentOrElse((val) -> System.out.println(k + " : " + val),
                () -> System.out.println("Value missing")));
        System.out.println("-----------------------------------------------");

        //getting a summary for an attribute of grouped results
        Map<BlogPostType, IntSummaryStatistics> likeStatsByType = posts.stream()
                .collect(groupingBy(BlogPost::type, summarizingInt(BlogPost::likes)));

        likeStatsByType.forEach((k, v) -> System.out.println(k + " : " + v));
        System.out.println("-----------------------------------------------");

        //mapping grouped results to a different type
        Map<BlogPostType, String> titlesByType = posts.stream()
                .collect(groupingBy(BlogPost::type, mapping(BlogPost::title,
                        joining(", ", "[Post titles: ", "]"))));

        titlesByType.forEach((k, v) -> System.out.println(k + " : " + v));
        System.out.println("-----------------------------------------------");

        //getting the result in the chosen map type
        Map<BlogPostType, List<BlogPost>> postsByTypeEnumMap = posts.stream()
                .collect(groupingBy(BlogPost::type, () -> new EnumMap<>(BlogPostType.class), toList()));

        postsByTypeEnumMap.forEach((k, v) -> System.out.println(k + " : " + v));
        System.out.println("-----------------------------------------------");


        //collectors for concurrent ops
        ConcurrentMap<BlogPostType, List<BlogPost>> postsByTypeConcurrent = posts.parallelStream()
                .collect(groupingByConcurrent(BlogPost::type));

        postsByTypeConcurrent.forEach((k, v) -> System.out.println(k + " : " + v));
        System.out.println("-----------------------------------------------");

        //collectors filtering (we get traces of all categories from grouping first before filtering)
        Map<BlogPostType, Long> postCountWithOverMillionLikesByType = posts.stream()
                .collect(groupingByConcurrent(BlogPost::type, filtering(post -> post.likes() > 1_000_000, counting())));

        postCountWithOverMillionLikesByType.forEach((k, v) -> System.out.println(k + " : " + v));
        System.out.println("-----------------------------------------------");

        //grouping by flatmapping
        Map<String, List<String>> commentsByAuthor = posts.stream()
                .collect(groupingBy(BlogPost::author, flatMapping(post -> post.comments().stream(), toList())));

        commentsByAuthor.forEach((k, v) -> System.out.println(k + " : " + v));

    }
}
