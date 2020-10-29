import { combineReducers } from 'redux';

import {
	authReducer,
	signUpReducer,
	requestResetForgottenPasswordReducer,
	resetForgottenPasswordReducer,
	resetPasswordReducer,
	verifyEmailReducer
} from 'myRedux/reducers/auth';

import {
	searchUserReducer,
	listAllRoleReducer,
	updateUserReducer,
	deleteUserReducer,
	viewUserReducer,
	updateMyProfileReducer
} from 'myRedux/reducers/user';

import {
	searchOrgReducer,
	viewOrgReducer,
	createOrgReducer,
	editOrgReducer,
	deleteOrgReducer,
	listOrgRoomsReducer,
	listOrgAdminsReducer
} from 'myRedux/reducers/org';

import {
	roomSearchReducer,
	roomViewReducer,
	roomCreateReducer,
	roomDeleteReducer,
	roomEditReducer,
	roomListAdminsReducer,
	roomListBookingsReducer,
	roomListUsersReducer
} from 'myRedux/reducers/room';

import {
	bookingSearchReducer,
	bookingViewReducer,
	bookingCreateReducer,
	bookingDeleteReducer,
	bookingEditReducer,
	bookingListAdminsReducer,
	bookingListBookingsReducer,
	bookingListUsersReducer,
	getAvailableTimeslotReducer
} from 'myRedux/reducers/booking';

// import signInReducer from 'reducers/signInReducer';

export default combineReducers({
	authReducer,
	signUpReducer,
	requestResetForgottenPasswordReducer,
	resetForgottenPasswordReducer,
	resetPasswordReducer,
	verifyEmailReducer,

	searchUserReducer,
	listAllRoleReducer,
	updateUserReducer,
	deleteUserReducer,
	viewUserReducer,
	updateMyProfileReducer,

	searchOrgReducer,
	viewOrgReducer,
	createOrgReducer,
	editOrgReducer,
	deleteOrgReducer,
	listOrgRoomsReducer,
	listOrgAdminsReducer,

	roomSearchReducer,
	roomViewReducer,
	roomCreateReducer,
	roomDeleteReducer,
	roomEditReducer,
	roomListAdminsReducer,
	roomListBookingsReducer,
	roomListUsersReducer,

	bookingSearchReducer,
	bookingViewReducer,
	bookingCreateReducer,
	bookingDeleteReducer,
	bookingEditReducer,
	getAvailableTimeslotReducer
});
