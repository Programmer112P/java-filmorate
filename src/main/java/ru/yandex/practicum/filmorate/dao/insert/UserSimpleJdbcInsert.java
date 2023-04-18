package ru.yandex.practicum.filmorate.dao.insert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserSimpleJdbcInsert extends SimpleJdbcInsert {

    @Autowired
    public UserSimpleJdbcInsert(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.setTableName("users");
        this.setColumnNames(List.of("user_email", "user_login", "user_name", "user_birthday"));
        this.setGeneratedKeyName("user_id");
        this.compile();
    }
}
