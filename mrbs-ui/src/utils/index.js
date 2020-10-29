export const isEmptyObject = (obj) => {
	if (obj === undefined || obj === null || Object.keys(obj).length === 0) {
		return true;
	}
	return false;
};

export const isEqualObject = (o1, o2) => {
	console.log('isEqualObject function is called');
	if (!o1 && !o2) {
		console.log('return false');
		return true;
	}
	if ((o1 && !o2) || (!o1 && o2)) {
		console.log('return false');
		return false;
	}
	const o1keys = Object.keys(o1);
	const o2keys = Object.keys(o2);
	if (o1keys.length !== o2keys.length) {
		console.log('return false');
		return false;
	}

	for (let i = 0; i < o1keys.length; i++) {
		if (o1[o1keys[i]] !== o2[o1keys[i]]) {
			return false;
		}
	}

	for (let i = 0; i < o2keys.length; i++) {
		if (o2[o2keys[i]] !== o1[o2keys[i]]) {
			return false;
		}
	}

	console.log(o1, o2, ' equal');
	return true;
};

export const getAuthDetails = () => {
	const userId = sessionStorage.getItem('userId');
	const authToken = sessionStorage.getItem('authToken');
	return { userId, authToken };
};
