import moment from 'moment';

export const validateEmail = (email) => {
	const regex = /\S+@\S+\.\S/;
	return regex.test(email);
};

export const validateCommaSeparatedEmails = (emails) => {
	let emailArr = [];
	let isValid = true;
	if (emails) {
		emailArr = emails.split(',');
	}
	emailArr.forEach((e) => {
		console.log(e);
	});

	emailArr.forEach((e) => {
		if (!validateEmail(e)) {
			isValid = false;
		}
	});

	if (emailArr.length === 0) {
		isValid = false;
	}

	return isValid;
};

export const validateName = (name) => {
	const regex = /^\D+$/;
	return regex.test(name);
};
export const validateOrgName = (name) => {
	return name && name.length > 0;
};

export const validatePassword = (password) => {
	return password.length >= 8;
};

export const validatePasswordMatch = (password, confirmPassword) => {
	return password === confirmPassword;
};

export const validateNotEmpty = (value) => {
	if (value && value.length > 0) {
		return true;
	}
	return false;
};

export const validateNumber = (value) => {
	if (value && value.length > 0) {
		if (/^\d+$/.test(value)) {
			return true;
		}
	}
	return false;
};

export const validateTime = (value) => {
	console.log('validate time', moment(value, 'yyyy-MM-dd HH:mm:ss').isValid());
	return moment(value, 'yyyy-MM-dd HH:mm:ss').isValid();
};
