package se.devnews.Topics;

import org.springframework.data.jpa.repository.JpaRepository;
import se.devnews.Topics.Topic;


public interface TopicRepository extends JpaRepository<Topic, Long> {
    Topic findByName(String name);
    boolean existsByName(String name);
}