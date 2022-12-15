package com.boosters.promise.ui.detail

import com.boosters.promise.data.location.GeoLocation
import com.boosters.promise.data.location.LocationRepository
import com.boosters.promise.data.location.UserGeoLocation
import com.boosters.promise.data.member.Member
import com.boosters.promise.data.member.MemberRepository
import com.boosters.promise.data.notification.NotificationRepository
import com.boosters.promise.data.promise.Promise
import com.boosters.promise.data.promise.PromiseRepository
import com.boosters.promise.data.user.User
import com.boosters.promise.data.user.UserRepository
import com.boosters.promise.receiver.alarm.AlarmDirector
import com.boosters.promise.util.NetworkConnectionUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.argumentCaptor

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PromiseDetailViewModelTest {

    @Mock private lateinit var promiseRepository: PromiseRepository

    @Mock private lateinit var userRepository: UserRepository

    @Mock private lateinit var memberRepository: MemberRepository

    @Mock private lateinit var locationRepository: LocationRepository

    @Mock private lateinit var notificationRepository: NotificationRepository

    @Mock private lateinit var alarmDirector: AlarmDirector

    @Mock private lateinit var networkConnectionUtil: NetworkConnectionUtil

    private val promiseDetailViewModel: PromiseDetailViewModel
        get() = PromiseDetailViewModel(
            promiseRepository,
            userRepository,
            memberRepository,
            locationRepository,
            notificationRepository,
            alarmDirector,
            networkConnectionUtil,
            myPromiseId
        )

    private lateinit var myInfo: User
    private lateinit var myPromiseId: String
    private lateinit var myPromise: Promise
    private lateinit var usersIncludeMe: List<User>

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())

        myInfo = User("ABCDE1", "user1", null, "userToken1")
        usersIncludeMe = listOf(
            User("ABCDE1", "user1", null, "userToken1"),
            User("ABCDE2", "user2", null, "userToken2"),
        )
        myPromiseId = "1"
        myPromise = Promise(
            promiseId = myPromiseId,
            title = "제목",
            destinationName = "도착지",
            destinationGeoLocation = GeoLocation(),
            date = "2022/12/10",
            time = "21:47",
            members = usersIncludeMe
        )
        `when`(promiseRepository.getPromise(myPromise.promiseId)).thenReturn(
            flow {
                emit(myPromise)
            }
        )
        `when`(userRepository.getMyInfo()).thenReturn(
            flow {
                emit(Result.success(myInfo))
            }
        )
    }

    @Test
    fun getPromise_CorrectMyPromise() = runTest {
        val promise = promiseDetailViewModel.promise.first()

        assertEquals(myPromise, promise)
    }

    @Test
    fun getUserGeoLocations_CorrectMembers() = runTest {
        val fakeMembers = myPromise.members.map { user ->
            Member(myPromise.promiseId, user.userCode, true)
        }
        val expectedUserGeoLocations = myPromise.members.filter { it.userCode != myInfo.userCode }.map {
            UserGeoLocation(
                it.userCode,
                null
            )
        }
        `when`(memberRepository.getMembers(myPromise.promiseId)).thenReturn(
            flow {
                emit(
                    fakeMembers
                )
            }
        )

        `when`(locationRepository.getGeoLocations(anyList())).thenReturn(
            flow {
                emit(expectedUserGeoLocations)
            }
        )

        val userCodesArgumentCaptor = argumentCaptor<List<String>>()

        val userGeoLocations = promiseDetailViewModel.userGeoLocations.first()

        verify(locationRepository).getGeoLocations(userCodesArgumentCaptor.capture())
        assertEquals(userCodesArgumentCaptor.firstValue, listOf("ABCDE2"))
        assertEquals(expectedUserGeoLocations, userGeoLocations)
    }

    @Test
    fun getUserGeoLocations_AnyoneNoLocationSharingAccept() = runTest {
        val fakeMembers = myPromise.members.map { user ->
            Member(myPromise.promiseId, user.userCode, false)
        }
        `when`(memberRepository.getMembers(myPromise.promiseId)).thenReturn(
            flow {
                emit(
                    fakeMembers
                )
            }
        )

        val userGeoLocations = promiseDetailViewModel.userGeoLocations.first()

        assertEquals(emptyList<UserGeoLocation>(), userGeoLocations)
    }

}