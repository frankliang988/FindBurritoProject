package com.frankliang.findburritoproject.network

import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import com.frankliang.findburritoproject.BurritoPlaceListQuery
import com.frankliang.findburritoproject.model.Coordinate

object NetworkRepository {
    private const val PAGE_SIZE = 20
    suspend fun findRestaurants(coordinate: Coordinate, page: Int): Response<BurritoPlaceListQuery.Data> {
        return ApolloClientUtil.getApolloClient()
            .query(BurritoPlaceListQuery(coordinate.latitude!!, coordinate.longitude!!, PAGE_SIZE, page * PAGE_SIZE)).await()
    }
}