package com.boosters.promise.data.member.source.remote

import com.boosters.promise.data.member.Member

data class MemberBody(
    val promiseId: String? = null,
    val userCode: String? = null,
    val acceptLocation: Boolean? = false
)

fun MemberBody.toMember() =
    Member(
        promiseId = promiseId.orEmpty(),
        userCode = userCode ?: throw NullPointerException(),
        isAcceptLocation = acceptLocation ?: false
    )