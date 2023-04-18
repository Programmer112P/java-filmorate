package ru.yandex.practicum.filmorate.dao.insert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilmSimpleJdbcInsert extends SimpleJdbcInsert {

    @Autowired
    public FilmSimpleJdbcInsert(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.setTableName("films");
        this.setColumnNames(List.of("mpa_id", "film_name", "film_description", "film_release_date", "film_duration"));
        this.setGeneratedKeyName("film_id");
        this.compile();
    }
}
