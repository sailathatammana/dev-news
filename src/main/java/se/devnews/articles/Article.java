package se.devnews.articles;

import se.devnews.Topics.Topic;
import se.devnews.comments.Comments;

import javax.persistence.*;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String body;
    private String authorName;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comments> comments;

    @ManyToMany
    private Set<Topic> topicsList = new HashSet<>();

    public Article() {
    }

    public Article(String title, String body, String authorName) {
        this.title = title;
        this.body = body;
        this.authorName = authorName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public Set<Topic> getTopicsList() {
        return topicsList;
    }

    public void setTopicsList(Set<Topic> topics) {
        this.topicsList = topics;
    }
}
