package se.devnews.Topics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.devnews.articles.Article;
import se.devnews.articles.ArticleRepository;
import se.devnews.exceptions.ResourceNotFoundException;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TopicController {

    TopicRepository topicRepository;
    ArticleRepository articleRepository;

    @Autowired
    public TopicController(TopicRepository topicRepository, ArticleRepository articleRepository) {
        this.topicRepository = topicRepository;
        this.articleRepository = articleRepository;
    }

    // Create a new topic
    @PostMapping("/topics")
    public ResponseEntity<Topic> createTopic(@Valid @RequestBody Topic topic){
        Topic topicSearch = topicRepository.findByName(topic.getName());
        if(topicSearch == null){
            topicRepository.save(topic);
            return ResponseEntity.status(HttpStatus.CREATED).body(topic);
        }
        topicRepository.save(topicSearch);
        return ResponseEntity.status(HttpStatus.CREATED).body(topicSearch);
    }

    // Associate the topic with the article given by articleId. If topic does not already exist, it is created.
    @PostMapping("/articles/{articleId}/topics")
    public ResponseEntity<Article> createAssociation(@RequestBody Topic topicToAdd, @PathVariable Long articleId){
        Article article = articleRepository.findById(articleId).orElseThrow(ResourceNotFoundException::new);
        //Checks if Topic already exists on the Article so double topic is not added.
        List<Topic> topics = article.getTopicsList();
        for(Topic topic2 : topics){
            if(topic2.getName().equals(topicToAdd.getName())){
                return  ResponseEntity.status(HttpStatus.CONFLICT).body(article);
            }
        }
        //Checks if the article Topic already exists in the articleTopic Repositoty, if it doesnt exist adds it to the Repository.
        Topic articleTopicSearch = topicRepository.findByName(topicToAdd.getName());
        if (articleTopicSearch == null) {
            topicRepository.save(topicToAdd);
            article.getTopicsList().add(topicToAdd);
        } else {
            article.getTopicsList().add(articleTopicSearch);
        }
        articleRepository.save(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(article);
    }

    // Return all topics
    @GetMapping("/topics")
    public ResponseEntity<List<Topic>> getAllTopics(){
        List<Topic> allTopics = topicRepository.findAll();
        return ResponseEntity.ok(allTopics);
    }

    // Return all topics associated with article given by articleId
    @GetMapping("/articles/{articleId}/topics")
    public ResponseEntity<List<Topic>> getAllTopicsOfArticle(@PathVariable Long articleId){
        Article givenArticle = articleRepository.findById(articleId).orElseThrow(ResourceNotFoundException::new);
        List<Topic> allTopicOfGivenArticle = givenArticle.getTopicsList();
        return ResponseEntity.ok(allTopicOfGivenArticle);
    }

    // Return all articles associated with the given topicId
    @GetMapping("/topics/{topicId}/articles")
    public ResponseEntity<List<Article>> getAllArticlesOfTopic(@PathVariable Long topicId){
        Topic givenTopic = topicRepository.findById(topicId).orElseThrow(ResourceNotFoundException::new);
        List<Article> allArticlesOfGivenTopic = givenTopic.getArticles();
        return ResponseEntity.ok(allArticlesOfGivenTopic);
    }

    // Update the given topic
    @PutMapping("/topics/{id}")
    public ResponseEntity<Topic> updateTopic(@PathVariable Long id, @Valid @RequestBody Topic updatedTopic){
        topicRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        updatedTopic.setId(id);
        topicRepository.save(updatedTopic);
        return ResponseEntity.ok(updatedTopic);
    }

    // Delete the given topic
    @DeleteMapping("/topics/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTopic(@PathVariable Long id){
        Topic toBeDeleted = topicRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        topicRepository.delete(toBeDeleted);
    }

    // Delete the association of the given topic for the given article. The topic & article themselves remain
    @DeleteMapping("/articles/{articleId}/topics/{topicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAssociation(@PathVariable Long articleId, @PathVariable Long topicId){
        Article articleToBeDisassociated = articleRepository.findById(articleId).orElseThrow(ResourceNotFoundException::new);
        Topic topicToBeDisassociated = topicRepository.findById(topicId).orElseThrow(ResourceNotFoundException::new);
        if(articleToBeDisassociated.getTopicsList().contains(topicToBeDisassociated)){
            articleToBeDisassociated.getTopicsList().remove(topicToBeDisassociated);
            articleRepository.save(articleToBeDisassociated);
        }
        throw new ResourceNotFoundException();
    }
}