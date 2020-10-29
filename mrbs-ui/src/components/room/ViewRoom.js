import React, {useState, useEffect} from 'react';
import {connect} from 'react-redux';
import {viewRoom, listRoomAdmins, listRoomUsers, listRoomBookings, deleteRoom} from 'myRedux/actions/room';
import {viewOrg} from 'myRedux/actions/org';
import requireAuth from 'hoc/requireAuth';
import layout2 from 'components/layout/layout2';
import {
    Grid,
    Typography,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableRow,
    Paper,
    Card,
} from '@material-ui/core';
import {MyButton, StatusMessage, MyModal} from 'components/element';
import {ROUTE_ROOM_EDIT, ROUTE_ROOM_SEARCH} from 'utils/const';
import {signOut} from "../../myRedux/actions/auth";

const ViewRoom = (props) => {
    const returnHander = () => {
        props.history.goBack();
    };
    const clickDeleteHandler = () => {
        setOpenDeleteModal(true);
    };
    const [openDeleteModal, setOpenDeleteModal] = useState(false);

    const submitDeleteHandler = () => {
        props.deleteRoom(props.authToken, props.room.publicId, props.room.name, props.history.push(ROUTE_ROOM_SEARCH));
        setOpenDeleteModal(false);
    };

    const allowEditDeleteRoom = () => {
        if (sessionStorage.getItem("roles") === 'SYSADM') {
            return true;
        }
        let userPublicId = sessionStorage.getItem("userPublicId");
        console.log(userPublicId, props.admins)
        if (props.admins && props.admins.length > 0 && props.admins.filter(e => e.publicId === userPublicId).length > 0) {
            return true;
        }
        return false;
    }

    const isRoomUserOrAdmin = () => {
        let userPublicId = sessionStorage.getItem("userPublicId");
        if (props.room && props.room.users && props.room.users.filter(e => e === userPublicId).length > 0) {
            return true;
        }
        console.log(props.room.admins)
        if (props.room && props.room.admins && props.room.admins.filter(e => e === userPublicId).length > 0) {
            return true;
        }
        return false;
    }

    useEffect(() => {
        const param = new URLSearchParams(window.location.search);
        props.viewRoom(props.authToken, param.get('roomId'));
        props.listRoomAdmins(props.authToken, param.get('roomId'));
        props.listRoomUsers(props.authToken, param.get('roomId'));
        // props.listRoomBookings(props.authToken, param.get('roomId'));

    }, []);

    useEffect(() => {
        if (props.room.organization) {
            props.viewOrg(props.authToken, props.room.organization);
        }
    }, [props.room.organization]);

    return (
        <>
            <Grid item xs={12} sm={11} md={10} lg={9} xl={8}>
                <Grid container style={{marginBottom: '3em'}} justify='center'>
                    <Typography align='center' variant='h5'>
                        View Room Details
                    </Typography>
                </Grid>
                {props.viewRoomErrorMessage && (
                    <Grid container alignItems='center' justify='space-around' style={{marginBottom: '1em'}}>
                        <Grid item xs={8}>
                            <StatusMessage type='failed' errorMessage={props.viewRoomErrorMessage}/>
                        </Grid>
                    </Grid>
                )}


                <Grid container alignItems='center' justify='space-around' style={{marginBottom: '1em'}}>
                    <Grid item xs={8}>
                        {props.room.images && props.room.images.map((e, i) => <Card><img key={i}
                                                                                         src={`data:image/jpeg;base64, ${e}`}
                                                                                         height="100%" width="100%"
                                                                                         style={{objectFit: "cover"}}/></Card>)}

                    </Grid>
                </Grid>
                <Grid container alignItems='center' justify='space-around' style={{marginBottom: '1em'}}>
                    <Grid item xs={8}>
                        <TableContainer component={Paper}>
                            <Table>
                                <TableBody>
                                    <TableRow>
                                        <TableCell>Organization</TableCell>
                                        <TableCell>{props.organization}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>Room Name</TableCell>
                                        <TableCell>{props.room.name}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>Capacity</TableCell>
                                        <TableCell>{props.room.capacity}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>Facilities</TableCell>
                                        <TableCell>{props.room.facilities}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>Description</TableCell>
                                        <TableCell>{props.room.description}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>Active</TableCell>
                                        <TableCell>{props.room.active ? 'Yes' : 'No'}</TableCell>
                                    </TableRow>
                                </TableBody>
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
                            <Typography variant='h6'>Room Administrator</Typography>
                        </Grid>
                        {!isRoomUserOrAdmin() &&
                        <Grid item xs={8}>
                            <Typography variant='body1' style={{color: "red"}}>You are not yet enrolled in this room.
                                Please email the below room administrator to request accessing this room.</Typography>
                        </Grid>}
                    </Grid>
                )}

                <Grid
                    container
                    alignItems='center'
                    justify='space-around'
                    style={{marginBottom: '1em'}}>
                    <Grid item xs={8}>
                        <TableContainer component={Paper}>
                            <Table>
                                <TableBody>
                                    <TableRow>
                                        <TableCell>Name</TableCell>
                                        <TableCell>Email</TableCell>
                                        <TableCell>Active</TableCell>
                                        <TableCell>Email Verified</TableCell>
                                    </TableRow>
                                    {props.admins &&
                                    props.admins.length > 0 &&
                                    props.admins.map((e, i) => (
                                        <TableRow
                                            key={i}>
                                            <TableCell>{e.name}</TableCell>
                                            <TableCell>
                                                <a style={{color: "black"}}
                                                   href={"mailto:" + e.email + "?subject=[MRBS] Enrollment Request: " + props.organization + " - " + props.room.name + "" +
                                                   "&body=Hi " + e.name + ", I would like to be enrolled in " + props.organization + " - " + props.room.name + ". Regards and Thanks!"}>
                                                    {e.email}</a>
                                            </TableCell>
                                            <TableCell>{e.active ? 'Yes' : 'No'}</TableCell>
                                            <TableCell>{e.emailVerified ? 'Yes' : 'No'}</TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </Grid>
                </Grid>

                {props.users && props.users.length > 0 && (<>
                        <Grid
                            container
                            alignItems='center'
                            justify='space-around'
                            style={{marginBottom: '1em', marginTop: '2em'}}>
                            <Grid item xs={8}>
                                <Typography variant='h6'>Room Users</Typography>
                            </Grid>
                        </Grid>

                        <Grid container alignItems='center' justify='space-around' style={{marginBottom: '1em'}}>
                            <Grid item xs={8}>
                                <TableContainer component={Paper}>
                                    <Table>
                                        <TableBody>
                                            {props.users &&
                                            props.users.length > 0 &&
                                            <TableRow>
                                                <TableCell>Name</TableCell>
                                                <TableCell>Email</TableCell>
                                                <TableCell>Active</TableCell>
                                                <TableCell>Email Verified</TableCell>
                                            </TableRow>}
                                            {props.users &&
                                            props.users.length > 0 &&
                                            props.users.map((e, i) => (
                                                <TableRow key={i}>
                                                    <TableCell>{e.name}</TableCell>
                                                    <TableCell>{e.email}</TableCell>
                                                    <TableCell>{e.active ? 'Yes' : 'No'}</TableCell>
                                                    <TableCell>{e.emailVerified ? 'Yes' : 'No'}</TableCell>
                                                </TableRow>
                                            ))}
                                        </TableBody>
                                    </Table>
                                </TableContainer>
                            </Grid>
                        </Grid></>
                )}

                {props.rooms &&
                props.room.bookings.length > 0 &&
                props.room.bookings.map((e) => (
                    <Grid container alignItems='center' justify='space-around' style={{marginBottom: '1em'}}>
                        <Grid item xs={8}>
                            <TableContainer component={Paper}>
                                <TableBody>
                                    <Table>
                                        <TableRow>
                                            <TableCell>From </TableCell>
                                            <TableCell>{e.fromTime}</TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>To</TableCell>
                                            <TableCell>{e.toTime}</TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>BookedBy</TableCell>
                                            <TableCell>{e.bookedBy}</TableCell>
                                        </TableRow>
                                    </Table>
                                </TableBody>
                            </TableContainer>
                        </Grid>
                    </Grid>
                ))}
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
                            {allowEditDeleteRoom() &&
                            <Grid item>
                                <MyButton label='Delete' width='10em' onClick={clickDeleteHandler}/>
                                <MyButton
                                    label='Edit'
                                    width='10em'
                                    onClick={() =>
                                        props.history.push(`${ROUTE_ROOM_EDIT}?roomId=${props.room.publicId}`)
                                    }
                                />
                            </Grid>}
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
                message={`You are going to delete Room ${props.room.name}. Do you confirm to continue? `}
            />
        </>
    );
};

const mapStateToProps = (state) => ({
    authToken: state.authReducer.authToken,
    room: state.roomViewReducer.room,
    admins: state.roomListAdminsReducer.admins,
    users: state.roomListUsersReducer.users,
    bookings: state.roomListBookingsReducer.bookings,
    organization: state.viewOrgReducer.name,
    viewRoomStatus: state.roomViewReducer.status,
    viewRoomErrorMessage: state.roomViewReducer.errorMessage,
    listAdminsStatus: state.roomListAdminsReducer.status,
    listAdminsErrorMessage: state.roomListAdminsReducer.errorMessage,
    listUsersStatus: state.roomListUsersReducer.status,
    listUsersErrorMessage: state.roomListUsersReducer.errorMessage,
    listBookingsStatus: state.roomListBookingsReducer.status,
    listBookingsErrorMessage: state.roomListBookingsReducer.errorMessage
});

export default connect(mapStateToProps, {
    viewRoom,
    listRoomAdmins,
    listRoomUsers,
    listRoomBookings,
    viewOrg,
    deleteRoom
})(requireAuth(layout2(ViewRoom)));
