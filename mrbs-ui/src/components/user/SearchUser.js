import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';

import { Grid } from '@material-ui/core';

import {searchUser, listAllRoles, deleteUser} from 'myRedux/actions/user';
import layout2 from 'components/layout/layout2';
import { StatusMessage } from 'components/element';
import SearchResultTitleBar from 'components/user/SearchResultTitleBar';
import SearchBar from 'components/user/SearchBar';
import SearchResultItem from 'components/user/SearchResultItem';
import requireAuth from 'hoc/requireAuth';

function SearchUser(props) {
	const [nameFilter, setNameFilter] = useState('');
	const [emailFilter, setEmailFilter] = useState('');
	const [rolesFilter, setRolesFilter] = useState([]);
	const [activeFilter, setActiveFilter] = useState([]);
	const [verifiedFilter, setVerifiedFilter] = useState([]);
	const searchHandler = (name, email, roles, active, verified) => {
		setNameFilter(name);
		setEmailFilter(email);
		setRolesFilter(roles);
		setActiveFilter(active);
		setVerifiedFilter(verified);

		if (active.includes('Yes')) {
			activeFilter.push(true);
		}
		if (active.includes('No')) {
			activeFilter.push(false);
		}

		if (verified.includes('Yes')) {
			verifiedFilter.push(true);
		}
		if (verified.includes('No')) {
			verifiedFilter.push(false);
		}
		props.searchUser(
			sessionStorage.getItem('authToken'),
			nameFilter,
			emailFilter,
			rolesFilter,
			activeFilter,
			verifiedFilter,
			false
		);
	};
	const reloadSearchHandler = () => {
		props.searchUser(
			sessionStorage.getItem('authToken'),
			nameFilter,
			emailFilter,
			rolesFilter,
			activeFilter,
			verifiedFilter,
			false
		);
	};

	useEffect(() => {
		props.searchUser(sessionStorage.getItem('authToken'),null,null,null,null,null,false);
		props.listAllRoles(sessionStorage.getItem('authToken'));
	}, []);

	return (
		<>
			{sessionStorage.getItem('roleOptions') && (
				<Grid item xs={12} sm={11} md={10}>
					<SearchBar
						options={JSON.parse(sessionStorage.getItem('roleOptions'))}
						searchHandler={searchHandler}
					/>
				</Grid>
			)}
			{
				props.deleteErrorMessage && (
					<StatusMessage type='failed' errorMessage={props.deleteErrorMessage} />
				)
				// <ErrorBox errorMessage={props.errorMessage}  />
			}
			{
				props.status === 'failed' && props.errorMessage && (
					<StatusMessage type='failed' errorMessage={props.errorMessage} />
				)
				// <ErrorBox errorMessage={props.errorMessage}  />
			}
			{
				/^\d{3}$/.test(props.status) && <StatusMessage type='failed' errorMessage="Couldn\'t get user list" />
				// <ErrorBox errorMessage="Couldn't get user list" />
			}
			<Grid item xs={12} sm={11} md={10}>
				<SearchResultTitleBar></SearchResultTitleBar>
			</Grid>

			{props.status === 'success' && (
				<Grid item xs={12} sm={11} md={10}>
					{props.users.map((e,i) => {
						return (

							<Grid container key={i}>
								<SearchResultItem
									key={e.publicId}
									user={e}
									roleOptions={props.roleOptions}
									reloadSearch={reloadSearchHandler}
									deleteUser={props.deleteUser}
									{...props}></SearchResultItem>
							</Grid>
						);
					})}
				</Grid>
			)}
		</>
	);
}

const mapStateToProps = (state) => {
	return {
		errorId: state.searchUserReducer.errorId,
		errorMessage: state.searchUserReducer.errorMessage,
		deleteErrorMessage:state.deleteUserReducer.errorMessage,
		message: state.searchUserReducer.message,
		status: state.searchUserReducer.status,
		users: state.searchUserReducer.users,
		roleOptions: state.listAllRoleReducer.roles
	};
};

export default connect(mapStateToProps, { searchUser, deleteUser,listAllRoles })(layout2(requireAuth(SearchUser)));
