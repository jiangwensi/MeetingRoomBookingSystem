import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Grid } from '@material-ui/core';
import { searchOrg, deleteOrg } from 'myRedux/actions/org';
import SearchBar from 'components/org/SearchBar';
import requireAuth from 'hoc/requireAuth';
import { MyDataList, MyButton, MyModal, StatusMessage } from 'components/element';
import { ROUTE_ORG_CREATE, ROUTE_ORG_EDIT, ROUTE_ORG_VIEW } from 'utils/const';
import layout2 from 'components/layout/layout2';

const SearchOrg = (props) => {
	const [deletePublicId, setDeletePublicId] = useState('');
	const [deleteOrgName, setDeleteOrgName] = useState('');
	const [ignoreData, setIgnoreData] = useState([]);
	const [organizations, setOrganizations] = useState([]);

	const createHandler = () => {
		props.history.push(ROUTE_ORG_CREATE);
	};

	const searchHandler = (name, active) => {
		props.searchOrg(props.authToken, name, active);
	};

	const clickDeleteIconHandler = (deletePublicId, deleteOrgName) => {
		setDeletePublicId(deletePublicId);
		setDeleteOrgName(deleteOrgName);
		setOpenDeleteModal(true);
	};

	const submitDeleteHandler = () => {
		props.deleteOrg(props.authToken, deletePublicId, deleteOrgName);
		setOpenDeleteModal(false);
	};


	useEffect(() => {
		props.searchOrg(props.authToken, '', '');
	}, [props.customCreateMessage, props.customDeleteMessage]);

	useEffect(()=> {
		setOrganizations(props.organizations);
	},[props.organizations])

	const [openDeleteModal, setOpenDeleteModal] = useState(false);

	const [orgData, setOrgData] = useState([]);
	useEffect(() => {
		if (organizations && organizations.length > 0) {
			const orgs = [];
			organizations.forEach((e) => {
				const org = {};
				org['Public Id'] = e['publicId'];
				org['Name'] = e['name'];
				org['Description'] = e['description'];
				org['Active'] = e['active'] === true ? 'Yes' : 'No';
				setIgnoreData([...ignoreData, 'Public Id']);
				org['Actions'] = [
					{
						icon: 'view',
						clickHandler: () => props.history.push(`${ROUTE_ORG_VIEW}?orgId=${e['publicId']}`)
					}
				];
				orgs.push(org);
			});
			setOrgData(orgs);
		}
	}, [props.status, organizations]);

	return (
		<>
			<Grid container justify='center' alignItems='center' style={{ width: '100%' }}>
				<Grid item xs={12} sm={11} md={10} lg={9} xl={8}>
					<SearchBar searchHandler={searchHandler} />
				</Grid>
				<Grid item xs={12} sm={11} md={10} lg={9} xl={8} style={{ marginTop: '3em' }}>
					<MyButton label='Create Organization' width='15em' onClick={createHandler}></MyButton>
				</Grid>

				{props.customCreateMessage && (
					<Grid item xs={12} sm={11} md={10} lg={9} xl={8} style={{ marginTop: '3em' }}>
						<StatusMessage type='success' message={props.customCreateMessage} />
					</Grid>
				)}
				{props.customDeleteMessage && (
					<Grid item xs={12} sm={11} md={10} lg={9} xl={8} style={{ marginTop: '3em' }}>
						<StatusMessage type='success' message={props.customDeleteMessage} />
					</Grid>
				)}
				{props.deleteErrorMessage && (
					<Grid item xs={12} sm={11} md={10} lg={9} xl={8} style={{ marginTop: '3em' }}>
						<StatusMessage type='failed' errorMessage={props.deleteErrorMessage} />
					</Grid>
				)}

				<Grid item xs={12} sm={11} md={10} lg={9} xl={8} style={{ marginTop: '1em' }}>
					{orgData !== undefined && orgData.length > 0 && (
						<MyDataList data={orgData} ignoreData={ignoreData}></MyDataList>
					)}
				</Grid>
			</Grid>

			<MyModal
				open={openDeleteModal}
				setOpen={setOpenDeleteModal}
				okLabel='Confirm Delete'
				noLabel='Cancel'
				title='Confirm Deletion?'
				okHandler={submitDeleteHandler}
				message={`You are going to delete Org ${deleteOrgName}. Do you confirm to continue? `}
			/>
		</>
	);
};

const mapStateToProps = (state) => ({
	authToken: state.authReducer.authToken,
	status: state.searchOrgReducer.status,
	organizations: state.searchOrgReducer.organizations,
	customCreateMessage: state.createOrgReducer.customMessage,
	customDeleteMessage: state.deleteOrgReducer.customMessage,
	deleteErrorMessage: state.deleteOrgReducer.errorMessage
});

export default connect(mapStateToProps, { searchOrg, deleteOrg })(requireAuth(layout2(SearchOrg)));
