package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.exception.DAOException;
import ru.yandex.practicum.filmorate.exception.ServiceException;

import java.util.List;

//Нет никакой бизнес-логики, поэтому использование интерфейса вылядит странным
//Но, наверное, в реальном проекте надо писать какую-то базовую реализацию, а от нее уже наследоваться при расширении
//Вроде того, как мы делали менеджеры

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmDao filmDao;

    @Override
    public List<Film> getAll() {
        log.info("Список фильмов получен в сервисном слое");
        return filmDao.getAll();
    }

    @Override
    public Film create(final Film film) {
        log.info("Фильм {} создется в сервисном слое", film);
        Film created = filmDao.create(film);
        log.info("Фильм {} создан в сервисном слое", created);
        return created;
    }

    @Override
    public Film update(final Film film) {
        try {
            log.info("Фильм {} обновляется в сервисном слое", film);
            Film updated = filmDao.update(film);
            log.info("Фильм {} обновлен в сервисном слое", updated);
            return updated;
        } catch (DAOException ex) {
            log.error("Фильма с id {} не существует в сервисном слое", film.getId());
            throw new ServiceException((String.format("Фильма с id %s не существует", film.getId())), ex);
        }
    }
}
