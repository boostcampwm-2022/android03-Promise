package com.boosters.promise.data.member

import com.boosters.promise.data.member.source.remote.MemberBody

data class Member(
    val promiseId: String,
    val userCode: String,
    val isAcceptLocation: Boolean
)

fun Member.toMemberBody() =
    MemberBody(
        promiseId = promiseId,
        userCode = userCode,
        isAcceptLocation = isAcceptLocation
    )