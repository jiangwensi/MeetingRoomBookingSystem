import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { updateUser,  viewUser } from 'myRedux/actions/user';

import { Grid } from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';
import { Check as CheckIcon, DeleteOutlineOutlined as DeleteIcon, VisibilityOutlined } from '@material-ui/icons';

import { MyInputField, OutputText, InputRadioButton, MyCheckbox, MyModal, MyTable } from 'components/element';
import { buildViewUserDetailsForSearch } from 'components/user/utils';

const useStyles = makeStyles({
	hover: {
		'&:hover': {
			color: 'black',
			cursor: 'pointer'
		}
	}
});

function SearchResultItem(props) {
	const oldName = JSON.parse(sessionStorage.getItem('SearchUser ' + props.user.publicId)).name;
	const oldRoles = JSON.parse(sessionStorage.getItem('SearchUser ' + props.user.publicId)).roles;
	const oldActive = JSON.parse(sessionStorage.getItem('SearchUser ' + props.user.publicId)).active;
	const [valueChanged, setValueChanged] = useState(false);
	const [name, setName] = useState(oldName);
	const [roles, setRoles] = useState(oldRoles);
	const [active, setActive] = useState(oldActive);
	const [updateSuccessful, setUpdateSuccessful] = useState(oldActive);
	const [openDeleteModal, setOpenDeleteModal] = useState(false);
	const [openViewModal, setOpenViewModal] = useState(false);
	const classes = useStyles();

	useEffect(() => {
		if (props.deletePublicId === props.user.publicId) {
			props.reloadSearch();
		}
	}, [props.deleteStatus, props.deletePublicId]);

	useEffect(() => {
		if (props.updatedUserStatus === 'success' && props.updatedUserPublicId === props.user.publicId) {
			setUpdateSuccessful(true);
		}
	}, [
		props.updatedUserStatus,
		props.updatedUserPublicId,
		props.updatedUserName,
		props.updatedUserEmail,
		props.udatedUserVerified,
		props.updatedUserActive,
		props.updatedUserRoles
	]);

	useEffect(() => {
		let sameName = oldName.toUpperCase() === name.toUpperCase();
		let sameActive = oldActive === active;
		let sameRole = true;

		if ((oldRoles !== undefined) & (roles !== undefined)) {
			if (oldRoles.length !== roles.length) {
				sameRole = false;
			} else {
				oldRoles.forEach((e) => {
					if (roles.map((r) => r.toUpperCase).indexOf(e.toUpperCase) === -1) {
						sameRole = false;
					}
				});
				roles.forEach((e) => {
					if (oldRoles.map((r) => r.toUpperCase).indexOf(e.toUpperCase) === -1) {
						sameRole = false;
					}
				});
			}
		}

		if (sameName && sameActive && sameRole) {
			setValueChanged(false);
		} else {
			setValueChanged(true);
			setUpdateSuccessful(false);
		}
	}, [
		name,
		roles,
		active,
		props.updatedUserStatus,
		props.updatedUserPublicId,
		props.updatedUserName,
		props.updatedUserEmail,
		props.udatedUserVerified,
		props.updatedUserActive,
		props.updatedUserRoles
	]);

	const updateUserHandler = () => {
		if (valueChanged && !updateSuccessful) {
			props.updateUser(props.authToken, props.user.publicId, name, roles, active);
		}
	};

	const deleteUserHandler = () => {
		props.deleteUser(props.authToken, props.user.publicId);
	};

	const showDeleteUserModal = () => {
		setOpenDeleteModal(true);
	};

	const UserDetails = () => {
		let userDetails = buildViewUserDetailsForSearch(props.userDetails);

		return (
			<>
				{userDetails !== undefined && Object.keys(userDetails).length > 0 && (
					<Grid item>
						<MyTable data={userDetails} {...props} />
					</Grid>
				)}
			</>
		);
	};

	const showUserDetails = () => {
		props.viewUser(props.authToken, props.user.publicId);
		setOpenViewModal(true);
	};

	return (
		<>
			<Grid container style={{ height: '80px', borderBottom: '2px #e3e3e3 solid' }}>
				{/* <Grid item md={2}>
					<OutputText value={props.user.publicId} />
				</Grid> */}
				<Grid item md={3}>
					<OutputText value={props.user.email} />
				</Grid>
				<Grid item md={1}>
					<OutputText value={props.user.emailVerified == true ? 'Yes' : 'No'} />
				</Grid>
				<Grid item md={3}>
					<MyInputField value={name} setvalue={setName} />
				</Grid>
				<Grid item md={2}>
					<MyCheckbox options={props.roleOptions} value={roles} setValue={setRoles}></MyCheckbox>
				</Grid>
				<Grid item md={2}>
					<InputRadioButton
						label1='Yes'
						value1={true}
						label2='No'
						value2={false}
						value={active}
						setValue={setActive}
					/>
				</Grid>
				<Grid item xs={1}>
					<Grid
						container
						justify='space-around'
						alignItems='center'
						style={{ height: '100%', textAlign: 'center' }}>
						<Grid item>
							<CheckIcon
								fontSize={valueChanged && !updateSuccessful ? 'large' : 'default'}
								color={valueChanged && !updateSuccessful ? 'action' : 'disabled'}
								onClick={updateUserHandler}
								className={valueChanged && !updateSuccessful ? classes.hover : null}></CheckIcon>
						</Grid>
						<Grid item>
							<VisibilityOutlined
								fontSize='default'
								color='action'
								onClick={showUserDetails}
								className={classes.hover}></VisibilityOutlined>
						</Grid>
						<Grid item>
							<DeleteIcon
								fontSize='default'
								color='action'
								className={classes.hover}
								onClick={showDeleteUserModal}></DeleteIcon>
						</Grid>
					</Grid>
				</Grid>
			</Grid>
			<>
				{openDeleteModal &&
				<MyModal
					open={openDeleteModal}
					setOpen={setOpenDeleteModal}
					okLabel='Confirm Delete'
					noLabel='Cancel'
					title='Confirm Deletion?'
					okHandler={deleteUserHandler}
					message={`You are going to delete user with email ${props.user.email}. Do you confirm to continue? `}
				/>
				}
				{openViewModal &&
				<MyModal
					open={openViewModal}
					setOpen={setOpenViewModal}
					title='User Details'
					displayContent={<UserDetails/>}
					okLabel='Close'
				/>
				}
			</>
		</>
	);
}

const mapStateToProps = (state) => ({
	authToken: state.authReducer.authToken,

	updatedUserStatus: state.updateUserReducer.status,
	updatedUserPublicId: state.updateUserReducer.publicId,
	updatedUserName: state.updateUserReducer.name,
	updatedUserEmail: state.updateUserReducer.email,
	updatedUserVerified: state.updateUserReducer.verified,
	updatedUserRoles: state.updateUserReducer.roles,
	updatedUserActive: state.updateUserReducer.active,

	deleteStatus: state.deleteUserReducer.status,
	deletePublicId: state.deleteUserReducer.publicId,

	userDetails: state.viewUserReducer.user,
	userDetailsErrorId: state.viewUserReducer.errorId,
	userDetailsErrorMessage: state.viewUserReducer.errorMessage,
	userDetailsMessage: state.viewUserReducer.message,
	userDetailsStatus: state.viewUserReducer.status
});

export default connect(mapStateToProps, { updateUser, viewUser })(SearchResultItem);
