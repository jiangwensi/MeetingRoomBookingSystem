import React, { useEffect, useState } from 'react';
import { connect } from 'react-redux';
import _ from 'lodash';
import { Grid, Typography, Card } from '@material-ui/core';

import { viewMyProfile, updateMyProfile } from 'myRedux/actions/user';
import layout2 from 'components/layout/layout2';
import requireAuth from 'hoc/requireAuth';
import { MyTable, MyButton, StatusMessage } from 'components/element';
import {buildViewUserDetails, buildViewUserDetailsForSearch} from 'components/user/utils';

import { viewOrg } from 'myRedux/actions/org';
import { isEmptyObject } from 'utils';
import { CardHeader, CardContent, CardActions } from '@material-ui/core';

const Profile = (props) => {
	useEffect(() => {
		props.viewMyProfile(props.authToken);
	}, []);

	const [name, setName] = useState('');
	const [email, setEmail] = useState('');
	const [myOrgNames, setMyOrgNames] = useState('');

	const updateProfileHandler = () => {
		props.updateMyProfile(props.authToken, props.userDetails.publicId, name, email);
	};

	const returnHander = () => {
		props.history.goBack();
	};

	useEffect(()=> {
		console.log(props.userDetails)
		if(props.userDetails && props.userDetails.isAdminOfOrganizations && props.userDetails.isAdminOfOrganizations.length>0){
			props.userDetails.isAdminOfOrganizations.forEach(e=> {
				console.log(e)
				props.viewOrg(props.authToken,e)
			})
		}
	},[props.userDetails])

	useEffect(()=>{
		let tmp = myOrgNames;
		if(tmp!=''){
			tmp+=', ';
		}
		tmp+=props.myOrgName;
		setMyOrgNames(tmp)
	},[props.myOrgName])

	let user = buildViewUserDetails(props.userDetails,myOrgNames);
	const editableFields = [
		{ key: 'Name', value: name, setValue: setName },
		{ key: 'Email', value: email, setValue: setEmail }
	];

	return (
		<>
			<Card>
				<CardHeader title='My Profile'  style={{ textAlign: 'center' }}></CardHeader>
				<CardContent>
					{props.updateMyProfileStatus && props.updateMyProfileStatus === 'success' && (
						<StatusMessage message='Profile update is successful' />
					)}
					{props.updateMyProfileStatus && props.updateMyProfileStatus === 'failed' && (
						<StatusMessage errorMessage='Unable to update profile. Please try again later' type='failed' />
					)}
					{user  && Object.keys(user).length > 0 && (
						<Grid item>
							<MyTable data={user} editableFields={editableFields} {...props} />
						</Grid>
					)}
				</CardContent>
				<CardActions>
					<Grid container justify='space-around'>
						<Grid item>
							<MyButton
								label='Cancel'
								size='large'
								style={{ width: '100px' }}
								onClick={returnHander}></MyButton>
						</Grid>
						<Grid item>
							<MyButton
								label='Save'
								size='large'
								style={{ width: '100px' }}
								onClick={updateProfileHandler}></MyButton>
						</Grid>
					</Grid>
				</CardActions>
			</Card>
		</>
	);
};

const mapStateToProps = (state) => {
	return {
		authToken: state.authReducer.authToken,
		userDetails: state.viewUserReducer.user,
		updateMyProfileStatus: state.updateMyProfileReducer.status,
		myOrgName: state.viewOrgReducer.name
	};
};

export default connect(mapStateToProps, { viewMyProfile, updateMyProfile,viewOrg })(layout2(requireAuth(Profile)));
