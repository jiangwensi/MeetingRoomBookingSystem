import {
	ROOM_SEARCH,
	ROOM_VIEW,
	ROOM_CREATE,
	ROOM_EDIT,
	ROOM_DELETE,
	ROOM_LIST_ADMINS,
	ROOM_LIST_BOOKINGS,
	ROOM_LIST_USERS
} from 'utils/const';

export const roomSearchReducer = (state = { status: '', customMessage: '', rooms: [], errorMessage: '' }, action) => {
	if (action.type === ROOM_SEARCH) {
		return {
			...state,
			status: action.payload.status,
			rooms: action.payload.rooms,
			customMessage: action.payload.customMessage,
			errorMessage: action.payload.errorMessage
		};
	}
	return { ...state };
};

export const roomViewReducer = (state = { status: '', customMessage: '', room: {}, errorMessage: '' }, action) => {
	if (action.type === ROOM_VIEW) {
		return {
			...state,
			status: action.payload.status,
			room: action.payload.room,
			customMessage: action.payload.customMessage,
			errorMessage: action.payload.errorMessage
		};
	}
	return { ...state };
};

export const roomCreateReducer = (state = { status: '', customMessage: '', room: {}, errorMessage: '' }, action) => {
	if (action.type === ROOM_CREATE) {
		return {
			...state,
			status: action.payload.status,
			room: action.payload.room,
			customMessage: action.payload.customMessage,
			errorMessage: action.payload.errorMessage
		};
	}
	return { ...state };
};

export const roomEditReducer = (state = { status: '', customMessage: '', room: {}, errorMessage: '' }, action) => {
	if (action.type === ROOM_EDIT) {
		return {
			...state,
			status: action.payload.status,
			room: action.payload.room,
			customMessage: action.payload.customMessage,
			errorMessage: action.payload.errorMessage
		};
	}
	return { ...state };
};

export const roomDeleteReducer = (state = { status: '', customMessage: '', errorMessage: '' }, action) => {
	if (action.type === ROOM_DELETE) {
		return {
			...state,
			status: action.payload.status,
			customMessage: action.payload.customMessage,
			errorMessage: action.payload.errorMessage
		};
	}
	return { ...state };
};

export const roomListAdminsReducer = (
	state = { status: '', customMessage: '', admins: [], errorMessage: '' },
	action
) => {
	if (action.type === ROOM_LIST_ADMINS) {
		return {
			...state,
			status: action.payload.status,
			admins: action.payload.users,
			customMessage: action.payload.customMessage,
			errorMessage: action.payload.errorMessage
		};
	}
	return { ...state };
};

export const roomListBookingsReducer = (
	state = { status: '', customMessage: '', bookings: [], errorMessage: '' },
	action
) => {
	if (action.type === ROOM_LIST_BOOKINGS) {
		return {
			...state,
			status: action.payload.status,
			bookings: action.payload.bookings,
			customMessage: action.payload.customMessage,
			errorMessage: action.payload.errorMessage
		};
	}
	return { ...state };
};

export const roomListUsersReducer = (
	state = { status: '', customMessage: '', users: [], errorMessage: '' },
	action
) => {
	if (action.type === ROOM_LIST_USERS) {
		return {
			...state,
			status: action.payload.status,
			users: action.payload.users,
			customMessage: action.payload.customMessage,
			errorMessage: action.payload.errorMessage
		};
	}
	return { ...state };
};
