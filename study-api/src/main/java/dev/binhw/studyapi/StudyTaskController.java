package dev.binhw.studyapi;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/study-tasks")
public class StudyTaskController {
    private final StudyTaskService service;

    public StudyTaskController(StudyTaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<StudyTask> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public StudyTask find(@PathVariable long id) {
        return service.find(id);
    }

    @PostMapping
    public ResponseEntity<StudyTask> create(@RequestBody CreateStudyTaskRequest request) {
        StudyTask task = service.create(request);
        return ResponseEntity.created(URI.create("/api/study-tasks/" + task.id())).body(task);
    }

    @PatchMapping("/{id}/complete")
    public StudyTask complete(@PathVariable long id) {
        return service.complete(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

