DELETE FROM event WHERE id > 0;
INSERT INTO event (id, title, place, speaker, event_type, date_time) VALUES
    (1, 'title 4', 'place test', 'Anatoly', 'Lecture', '2022-06-02T12:00:01'),
    (2, 'title 4', 'place test', 'Anatoly', 'Lecture', '2022-06-03T12:00:01'),
    (3, 'title 5', 'place test', 'Alexey', 'Dance', '2022-06-04T12:00:01');
