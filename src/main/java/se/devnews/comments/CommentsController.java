package se.devnews.comments;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.devnews.ResourceNotFoundException;
import se.devnews.articles.Article;
import se.devnews.articles.ArticleRepository;

@RestController
public class CommentsController {
    CommentsRepository commentsRepository;
    ArticleRepository articleRepository;

    public CommentsController(CommentsRepository commentsRepository, ArticleRepository articleRepository) {
        this.commentsRepository = commentsRepository;
        this.articleRepository = articleRepository;
    }

    //Add new comment on article
    @PostMapping("/articles/{articleId}/comments")
    public ResponseEntity<Comments> createComment(@PathVariable Long articleId, @RequestBody Comments comments){
        Article article = articleRepository.findById(articleId).orElseThrow(ResourceNotFoundException::new);
        comments.setArticle(article);
        commentsRepository.save(comments);
        return ResponseEntity.status(HttpStatus.CREATED).body(comments);
    }
}

