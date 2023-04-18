package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.exception.DAOException;
import ru.yandex.practicum.filmorate.exception.DbConnectionException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getById(long id) {
        log.info("MpaDao getById: Запрос на получение mpa с id {}", id);
        String sql = "SELECT * FROM mpa WHERE mpa_id = ?;";
        try {
            Mpa mpa = jdbcTemplate.queryForObject(sql, this::mapRowToMpa, id);
            log.info("MpaDao getById: Получен mpa с id {}", id);
            return mpa;
        } catch (IncorrectResultSizeDataAccessException e) {
            log.error("MpaDao getById: Не существует mpa с id {}", id);
            throw new DAOException(e.getMessage(), e);
        } catch (DataAccessException e) {
            log.error("MpaDao getById: Ошибка при выполнении запроса {} с id {}", sql, id);
            throw new DbConnectionException(e.getMessage(), e);
        }
    }

    @Override
    public List<Mpa> getAll() {
        log.info("MpaDao getAll: Запрос на получение всех mpa");
        String sql = "SELECT * FROM mpa;";
        try {
            List<Mpa> mpas = jdbcTemplate.query(sql, this::mapRowToMpa);
            log.info("MpaDao getAll: Получены все mpa");
            return mpas;
        } catch (DataAccessException e) {
            log.error("MpaDao getAll: Ошибка при выполнении запроса {}", sql);
            throw new DbConnectionException(e.getMessage(), e);
        }
    }

    private Mpa mapRowToMpa(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(rs.getLong("mpa_id"))
                .name(rs.getString("mpa_name"))
                .build();
    }
}
