import { BOOKING } from 'utils/const.js';
import { BOOKING_CREATE, BOOKING_DELETE, BOOKING_SEARCH, BOOKING_UPDATE, BOOKING_VIEW ,BOOKING_AVAIL} from 'utils/const';
import mrbsAxios from 'apis';
import { handleError } from 'myRedux/actions/util';

export const searchBooking = (authToken, roomId, date) => async (dispatch) => {
	let payload = {};
	const response = await mrbsAxios
		.get(`${BOOKING}`, {
			params: {
				roomId,
				date
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

	dispatch({ type: BOOKING_SEARCH, payload });
};


export const getAvailableTimeslotByRoom = (authToken, roomId,date)=> async(dispatch)=> {
	console.log("getAvailableTimeslotByRoom,authToken="+authToken)
	let payload={};
	const response = await mrbsAxios
		.get(`${BOOKING}/room/${roomId}/availableslots/${date}`,{
			headers: {
				Authorization: authToken
			}
		})
		.catch((error)=>{
			handleError(payload,error);
		})
	if(response){
		payload = response.data;
		console.log(payload)
	}
	dispatch({type: BOOKING_AVAIL, payload})
}

export const createBooking = (authToken, roomId, fromTime, toTime, successCallback) => async (dispatch) => {
	let payload = {};
	console.log(authToken, roomId, fromTime, toTime);
	const response = await mrbsAxios
		.post(
			`${BOOKING}`,
			{
				roomId,
				fromTime,
				toTime
			},
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
			payload.customMessage = 'Successfully created booking from '+fromTime.substring(0,16)+' to '+toTime.substring(0,16);

			if (successCallback && typeof successCallback === 'function') {
				successCallback();
			}
		}
	}
	dispatch({
		type: BOOKING_CREATE,
		payload: {
			status: payload.status,
			errorMessage: payload.errorMessage,
			customMessage: payload.customMessage,
			booking: {
				publicId: payload.publicId,
				fromTime: payload.fromTime,
				toTime: payload.toTime,
				roomId: payload.roomId,
				bookedBy: payload.bookedBy
			}
		}
	});
};


export const deleteBooking = (authToken, bookingId, successCallback) => async (dispatch) => {
	let payload = {};
	const response = await mrbsAxios
		.delete(`${BOOKING}/${bookingId}`, {
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
			payload.customMessage = 'Successfully deleted booking';
			if (successCallback && typeof successCallback === 'function') {
				successCallback();
			}
		}
	}

	dispatch({ type: BOOKING_DELETE, payload });
};
