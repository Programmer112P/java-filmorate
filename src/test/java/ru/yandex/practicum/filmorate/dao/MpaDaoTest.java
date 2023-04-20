package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.impl.MpaDaoImpl;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.exception.DAOException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@DataJdbcTest
@Sql({"/testschema.sql", "/testdata.sql"})
class MpaDaoTest {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDao mpaDao;

    @Autowired
    public MpaDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        mpaDao = new MpaDaoImpl(jdbcTemplate);
    }

    @Test
    void getById_shouldReturnMpaById() {
        Mpa mpa = mpaDao.getById(2);
        assertThat(mpa.getId()).isEqualTo(2);
        assertThat(mpa.getName()).isEqualTo("PG");
        Mpa mpa1 = mpaDao.getById(4);
        assertThat(mpa1.getId()).isEqualTo(4);
        assertThat(mpa1.getName()).isEqualTo("R");
    }

    @Test
    void getById_shouldThrowException() {
        assertThrows(DAOException.class, () -> mpaDao.getById(222));
    }

    @Test
    void getAll() {
        List<Mpa> mpas = mpaDao.getAll();
        assertThat(mpas.size()).isEqualTo(5);
    }
}