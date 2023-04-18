package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.exception.DAOException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@Service
public class MpaServiceImpl implements MpaService {

    private final MpaDao mpaDao;

    @Autowired
    public MpaServiceImpl(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    @Override
    public Mpa getById(Long id) {
        log.info("MpaDao getById: Запрос на получение mpa с id {}", id);
        try {
            Mpa mpa = mpaDao.getById(id);
            log.info("MpaDao getById: Получен mpa с id {}", id);
            return mpa;
        } catch (DAOException e) {
            log.error("MpaDao getById: Не найден mpa с id {}", id);
            throw new NotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public List<Mpa> getAll() {
        log.info("MpaDao getAll: Запрос на получение всех mpa");
        List<Mpa> mpas = mpaDao.getAll();
        log.info("MpaDao getAll: Получены все mpa");
        return mpas;
    }
}
