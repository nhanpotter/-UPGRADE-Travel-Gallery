package tech.ducletran.travelgalleryupgrade.ext

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Single<T>.getDefaultSchedulers(): Single<T> =
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Flowable<T>.getDefaultSchedulers(): Flowable<T> =
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun Completable.getDefaultSchedulers(): Completable =
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())