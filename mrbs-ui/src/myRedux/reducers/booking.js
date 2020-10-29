import { BOOKING_CREATE, BOOKING_DELETE, BOOKING_SEARCH, BOOKING_UPDATE, BOOKING_VIEW,BOOKING_AVAIL } from 'utils/const';

export const bookingSearchReducer = (
	state = { status: '', customMessage: '', bookings: [], errorMessage: '' },
	action
) => {
	if (action.type === BOOKING_SEARCH) {
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

export const bookingViewReducer = (
	state = { status: '', customMessage: '', booking: {}, errorMessage: '' },
	action
) => {
	if (action.type === BOOKING_VIEW) {
		return {
			...state,
			status: action.payload.status,
			booking: action.payload.booking,
			customMessage: action.payload.customMessage,
			errorMessage: action.payload.errorMessage
		};
	}
	return { ...state };
};

export const bookingCreateReducer = (
	state = { status: '', customMessage: '', booking: {}, errorMessage: '' },
	action
) => {
	if (action.type === BOOKING_CREATE) {
		return {
			...state,
			status: action.payload.status,
			booking: action.payload.booking,
			customMessage: action.payload.customMessage,
			errorMessage: action.payload.errorMessage
		};
	}
	return { ...state };
};

export const bookingEditReducer = (
	state = { status: '', customMessage: '', booking: {}, errorMessage: '' },
	action
) => {
	if (action.type === BOOKING_UPDATE) {
		return {
			...state,
			status: action.payload.status,
			booking: action.payload.booking,
			customMessage: action.payload.customMessage,
			errorMessage: action.payload.errorMessage
		};
	}
	return { ...state };
};

export const bookingDeleteReducer = (state = { status: '', customMessage: '', errorMessage: '' }, action) => {
	if (action.type === BOOKING_DELETE) {
		return {
			...state,
			status: action.payload.status,
			customMessage: action.payload.customMessage,
			errorMessage: action.payload.errorMessage
		};
	}
	return { ...state };
};

export const getAvailableTimeslotReducer = (state={status:''}, action)=>{
	if(action.type ===BOOKING_AVAIL){
		return {
			...state,
			status:action.payload.status,
			slots:action.payload.availableTimeslots,
			customMessage: action.payload.customMessage,
			errorMessage: action.payload.errorMessage
		}
	}
	return { ...state };
}