package android.ru.romashkaapp.data.repository

import io.reactivex.Observable
/**
 * Created by yasina on 29.06.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */

interface BaseRepository<T> {
    fun delete(t: T): Observable<Boolean>
    fun delete(t: Int): Observable<Boolean>
    fun delete(t: MutableList<T>): Observable<Boolean>
    fun deleteAll(): Observable<Boolean>
    operator fun get(ids: MutableList<String>): Observable<MutableList<T>>
    operator fun get(id: String): Observable<T>
    fun getLong(ids: MutableList<Long>): Observable<MutableList<T>>
    fun getLong(id: Long): Observable<T>
    fun getAll(): Observable<T>
    fun getAll2(): Observable<MutableList<T?>>
    fun saveOrUpdate(t: T): Observable<Boolean>
    fun saveOrUpdate(t: MutableList<T>): Observable<Boolean>
    fun save(t: T): Observable<Boolean>
    fun save(t: MutableList<T>): Observable<Boolean>
}