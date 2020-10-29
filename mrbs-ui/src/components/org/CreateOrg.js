import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import layout2 from 'components/layout/layout2';
import requireAuth from 'hoc/requireAuth';
import { Grid, Typography, TextField, FormControl, FormLabel, RadioGroup, FormControlLabel } from '@material-ui/core';
import { MyButton, InputRadioButton, StatusMessage } from 'components/element';
import { validateOrgName, validateCommaSeparatedEmails } from 'utils/validation';
import { createOrg } from 'myRedux/actions/org';
import { ROUTE_ORG_SEARCH } from 'utils/const';

const CreateOrg = (props) => {
	const [name, setName] = useState('');
	const [description, setDescription] = useState('');
	const [adminEmails, setAdminEmails] = useState('');
	const [active, setActive] = useState(true);

	const [nameHelper, setNameHelper] = useState('');
	const [adminEmailsHelper, setAdminEmailsHelper] = useState('');

	useEffect(() => {
		if (!skipValidation() && !validateOrgName(name)) {
			setNameHelper('Invalid Name');
		} else {
			setNameHelper('');
		}
	}, [name]);

	useEffect(() => {
		if (!skipValidation() && !validateCommaSeparatedEmails(adminEmails)) {
			setAdminEmailsHelper('Invalid Admin Emails');
		} else {
			setAdminEmailsHelper('');
		}
	}, [adminEmails]);

	const skipValidation = () => {
		return !name && !description && !adminEmails;
	};

	const returnHander = () => {
		props.history.goBack();
	};

	const submitHandler = () => {
		console.log('clicked');
		if (validateOrgName(name) && validateCommaSeparatedEmails(adminEmails)) {
			console.log('valid');
			props.createOrg(props.authToken, name, description, adminEmails, active, successHandler);
		}
	};

	const successHandler = () => {
		props.history.push(ROUTE_ORG_SEARCH);
	};

	return (
		<Grid item xs={12} sm={11} md={10} lg={9} xl={8}>
			<Grid container style={{ marginBottom: '3em' }} justify='center'>
				<Typography align='center' variant='h5'>
					Create Organization
				</Typography>
			</Grid>
			{props.errorMessage && (
				<Grid container alignItems='center' justify='space-around' style={{ marginBottom: '1em' }}>
					<Grid item xs={8}>
						<StatusMessage type='failed' errorMessage={props.errorMessage} />
					</Grid>
				</Grid>
			)}
			<Grid container alignItems='center' justify='space-around' style={{ marginBottom: '1em' }}>
				<Grid item xs={8}>
					<Grid container>
						<Grid item xs={9}>
							<TextField
								helperText={nameHelper}
								error={nameHelper !== ''}
								fullWidth
								variant='outlined'
								label='Name'
								value={name}
								onChange={(event) => setName(event.target.value)}></TextField>
						</Grid>
						<Grid item xs={3}>
							<InputRadioButton
								label1='Active'
								value1={true}
								label2='Inactive'
								value2={false}
								value={active}
								setValue={setActive}
							/>
						</Grid>
					</Grid>
				</Grid>
			</Grid>
			<Grid container alignItems='center' justify='space-around' style={{ marginBottom: '1em' }}>
				<Grid item xs={8}>
					<TextField
						fullWidth
						variant='outlined'
						label='Description'
						value={description}
						onChange={(event) => setDescription(event.target.value)}></TextField>
				</Grid>
			</Grid>
			<Grid container alignItems='center' justify='space-around' style={{ marginBottom: '1em' }}>
				<Grid item xs={8}>
					<TextField
						helperText={adminEmailsHelper}
						error={adminEmailsHelper !== ''}
						fullWidth
						variant='outlined'
						label='Organization Admin Email (separate multiple emails by comma)'
						value={adminEmails}
						onChange={(event) => setAdminEmails(event.target.value)}></TextField>
				</Grid>
			</Grid>
			<Grid
				container
				alignItems='center'
				justify='space-around'
				style={{ marginBottom: '1em', marginTop: '2em' }}>
				<Grid item xs={8} style={{ padding: 0 }}>
					<Grid container justify='space-between' style={{ width: '100%' }}>
						<Grid item>
							<MyButton label='Cancel' width='10em' onClick={returnHander} />
						</Grid>
						<Grid item>
							<MyButton
								label='Submit'
								width='10em'
								onClick={submitHandler}
								disabled={!name && !description && !adminEmails}
							/>
						</Grid>
					</Grid>
				</Grid>
			</Grid>
		</Grid>
	);
};

const mapStateToProps = (state) => {
	return {
		authToken: state.authReducer.authToken,
		status: state.createOrgReducer.status,
		errorMessage: state.createOrgReducer.errorMessage
	};
};

export default connect(mapStateToProps, { createOrg })(requireAuth(layout2(CreateOrg)));
