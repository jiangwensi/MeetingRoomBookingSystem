export const buildViewUserDetailsForSearch = (userDetails,myOrgNames) => {
	console.log("buildViewUserDetailsForSearch")
	let user = {};
	if (userDetails && Object.keys(userDetails).length > 0) {
		// user['Public ID'] = userDetails.publicId;
		user['Name'] = userDetails.name;
		user['Email'] = userDetails.email;
		user['Email Verified'] = userDetails.verified ? 'Yes' : 'No';

		if (userDetails.roles  && userDetails.roles.length > 0) {
			let result = '';
			userDetails.roles.forEach((r) => {
				result += r + ', ';
			});
			if (result.substring(result.length - 2, result.length) === ', ') {
				result = result.substring(0, result.length - 2);
			}
			user['Role'] = result;
		}

		if (userDetails.isAdminOfRooms && userDetails.isAdminOfRooms.length > 0) {
			let result = userDetails.isAdminOfRooms[0];
			userDetails.isAdminOfRooms.forEach((r,i) => {
				if(i>0){
					result += ', '+r ;
				}
			});
			user['Room Admin'] = result;
		}
		console.log(userDetails)
		if (userDetails.isUserOfRooms && userDetails.isUserOfRooms.length > 0) {
			let result = userDetails.isUserOfRooms[0];
			userDetails.isUserOfRooms.forEach((r,i) => {
				if(i>0){
					result += ', '+r ;
				}
			});
			user['Room User'] = result;
		}

		if (
			userDetails.isAdminOfOrganizations &&
			userDetails.isAdminOfOrganizations.length > 0
		) {
			// let result = '';
			// userDetails.isAdminOfOrganizations.forEach((r) => {
			// 	result += r + ',';
			// });
			user['Org Admin'] = myOrgNames;
		}
		user['Active'] = userDetails.active ? 'Yes' : 'No';
	}
	return user;
};


export const buildViewUserDetails = (userDetails,myOrgNames) => {
	let user = {};
	if (userDetails && Object.keys(userDetails).length > 0) {
		// user['Public ID'] = userDetails.publicId;
		user['Name'] = userDetails.name;
		user['Email'] = userDetails.email;
		user['Email Verified'] = userDetails.verified ? 'Yes' : 'No';

		if (userDetails.roles  && userDetails.roles.length > 0) {
			let result = '';
			userDetails.roles.forEach((r) => {
				result += r + ', ';
			});
			if (result.substring(result.length - 2, result.length) === ', ') {
				result = result.substring(0, result.length - 2);
			}
			user['Role'] = result;
		}

		if (userDetails.isAdminOfRooms && userDetails.isAdminOfRooms.length > 0) {
			let result = userDetails.isAdminOfRooms[0];
			userDetails.isAdminOfRooms.forEach((r,i) => {
				if(i>0){
					result += ', '+r ;
				}
			});
			user['Room Admin'] = result;
		}
		console.log(userDetails)
		if (userDetails.isUserOfRooms && userDetails.isUserOfRooms.length > 0) {
			let result = userDetails.isUserOfRooms[0];
			userDetails.isUserOfRooms.forEach((r,i) => {
				if(i>0){
					result += ', '+r ;
				}
			});
			user['Room User'] = result;
		}

		if (
			userDetails.isAdminOfOrganizations &&
			userDetails.isAdminOfOrganizations.length > 0
		) {
			// let result = '';
			// userDetails.isAdminOfOrganizations.forEach((r) => {
			// 	result += r + ',';
			// });
			user['Org Admin'] = myOrgNames;
		}
		user['Active'] = userDetails.active ? 'Yes' : 'No';
	}
	return user;
};
