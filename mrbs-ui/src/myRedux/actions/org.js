import * as ACTION from 'utils/const';
import { ORG } from 'utils/const';
import mrbsAxios from 'apis';
import qs from 'qs';
import { handleError } from 'myRedux/actions/util';

export const searchOrg = (authToken, name, active) => async (dispatch) => {
	let payload = {};
	const response = await mrbsAxios
		.get(`${ORG}`, {
			params: {
				name,
				active
			},
			paramsSerializer: (params) => {
				return qs.stringify(params, { arrayFormat: 'repeat' });
			},
			headers: {
				Authorization: authToken
			}
		})
		.catch((error) => {
			handleError(payload, error);
		});

	if (response) {
		payload = response.data;
	}

	dispatch({ type: ACTION.ORG_SEARCH, payload });
};

export const viewOrg = (authToken, publicId) => async (dispatch) => {
	console.log(publicId)
	let payload = {};
	const response = await mrbsAxios
		.get(`${ORG}/${publicId}`, {
			headers: {
				Authorization: authToken
			}
		})
		.catch((error) => {
			handleError(payload, error);
		});

	if (response) {
		payload = response.data;
	}

	dispatch({ type: ACTION.ORG_VIEW, payload });
};

export const createOrg = (authToken, name, description, admins, active, callback) => async (dispatch) => {
	console.log('createOrg');
	let payload = {};
	const response = await mrbsAxios
		.post(
			`${ORG}`,
			{ name, admins: admins.split(','), description, active },
			{
				headers: {
					Authorization: authToken
				}
			}
		)
		.catch((error) => {
			payload = handleError(payload, error);
		});

	if (response) {
		payload = response.data;
		if (response.data.status === 'success') {
			payload.customMessage = 'Successfully created organization ' + name;

			if (callback && typeof callback === 'function') {
				callback();
			}
		}
	}
	dispatch({ type: ACTION.ORG_CREATE, payload });
};

export const editOrg = (authToken, name, publicId, description, active, admins, successCallBack) => async (
	dispatch
) => {
	let payload = {};
	let adminStr = [];
	if (admins !== undefined) {
		admins.forEach((a) => adminStr.push(a.publicId));
	}

	const response = await mrbsAxios
		.patch(
			`${ORG}`,
			{ publicId, name, admins: adminStr, description, active },
			{
				headers: {
					Authorization: authToken
				}
			}
		)
		.catch((error) => {
			payload = handleError(payload, error);
		});

	if (response) {
		payload = response.data;
		if (
			response.data &&
			response.data.status === 'success' &&
			successCallBack &&
			typeof successCallBack === 'function'
		) {
			successCallBack();
		}
	}

	dispatch({ type: ACTION.ORG_UPDATE, payload });
};

export const deleteOrg = (authToken, publicId, name, successCallback) => async (dispatch) => {
	let payload = {};
	const response = await mrbsAxios
		.delete(`${ORG}/${publicId}`, {
			headers: {
				Authorization: authToken
			}
		})
		.catch((error) => {
			handleError(payload, error);
		});

	if (response) {
		payload = response.data;
		if (response.data.status === 'success') {
			payload.customMessage = 'Successfully deleted organization ' + name;
			if (successCallback && typeof successCallback === 'function') {
				successCallback();
			}
		}
	}

	dispatch({ type: ACTION.ORG_DELETE, payload });
};

export const listOrgRooms = (authToken, orgPublicId) => async (dispatch) => {
	let payload = {};
	const response = await mrbsAxios
		.get(`${ORG}/${orgPublicId}/rooms`, {
			headers: {
				Authorization: authToken
			}
		})
		.catch((error) => {
			handleError(payload, error);
		});

	if (response) {
		payload = response.data;
	}

	dispatch({ type: ACTION.ORG_LIST_ROOMS, payload });
};

export const listOrgAdmins = (authToken, orgPublicId) => async (dispatch) => {
	let payload = {};
	const response = await mrbsAxios
		.get(`${ORG}/${orgPublicId}/admins`, {
			headers: {
				Authorization: authToken
			}
		})
		.catch((error) => {
			handleError(payload, error);
		});

	if (response) {
		payload = response.data;
	}

	dispatch({ type: ACTION.ORG_LIST_ADMINS, payload });
};
