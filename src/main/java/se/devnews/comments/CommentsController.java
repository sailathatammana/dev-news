package se.devnews.comments;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.devnews.ResourceNotFoundException;
import se.devnews.articles.Article;
import se.devnews.articles.ArticleRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CommentsController {
    CommentsRepository commentsRepository;
    ArticleRepository articleRepository;

    public CommentsController(CommentsRepository commentsRepository, ArticleRepository articleRepository) {
        this.commentsRepository = commentsRepository;
        this.articleRepository = articleRepository;
    }

    // Return all comments on a given article
    @GetMapping("/articles/{articleId}/comments")
    public ResponseEntity<List<Comments>> getAllCommentsById(@PathVariable Long articleId){
        List<Comments> comments = commentsRepository
                .findAll()
                .stream()
                .filter((item) ->item.getArticle().getId().equals(articleId))
                .collect(Collectors.toList());
        return ResponseEntity.ok(comments);
    }

    // Return all comments made by a author
    @GetMapping(value = "/comments", params = {"authorName"})
    public ResponseEntity<List<Comments>> getAllCommentsByAuthorName(@RequestParam String authorName){
        List<Comments> comments = commentsRepository
                .findAll()
                .stream()
                .filter((item) -> item.getAuthorName().equals(authorName))
                .collect(Collectors.toList());
        return ResponseEntity.ok(comments);
    }

    //Add new comment on article
    @PostMapping("/articles/{articleId}/comments")
    public ResponseEntity<Comments> createComment(@PathVariable Long articleId, @RequestBody Comments comments){
        Article article = articleRepository.findById(articleId).orElseThrow(ResourceNotFoundException::new);
        comments.setArticle(article);
        commentsRepository.save(comments);
        return ResponseEntity.status(HttpStatus.CREATED).body(comments);
    }

    //update the given comment.
    @PutMapping("/comments/{id}")
    public ResponseEntity<Comments> updateComment(@PathVariable Long id, @RequestBody Comments updatedComment){
        commentsRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        updatedComment.setId(id);
        commentsRepository.save(updatedComment);
        return ResponseEntity.ok(updatedComment);
    }

    //Delete the given comment.
    @DeleteMapping("comments/{id}")
    public ResponseEntity<Comments> deleteComment(@PathVariable Long id){
        Comments comment = commentsRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        commentsRepository.delete(comment);
        return ResponseEntity.ok(comment);
    }
}