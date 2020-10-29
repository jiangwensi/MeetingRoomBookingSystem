import {
    ROOM_DELETE,
    ROOM_EDIT,
    ROOM_LIST_ADMINS,
    ROOM_LIST_BOOKINGS,
    ROOM_SEARCH,
    ROOM_VIEW,
    ROOM_CREATE,
    ROOM_LIST_USERS
} from 'utils/const';
import {ROOM} from 'utils/const';
import mrbsAxios from 'apis';
import qs from 'qs';
import {handleError} from 'myRedux/actions/util';

export const searchRoom = (authToken, roomName, orgName, active, myEnrolledRoomOnly) => async (dispatch) => {
    let payload = {};
    const response = await mrbsAxios
        .get(`${ROOM}`, {
            params: {
                roomName,
                orgName,
                active,
                myEnrolledRoomOnly
            },
            paramsSerializer: (params) => {
                return qs.stringify(params, {arrayFormat: 'repeat'});
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

    dispatch({type: ROOM_SEARCH, payload});
};

export const viewRoom = (authToken, publicId) => async (dispatch) => {
    let payload = {};
    const response = await mrbsAxios
        .get(`${ROOM}/${publicId}`, {
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

    dispatch({
        type: ROOM_VIEW,
        payload: {
            status: payload.status,
            errorMessage: payload.errorMessage,
            room: {
                publicId: payload.publicId,
                name: payload.name,
                capacity: payload.capacity,
                facilities: payload.facilities,
                description: payload.description,
                active: payload.active,
                organization: payload.organization,
                bookings: payload.bookings,
                admins: payload.admins,
                users: payload.users,
                blockedTimeslots: payload.blockedTimeslots,
                images: payload.images
            }
        }
    });
};

export const listRoomAdmins = (authToken, roomPublicId) => async (dispatch) => {
    let payload = {};
    const response = await mrbsAxios
        .get(`${ROOM}/${roomPublicId}/admins`, {
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

    dispatch({type: ROOM_LIST_ADMINS, payload});
};

export const listRoomUsers = (authToken, roomPublicId) => async (dispatch) => {
    let payload = {};
    const response = await mrbsAxios
        .get(`${ROOM}/${roomPublicId}/users`, {
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

    dispatch({type: ROOM_LIST_USERS, payload});
};

export const listRoomBookings = (authToken, roomPublicId) => async (dispatch) => {
    let payload = {};
    const response = await mrbsAxios
        .get(`${ROOM}/${roomPublicId}/bookings`, {
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

    dispatch({type: ROOM_LIST_BOOKINGS, payload});
};

export const createRoom = (
    authToken,
    name,
    capacity,
    description,
    facilities,
    orgPublicId,
    admins,
    active,
    blockedTimeslots,
    roomImages,
    successCallback
) => async (dispatch) => {

    let payload = {};
    let formData = new FormData();
    let roomData = {
        name,
        capacity,
        description,
        facilities,
        orgPublicId,
        admins: admins.split(','),
        blockedTimeslots,
        active
    }

    roomImages.forEach(e => formData.append("roomImages", e));
    // formData.append("roomImages", roomImages[0])
    formData.append("roomData", new Blob([JSON.stringify(roomData)], {type: 'application/json'}));
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    const response = await mrbsAxios
        .post(
            `${ROOM}`,
            formData,
            {
                headers: {
                    'Authorization': authToken,
                    // 'Content-Type': `multipart/form-data; boundary=${formData.getBoundary()}`
                }
            }
        )
        .catch((error) => {
            payload = handleError(payload, error);
        });
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // let response;
    // axios({
    //     method: 'post',
    //     url: 'http://localhost:8080/rooms',
    //     data: formData,
    //     headers: {'Content-Type': 'multipart/form-data' ,'Authorization': authToken,}
    // })
    //     .then(function (response) {
    //         //handle success
    //         response = response;
    //         console.log(response);
    //     })
    //     .catch(function (response) {
    //         //handle error
    //         console.log(response);
    //     });
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    if (response) {
        payload = response.data;
        if (response.data.status === 'success') {
            payload.customMessage = 'Successfully created room ' + name;

            if (successCallback && typeof successCallback === 'function') {
                successCallback();
            }
        }
    }
    dispatch({type: ROOM_CREATE, payload});
};

export const editRoom = (
    authToken,
    publicId,
    name,
    capacity,
    facilities,
    description,
    active,
    roomAdmins,
    roomUsers,
    blockedTimeslots,
    successCallback
) => async (dispatch) => {
    let payload = {};
    let adminStr = [];
    let userStr = [];
    if (roomAdmins !== undefined) {
        roomAdmins.forEach((a) => adminStr.push(a.publicId));
    }
    if (roomUsers !== undefined) {
        roomUsers.forEach((a) => userStr.push(a.publicId));
    }

    const response = await mrbsAxios
        .patch(
            `${ROOM}`,
            {
                publicId,
                name,
                capacity,
                facilities,
                description,
                active,
                blockedTimeslots,
                // roomImages,
                admins: adminStr,
                users: userStr
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
            payload.customMessage = 'Successfully edited room ' + name;
            if (successCallback && typeof successCallback === 'function') {
                successCallback();
            }
        }
    }

    dispatch({type: ROOM_EDIT, payload});
};

export const deleteRoom = (authToken, publicId, name, successCallback) => async (dispatch) => {
    let payload = {};
    const response = await mrbsAxios
        .delete(`${ROOM}/${publicId}`, {
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
            payload.customMessage = 'Successfully deleted room ' + name;

            if (successCallback && typeof successCallback === 'function') {
                successCallback();
            }
        }
    }

    dispatch({type: ROOM_DELETE, payload});
};
