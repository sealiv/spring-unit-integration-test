CREATE TABLE event (
    id bigint NOT NULL auto_increment,
    title VARCHAR(255),
    place VARCHAR(255),
    speaker VARCHAR(255),
    event_type VARCHAR(255),
    date_time TIMESTAMP WITHOUT TIME ZONE,
    PRIMARY KEY (id)
);