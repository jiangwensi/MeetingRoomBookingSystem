import * as ACTION_CONST from 'utils/const';
import * as API_PATH_CONST from 'utils/const.js';
import mrbsAxios from 'apis';
import {frontEndDomain} from 'apis';

export const signUp = (name, email, password) => async (dispatch) => {
	let payload;
	const response = await mrbsAxios
		.post(API_PATH_CONST.AUTH + '/signUp', {
			name,
			email,
			password,
			returnUrl: 'http://'+frontEndDomain+'/verifyEmail'
		})
		.catch((error) => {
			if (error.response) {
				payload = error.response.data;
			}
		});

	if (response && response.data) {
		payload = response.data;
	}

	dispatch({
		type: ACTION_CONST.SIGN_UP,
		payload: payload
	});
};

export const requestResetForgottenPassword = (email) => async (dispatch) => {
	let message = '';
	let errorMessage = '';
	let returnUrl = 'http://'+frontEndDomain+'/resetForgottenPassword';

	const response = await mrbsAxios
		.post(API_PATH_CONST.AUTH + '/requestResetForgottenPassword', {
			email,
			returnUrl
		})
		.catch((error) => {
			console.log(error);
			errorMessage = 'Unable to request resetting forgotten password. Please try again later';
		});

	message = response ? (response.data ? response.data.message : '') : '';
	dispatch({
		type: ACTION_CONST.REQUEST_RESET_FORGOTTEN_PASSWORD,
		payload: {
			message: message,
			errorMessage
		}
	});
};

export const resetForgottenPassword = (email, password, token) => async (dispatch) => {
	let message = '';
	let errorMessage = '';
	const response = await mrbsAxios
		.post(API_PATH_CONST.AUTH + '/resetForgottenPassword', {
			email,
			password,
			token
		})
		.catch((error) => {
			console.log(error);
			errorMessage = 'Unable to reset forgotten password. Please try again later';
		});
	console.log(response.data)
	message = response ? (response.data ? response.data.message : '') : '';
	errorMessage = response ? (response.data ? response.data.errorMessage : errorMessage) : errorMessage;
	dispatch({
		type: ACTION_CONST.RESET_FORGOTTEN_PASSWORD,
		payload: {
			message,
			errorMessage
		}
	});
};

export const resetPassword = (email, oldPassword, newPassword) => async (dispatch) => {
	let message = '';
	let errorMessage = '';
	let status = '';

	const response = await mrbsAxios
		.post(API_PATH_CONST.AUTH + '/resetPassword', {
			email,
			oldPassword,
			newPassword
		})
		.catch((error) => {
			console.log(error);
			errorMessage = 'Unable to reset password. Please try again later';
		});
	console.log(response.data)
	message = response ? (response.data ? response.data.message : '') : '';
	errorMessage = response ? (response.data ? response.data.errorMessage : '') : '';
	status = response ? (response.data ? response.data.status : 'fail') : 'fail';
	dispatch({
		type: ACTION_CONST.RESET_PASSWORD,
		payload: {
			message,
			errorMessage,
			status
		}
	});
};

export const verifyEmail = (token) => async (dispatch) => {
	let message = '';
	let errorMessage = '';
	const response = await mrbsAxios
		.get(API_PATH_CONST.AUTH + '/verifyEmail', {
			params: { token: token }
		})
		.catch((error) => {
			console.log(error);
			errorMessage = 'Unable to verify email. Please try again later';
		});
	message = response ? (response.data ? response.data.message : '') : '';
	errorMessage = response ? (response.data ? response.data.errorMessage : errorMessage) : errorMessage;
	dispatch({
		type: ACTION_CONST.VERIFY_EMAIL,
		payload: {
			message,
			errorMessage
		}
	});
};

export const signIn = (email, password, callback) => async (dispatch) => {
	let errorMessage;
	let status;
	let errorId;
	let authToken;
	let roles;
	let message;
	let username;
	let myOrgs;
	let myRooms;
	let userPublicId;

	const response = await mrbsAxios.post('/login', { email, password }).catch((error) => {
		status = 'failed';
		errorMessage = 'Sign In failed, please try again';
	});

	if (response  && response.data ) {
		authToken = response.data.authToken;
		roles = response.data.roles;
		status = response.data.status;
		message = response.data.message;
		errorMessage = response.data.errorMessage;
		errorId = response.data.errorId;
		username = response.data.name;
		userPublicId = response.data.userPublicId;
		// myOrgs = response.data.orgs;
		// myRooms = response.data.rooms;
	}

	dispatch({
		type: ACTION_CONST.SIGN_IN,
		payload: { authToken, roles, status, message, errorMessage, errorId, username,userPublicId }
	});

	sessionStorage.setItem('authToken', authToken);
	sessionStorage.setItem('roles', roles);
	sessionStorage.setItem('username', username);
	sessionStorage.setItem("userPublicId",userPublicId)
	if(myOrgs){
		sessionStorage.setItem('myOrgs', JSON.stringify(myOrgs));
	}
	if(myRooms){
		sessionStorage.setItem('myRooms', JSON.stringify(myRooms));
	}

	callback();
};

export const signOut = (callback) => {
	// sessionStorage.removeItem('authToken');
	// sessionStorage.removeItem('roles');
	// sessionStorage.removeItem('openMobileMenu');
	// sessionStorage.removeItem('openMenu');
	// sessionStorage.removeItem('myOrgs');
	// sessionStorage.removeItem('myRooms');
	sessionStorage.clear();

	if (callback && typeof callback === 'function') {
		callback();
	}
	return {
		type: ACTION_CONST.SIGN_OUT
	};
};

// export const getAuth = () => {
// 	const userId = sessionStorage.getItem('userId');
// 	const authToken = sessionStorage.getItem('authToken');
// 	return {
// 		type: ACTION_CHECK_AUTH,
// 		payload: { userId, authToken }
// 	};
// };
