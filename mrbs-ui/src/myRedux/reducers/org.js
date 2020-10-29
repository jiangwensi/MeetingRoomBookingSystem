import * as ACTION from 'utils/const';

export const searchOrgReducer = (state = { status: '', organizations: [] }, action) => {
	if (action.type === ACTION.ORG_SEARCH) {
		return {
			...state,
			status: action.payload.status,
			organizations: action.payload.organizations
		};
	}

	return { ...state };
};

export const viewOrgReducer = (
	state = {
		status: '',
		publicId: '',
		name: '',
		admins: [],
		rooms: [],
		active: null,
		description: ''
	},
	action
) => {
	if (action.type === ACTION.ORG_VIEW) {
		return {
			...state,
			status: action.payload.status,
			publicId: action.payload.publicId,
			name: action.payload.name,
			admins: action.payload.admins,
			rooms: action.payload.rooms,
			active: action.payload.active,
			description: action.payload.description
		};
	}

	return { ...state };
};

export const createOrgReducer = (
	state = {
		status: '',
		message: '',
		errorMessage: '',
		customMessage: '',
		publicId: '',
		name: '',
		admins: [],
		rooms: [],
		active: null,
		description: ''
	},
	action
) => {
	if (action.type === ACTION.ORG_CREATE) {
		return {
			...state,
			status: action.payload.status,
			message: action.payload.message,
			customMessage: action.payload.customMessage,
			errorMessage: action.payload.errorMessage,
			publicId: action.payload.publicId,
			name: action.payload.name,
			admins: action.payload.admins,
			rooms: action.payload.rooms,
			active: action.payload.active,
			description: action.payload.description
		};
	}

	return { ...state };
};

export const editOrgReducer = (
	state = {
		status: '',
		errorMessage: '',
		publicId: '',
		name: '',
		admins: [],
		rooms: [],
		active: null,
		description: ''
	},
	action
) => {
	if (action.type === ACTION.ORG_UPDATE) {
		return {
			...state,
			status: action.payload.status,
			errorMessage: action.payload.errorMessage,
			publicId: action.payload.publicId,
			name: action.payload.name,
			admins: action.payload.admins,
			rooms: action.payload.rooms,
			active: action.payload.active,
			description: action.payload.description
		};
	}

	return { ...state };
};

export const deleteOrgReducer = (state = { status: '', customMessage: '' }, action) => {
	if (action.type === ACTION.ORG_DELETE) {
		return {
			...state,
			status: action.payload.status,
			customMessage: action.payload.customMessage,
			errorMessage: action.payload.errorMessage
		};
	}

	return { ...state };
};

export const listOrgAdminsReducer = (state = { status: '', admins: [] }, action) => {
	if (action.type === ACTION.ORG_LIST_ADMINS) {
		return {
			...state,
			status: action.payload.status,
			admins: action.payload.users
		};
	}

	return { ...state };
};

export const listOrgRoomsReducer = (state = { status: '', rooms: [] }, action) => {
	if (action.type === ACTION.ORG_LIST_ROOMS) {
		return {
			...state,
			status: action.payload.status,
			rooms: action.payload.rooms
		};
	}

	return { ...state };
};
