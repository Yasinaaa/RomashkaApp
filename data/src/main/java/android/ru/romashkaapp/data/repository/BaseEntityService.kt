package android.ru.romashkaapp.data.repository

import android.ru.romashkaapp.services.Service
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by yasina on 29.06.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
class BaseEntityService<T : BaseObject> @Inject constructor(localRepository: BaseRepository<T>) : Service<T> {

    private var tBaseRepository: BaseRepository<T>

    override fun deleteAll(): Observable<Boolean> {
        return tBaseRepository.deleteAll()
    }

    override fun delete(t: T): Observable<Boolean> {
        return tBaseRepository.delete(t)
    }

    override fun delete(t: MutableList<T>): Observable<Boolean> {
        return tBaseRepository.delete(t)
    }

    override fun delete(t: Int): Observable<Boolean> {
        return tBaseRepository.delete(t)
    }

    override fun get(ids: MutableList<String>): Observable<MutableList<T>> {
        return tBaseRepository[ids]
    }

    override fun getAll(): Observable<T> {
        return tBaseRepository.getAll()
    }

    override fun getAll2(): Observable<MutableList<T?>> {
        return tBaseRepository.getAll2()
    }

    override fun getById(id: String): Observable<T> {
        return tBaseRepository[id]
    }

    override fun saveOrUpdate(t: T): Observable<Boolean> {
        return tBaseRepository.saveOrUpdate(t)
    }

    override fun saveOrUpdate(t: MutableList<T>): Observable<Boolean> {
        return tBaseRepository.saveOrUpdate(t)
    }

    override fun save(t: T): Observable<Boolean> {
        return tBaseRepository.save(t)
    }

    override fun save(t: MutableList<T>): Observable<Boolean> {
        return tBaseRepository.save(t)
    }

    init {
        tBaseRepository = localRepository
    }
}