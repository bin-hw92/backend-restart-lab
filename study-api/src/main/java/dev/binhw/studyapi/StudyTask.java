package dev.binhw.studyapi;

public record StudyTask(long id, String title, boolean completed) {
    public StudyTask complete() {
        return new StudyTask(id, title, true);
    }
}

