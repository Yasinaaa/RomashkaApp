package android.ru.romashkaapp.services

import io.reactivex.Observable

/**
 * Created by yasina on 29.06.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
interface Service<T> {

    fun delete(t: T): Observable<Boolean>
    fun delete(t: Int): Observable<Boolean>
    fun deleteAll(): Observable<Boolean>
    fun delete(t: MutableList<T>): Observable<Boolean>
    operator fun get(ids: MutableList<String>): Observable<MutableList<T>>
    fun getAll(): Observable<T>
    fun getAll2(): Observable<MutableList<T?>>
    fun getById(id: String): Observable<T>
    fun saveOrUpdate(t: T): Observable<Boolean>
    fun saveOrUpdate(t: MutableList<T>): Observable<Boolean>
    fun save(t: T): Observable<Boolean>
    fun save(t: MutableList<T>): Observable<Boolean>
}