import * as ACTION_CONST from 'utils/const';

export const searchUserReducer = (
	state = { errorId: '', errorMessage: '', message: '', status: '', users: [] },
	action
) => {
	if (action.type === ACTION_CONST.SEARCH_USER) {
		return {
			...state,
			errorId: action.payload.errorId,
			errorMessage: action.payload.errorMessage,
			message: action.payload.message,
			status: action.payload.status,
			users: action.payload.users
		};
	}
	return { ...state };
};

export const viewUserReducer = (
	state = {
		errorId: '',
		errorMessage: '',
		message: '',
		status: '',
		user: {}
	},
	action
) => {
	if (action.type === ACTION_CONST.USER_VIEW) {
		console.log(action.payload.user)
		return {
			...state,
			errorId: action.payload.errorId,
			errorMessage: action.payload.errorMessage,
			message: action.payload.message,
			status: action.payload.status,
			user: action.payload.user
		};
	}
	return { ...state };
};

export const updateMyProfileReducer = (
	state = {
		errorId: '',
		errorMessage: '',
		message: '',
		status: '',
		user: {}
	},
	action
) => {
	if (action.type === ACTION_CONST.UPDATE_MY_PROFILE) {
		return {
			...state,
			errorId: action.payload.errorId,
			errorMessage: action.payload.errorMessage,
			message: action.payload.message,
			status: action.payload.status,
			user: action.payload.user
		};
	}
	return { ...state };
};

export const deleteUserReducer = (
	state = {
		status: '',
		publicId: ''
	},
	action
) => {
	if (action.type === ACTION_CONST.DELETE_USER) {
		return {
			...state,
			status: action.payload.status,
			publicId: action.payload.publicId,
			errorMessage:action.payload.errorMessage
		};
	}
	return { ...state };
};
export const updateUserReducer = (
	state = {
		errorId: '',
		errorMessage: '',
		message: '',
		status: '',
		publicId: '',
		active: false,
		verified: false,
		isAdminOfRooms: [],
		isAdminOfOrganizations: [],
		roles: []
	},
	action
) => {
	if (action.type === ACTION_CONST.USER_UPDATE) {
		return {
			...state,
			errorId: action.payload.errorId,
			errorMessage: action.payload.errorMessage,
			message: action.payload.message,
			publicId: action.payload.publicId,
			name: action.payload.name,
			email: action.payload.status,
			active: action.payload.active,
			verified: action.payload.verified,
			isAdminOfRooms: action.payload.isAdminOfRooms,
			isAdminOfOrganizations: action.payload.isAdminOfOrganizations,
			roles: action.payload.roles,
			status: action.payload.status
		};
	}
	return { ...state };
};

export const listAllRoleReducer = (
	state = { errorId: 'tet', errorMessage: '', message: '', status: '', roles: [] },
	action
) => {
	if (action.type === ACTION_CONST.LIST_ALL_ROLE) {
		return {
			...state,
			errorId: action.payload.errorId,
			errorMessage: action.payload.errorMessage,
			message: action.payload.message,
			status: action.payload.status,
			roles: action.payload.roles
		};
	}
	return { ...state };
};
