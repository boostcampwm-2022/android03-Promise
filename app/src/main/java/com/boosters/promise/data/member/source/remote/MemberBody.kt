package com.boosters.promise.data.member.source.remote

import com.boosters.promise.data.member.Member

data class MemberBody(
    val promiseId: String?,
    val userCode: String?,
    val isAcceptLocation: Boolean? = false
)

fun MemberBody.toMember() =
    Member(
        promiseId = promiseId.orEmpty(),
        userCode = userCode ?: throw NullPointerException(),
        isAcceptLocation = isAcceptLocation ?: false
    )