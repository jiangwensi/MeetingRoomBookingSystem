import * as ACTION_CONST from 'utils/const';
import * as API_PATH_CONST from 'utils/const';
import mrbsAxios from 'apis';
import {frontEndDomain} from 'apis';
import qs from 'qs';

export const searchUser = (authToken, name, email, roles, actives, verifies,verbose) => async (dispatch) => {
	let payload;

	const response = await mrbsAxios
		.get(API_PATH_CONST.USER, {
			params: {
				name,
				email,
				role: roles,
				active: actives,
				verified: verifies,
				verbose:verbose
			},
			paramsSerializer: (params) => {
				return qs.stringify(params, { arrayFormat: 'repeat' });
			},
			headers: {
				Authorization: authToken
			}
		})
		.catch((error) => {
			if (error.response) {
				payload = error.response.data;
			}
		});

	if (response && response.data && response.data.users) {
		response.data.users.forEach((e) => {
			sessionStorage.setItem(`SearchUser ${e.publicId}`, JSON.stringify(e));
		});
		if (response.data.users.length === 0) {
			let n = sessionStorage.length;
			while (n--) {
				let key = sessionStorage.key(n);
				if (/^SearchUser/.test(key)) {
					sessionStorage.removeItem(key);
				}
			}
		}
	}

	if (response && response.data) {
		payload = response.data;
	}

	dispatch({
		type: ACTION_CONST.SEARCH_USER,
		payload: payload
	});
};

export const deleteUser = (authToken, publicId) => async (dispatch) => {
	let payload;
	const response = await mrbsAxios
		.delete(`${API_PATH_CONST.USER}/${publicId}`, {
			headers: {
				Authorization: authToken
			}
		})
		.catch((error) => {
			if (error.response) {
				payload = error.response.data;
			}
		});

	if (response && response.data) {
		payload = response.data;
		// sessionStorage.removeItem(`SearchUser ${publicId}`);
	}

	dispatch({
		type: ACTION_CONST.DELETE_USER,
		payload: {
			status: payload.status,
			errorMessage:payload.errorMessage,
			publicId: publicId
		}
	});
};

export const viewMyProfile = (authToken) => async (dispatch) => {
	let payload;
	const response = await mrbsAxios
		.get(`/myprofile/view`, {
			headers: {
				Authorization: authToken
			}
		})
		.catch((error) => {
			if (error.response) {
				payload = error.response.data;
			}
		});

	if (response && response.data) {

		payload = response.data;
	}

	dispatchPayloadUserDetails(dispatch, payload);
};

export const viewUser = (authToken, publicId) => async (dispatch) => {
	let payload;
	const response = await mrbsAxios
		.get(`${API_PATH_CONST.USER}/${publicId}`, {
			headers: {
				Authorization: authToken
			}
		})
		.catch((error) => {
			if (error.reponse) {
				payload = error.response.data;
			}
		});
	if (response && response.data) {
		payload = response.data;
	}

	dispatchPayloadUserDetails(dispatch, payload);
};

export const updateMyProfile = (authToken, publicId, name, email) => async (dispatch) => {
	let payload = {};
	let returnUrl = 'http://'+frontEndDomain+'/verifyEmail';
	const response = await mrbsAxios
		.patch(
			'/myprofile/update',
			{ publicId, name, email, changeEmailReturnUrl: returnUrl },
			{
				headers: {
					Authorization: authToken
				}
			}
		)
		.catch((error) => {
			if (error.response) {
				console.log(error.response);
				payload = error.response.data;

				payload = {
					errorId: response.data.errorId,
					errorMessage: response.data.errorMessage,
					message: response.data.message,
					status: response.data.status,
					user: {}
				};
			} else if (error.request) {
				payload['status'] = 'failed';
				console.log(error.request);
			} else {
				payload['status'] = 'failed';
				console.log('Unable to send request, something went wrong');
			}
		});

	if (response && response.data) {
		payload = {
			errorId: response.data.errorId,
			errorMessage: response.data.errorMessage,
			message: response.data.message,
			status: response.data.status,
			user: {
				publicId: response.data.publicId,
				active: response.data.active,
				isAdminOfRooms: response.data.isAdminOfRooms,
				isAdminOfOrganizations: response.data.isAdminOfOrganizations,
				roles: response.data.roles,
				name: response.data.name,
				email: response.data.email,
				verified: response.data.email_verified
			}
		};
		sessionStorage.setItem('username', response.data.name);
	}

	dispatch({
		type: ACTION_CONST.UPDATE_MY_PROFILE,
		payload: payload
	});
};

export const updateUser = (authToken, publicId, name, roles, active) => async (dispatch) => {
	let payload;
	const response = await mrbsAxios
		.patch(
			API_PATH_CONST.USER,
			{
				publicId,
				name,
				roles,
				active
			},
			{
				headers: {
					Authorization: authToken
				}
			}
		)
		.catch((error) => {
			if (error.response) {
				payload = error.response.data;
			}
		});

	if (response && response.data) {
		payload = response.data;

		let user = {
			publicId: payload.publicId,
			name: payload.name,
			email: payload.email,
			active: payload.active,
			verified: payload.emailVerified,
			roles: payload.roles
		};
		sessionStorage.setItem(`SearchUser ${payload.publicId}`, JSON.stringify(user));
	}

	dispatch({
		type: ACTION_CONST.USER_UPDATE,
		payload: payload
	});
};

export const listAllRoles = (authToken) => async (dispatch) => {
	const response = await mrbsAxios.get(API_PATH_CONST.ROLE, {
		headers: {
			Authorization: authToken
		}
	});

	if (response && response.data && response.data.roles) {
		sessionStorage.setItem(`roleOptions`, JSON.stringify(response.data.roles));
	}

	dispatch({
		type: ACTION_CONST.LIST_ALL_ROLE,
		payload: response.data
	});
};
function dispatchPayloadUserDetails(dispatch, payload) {
	let updatedPayload = {
		errorId: payload.errorId,
		errorMessage: payload.errorMessage,
		message: payload.message,
		status: payload.status,
		user: {
			publicId: payload.publicId,
			active: payload.active,
			isAdminOfRooms: payload.isAdminOfRooms,
			isUserOfRooms: payload.isUserOfRooms,
			isAdminOfOrganizations: payload.isAdminOfOrganizations,
			roles: payload.roles,
			name: payload.name,
			email: payload.email,
			verified: payload.email_verified
		}
	};

	dispatch({
		type: ACTION_CONST.USER_VIEW,
		payload: updatedPayload
	});
}
