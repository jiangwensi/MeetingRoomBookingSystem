import React, {useState, useEffect} from 'react';
import {connect} from 'react-redux';
import layout2 from 'components/layout/layout2';
import requireAuth from 'hoc/requireAuth';
import {
    Grid,
    Typography,
    Table,
    TableCell,
    TableContainer,
    TableRow,
    Paper
} from '@material-ui/core';
import {MyButton, StatusMessage, MyModal} from 'components/element';
import {viewOrg, listOrgAdmins,  deleteOrg} from 'myRedux/actions/org';
import {ROUTE_ORG_EDIT, ROUTE_ORG_SEARCH} from 'utils/const';

const ViewOrg = (props) => {
    const [openDeleteModal, setOpenDeleteModal] = useState(false);

    const clickDeleteHandler = () => {
        setOpenDeleteModal(true);
    };

    const submitDeleteHandler = () => {
        props.deleteOrg(props.authToken, props.publicId, props.name, props.history.push(ROUTE_ORG_SEARCH));
        setOpenDeleteModal(false);
    };
    const returnHander = () => {
        props.history.goBack();
    };
    // const orgSimpleFields = {};
    useEffect(() => {
        const param = new URLSearchParams(window.location.search);
        props.viewOrg(props.authToken, param.get('orgId'));
        props.listOrgAdmins(props.authToken, param.get('orgId'));
    }, []);
    return (
        <>
            <Grid item xs={12} sm={11} md={10} lg={9} xl={8}>
                <Grid container style={{marginBottom: '3em'}} justify='center'>
                    <Typography align='center' variant='h5'>
                        View Organization Details
                    </Typography>
                </Grid>
                {props.errorMessage && (
                    <Grid container alignItems='center' justify='space-around' style={{marginBottom: '1em'}}>
                        <Grid item xs={8}>
                            <StatusMessage type='failed' errorMessage={props.errorMessage}/>
                        </Grid>
                    </Grid>
                )}

                <Grid container alignItems='center' justify='space-around' style={{marginBottom: '1em'}}>
                    <Grid item xs={8}>
                        <TableContainer component={Paper}>
                            <Table>
                                <TableRow>
                                    <TableCell>Org Name</TableCell>
                                    <TableCell>{props.name}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>Description</TableCell>
                                    <TableCell>{props.description}</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>Active</TableCell>
                                    <TableCell>{props.active ? 'Yes' : 'No'}</TableCell>
                                </TableRow>
                            </Table>
                        </TableContainer>
                    </Grid>
                </Grid>
                {props.admins && props.admins.length > 0 && (
                    <Grid
                        container
                        alignItems='center'
                        justify='space-around'
                        style={{marginBottom: '1em', marginTop: '2em'}}>
                        <Grid item xs={8}>
                            <Typography variant='h6'>Organization Administrator</Typography>
                        </Grid>
                    </Grid>
                )}
                <Grid container alignItems='center' justify='space-around' style={{marginBottom: '1em'}}>
                    <Grid item xs={8}>
                        <TableContainer component={Paper}>
                            <Table>
                                <TableRow>
                                    <TableCell>Name</TableCell>
                                    <TableCell>Email</TableCell>
                                    <TableCell>Active</TableCell>
                                    <TableCell>Email Verified</TableCell>
                                    <TableCell></TableCell>
                                </TableRow>
                                {props.admins &&
                                props.admins.length > 0 &&
                                props.admins.map((e,i) => (
                                    <React.Fragment key={i}>
                                        <TableRow>
                                            <TableCell>{e.name}</TableCell>
                                            <TableCell>{e.email}</TableCell>
                                            <TableCell>{e.active ? 'Yes' : 'No'}</TableCell>
                                            <TableCell>{e.emailVerified ? 'Yes' : 'No'}</TableCell>
                                            <TableCell></TableCell>
                                        </TableRow>
                                    </React.Fragment>
                                ))}


                            </Table>
                        </TableContainer>
                    </Grid>
                </Grid>
                {props.rooms && props.rooms.length > 0 && (
                    <Grid
                        container
                        alignItems='center'
                        justify='space-around'
                        style={{marginBottom: '1em', marginTop: '2em'}}>
                        <Grid item xs={8}>
                            <Typography variant='h6'>Organization Rooms</Typography>
                        </Grid>
                    </Grid>
                )}

                <Grid
                    container
                    alignItems='center'
                    justify='space-around'
                    style={{marginBottom: '1em', marginTop: '2em'}}>
                    <Grid item xs={8} style={{padding: 0}}>
                        <Grid container justify='space-between' style={{width: '100%'}}>
                            <Grid item>
                                <MyButton label='Cancel' width='10em' onClick={returnHander}/>
                            </Grid>
                            <Grid item>
                                <MyButton label='Delete' width='10em' onClick={clickDeleteHandler}/>
                                <MyButton
                                    label='Edit'
                                    width='10em'
                                    onClick={() => props.history.push(`${ROUTE_ORG_EDIT}?orgId=${props.publicId}`)}
                                />
                            </Grid>
                        </Grid>
                    </Grid>
                </Grid>
            </Grid>
            <MyModal
                open={openDeleteModal}
                setOpen={setOpenDeleteModal}
                okLabel='Confirm Delete'
                noLabel='Cancel'
                title='Confirm Deletion?'
                okHandler={submitDeleteHandler}
                message={`You are going to delete Org ${props.name}. Do you confirm to continue? `}
            />
        </>
    );
};

const mapStateToProps = (state) => {
    return {
        authToken: state.authReducer.authToken,
        status: state.viewOrgReducer.status,
        errorMessage: state.viewOrgReducer.errorMessage,
        publicId: state.viewOrgReducer.publicId,
        name: state.viewOrgReducer.name,
        active: state.viewOrgReducer.active,
        description: state.viewOrgReducer.description,
        admins: state.listOrgAdminsReducer.admins,
        rooms: state.listOrgAdminsReducer.rooms
    };
};

export default connect(mapStateToProps, {viewOrg, listOrgAdmins, deleteOrg})(
    requireAuth(layout2(ViewOrg))
);
