import * as ACTION_CONST from 'utils/const';

export const authReducer = (
	state = {
		userId: null,
		authorization: null,
		roles: []
	},
	action
) => {
	if (action.type === ACTION_CONST.CHECK_AUTH) {
		return { ...state, userId: action.payload.userId, authToke: action.payload.authToken };
	}

	if (action.type === ACTION_CONST.SIGN_IN) {
		return {
			...state,
			authToken: action.payload.authToken,
			status: action.payload.status,
			message: action.payload.message,
			errorMessage: action.payload.errorMessage,
			roles: action.payload.roles,
			errorId: action.payload.errorId,
			username: action.payload.username,
			userPublicId:action.payload.userPublicId
		};
	}

	if (action.type === ACTION_CONST.SIGN_OUT) {
		return {
			...state,
			authToken: '',
			userId: ''
		};
	}

	if (action.type === ACTION_CONST.UPDATE_MY_PROFILE && action.payload && action.payload.status === 'success') {
		return {
			...state,
			username: action.payload.user.name
		};
	}

	return { ...state };
};

export const requestResetForgottenPasswordReducer = (
	state = {
		errorMessage: ''
	},
	action
) => {
	if (action.type === ACTION_CONST.REQUEST_RESET_FORGOTTEN_PASSWORD) {
		return { ...state, errorMessage: action.payload.errorMessage, message: action.payload.message };
	}
	return { ...state };
};

export const resetForgottenPasswordReducer = (
	state = {
		message: '',
		errorMessage: ''
	},
	action
) => {
	if (action.type === ACTION_CONST.RESET_FORGOTTEN_PASSWORD) {
		return { ...state, errorMessage: action.payload.errorMessage, message: action.payload.message };
	}
	return { ...state };
};

export const resetPasswordReducer = (
	state = {
		message: '',
		errorMessage: ''
	},
	action
) => {
	if (action.type === ACTION_CONST.RESET_PASSWORD) {
		console.log(action.payload)
		return { ...state, errorMessage: action.payload.errorMessage, message: action.payload.message };
	}
	return { ...state };
};

export const signUpReducer = (
	state = {
		userId: null,
		name: null,
		email: null,
		message: null,
		time: '',
		errorMessage: ''
	},
	action
) => {
	if (action.type === ACTION_CONST.SIGN_UP) {
		return {
			...state,
			userId: action.payload.userId,
			name: action.payload.name,
			email: action.payload.email,
			message: action.payload.message,
			errorMessage: action.payload.errorMessage,
			time: action.payload.time,
			myOrgs: action.payload.myOrgs,
			myRooms: action.payload.myRooms
		};
	}
	return { ...state };
};

export const verifyEmailReducer = (
	state = {
		message: '',
		errorMessage: ''
	},
	action
) => {
	if (action.type === ACTION_CONST.VERIFY_EMAIL) {
		return { ...state, message: action.payload.message, errorMessage: action.payload.errorMessage };
	}

	return { ...state };
};
