export const handleError = (payload, error) => {
	payload['status'] = 'failed';
	if (error.response) {
		payload['errorMessage'] = error.response.data.errorMessage;
	} else if (error.request) {
		payload['errorMessage'] = 'Unable to submit this request. Please check your network';
	} else {
		payload['errorMessage'] = 'Something went wrong';
	}
	return payload;
};
