package dev.binhw.studyapi;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StudyTaskService {
    private final ConcurrentHashMap<Long, StudyTask> tasks = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong();

    public List<StudyTask> list() {
        return tasks.values().stream()
            .sorted(Comparator.comparingLong(StudyTask::id))
            .toList();
    }

    public StudyTask find(long id) {
        StudyTask task = tasks.get(id);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "학습 항목이 없습니다.");
        }
        return task;
    }

    public StudyTask create(CreateStudyTaskRequest request) {
        if (request == null || request.title() == null || request.title().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "제목을 입력하세요.");
        }
        long id = nextId.incrementAndGet();
        StudyTask task = new StudyTask(id, request.title().trim(), false);
        tasks.put(id, task);
        return task;
    }

    public StudyTask complete(long id) {
        return tasks.compute(id, (key, task) -> {
            if (task == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "학습 항목이 없습니다.");
            }
            return task.complete();
        });
    }

    public void delete(long id) {
        if (tasks.remove(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "학습 항목이 없습니다.");
        }
    }
}

