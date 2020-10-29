import React, {useState, useEffect} from 'react';
import {connect} from 'react-redux';
import {editRoom, viewRoom, listRoomAdmins, listRoomUsers, listRoomBookings, deleteRoom} from 'myRedux/actions/room';
import {viewOrg} from 'myRedux/actions/org';
import {searchUser} from 'myRedux/actions/user';
import requireAuth from 'hoc/requireAuth';
import layout2 from 'components/layout/layout2';
import {DeleteOutline, AddCircleOutlineOutlined} from '@material-ui/icons';
import {validateNotEmpty, validateNumber, validateEmail} from 'utils/validation';
import {
    Grid,
    Typography,
    TextField,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableRow,
    makeStyles,
    Paper
} from '@material-ui/core';
import {MyButton, InputRadioButton, StatusMessage} from 'components/element';
import {ROUTE_ROOM_SEARCH} from 'utils/const';

const useStyles = makeStyles((theme) => ({
    tableCellNoBorder: {
        borderBottom: 'none'
    },
    hover: {
        '&:hover': {
            color: 'black',
            cursor: 'pointer'
        }
    }
}));
const EditRoom = (props) => {
    const classes = useStyles();

    //useState
    const [roomName, setRoomName] = useState('');
    const [capacity, setCapacity] = useState(0);
    const [facilities, setFacilities] = useState('');
    const [description, setDescription] = useState('');
    const [active, setActive] = useState(null);
    const [roomAdmins, setRoomAdmins] = useState([]);
    const [roomUsers, setRoomUsers] = useState([]);
    const [searchingAdmin, setSearchingAdmin] = useState(true);
    const [blockedTimeslots, setBlockedTimeslots] = useState([]);

    const [newAdminEmail, setNewAdminEmail] = useState('');
    const [newUserEmail, setNewUserEmail] = useState('');

    const [roomNameHelper, setRoomNameHelper] = useState('');
    const [capacityHelper, setCapacityHelper] = useState(0);
    const [facilitiesHelper, setFacilitiesHelper] = useState('');
    const [descriptionHelper, setDescriptionHelper] = useState('');
    // const [roomAdminsHelper, setRoomAdminsHelper] = useState([]);
    const [newAdminEmailHelper, setNewAdminEmailHelper] = useState('');
    const [newUserEmailHelper, setNewUserEmailHelper] = useState('');
    const [roomImages, setRoomImages] = useState([]);
    const [roomImagesNameList, setRoomImagesNameList] = useState('');

    //functions

    const onRoomImageChange = (event) => {
        console.log("onRoomImageChange")
        let image = event.target.files[0]
        console.log(roomImages);
        setRoomImages([...roomImages, image])
    }

    const submitHandler = () => {
        if (validateRoomName() && validateCapacity() && validateRoomAdmins()) {
            props.editRoom(
                props.authToken,
                props.room.publicId,
                roomName,
                capacity,
                facilities,
                description,
                active,
                roomAdmins,
                roomUsers,
                blockedTimeslots,
                () => props.history.push(ROUTE_ROOM_SEARCH)
            );
        }
    };

    const returnHander = () => {
        props.history.goBack();
    };

    const addNewAdmin = () => {
        if (!validateEmail(newAdminEmail)) {
            setNewAdminEmailHelper('Email is invalid');
        } else {
            setSearchingAdmin(true);
            props.searchUser(props.authToken, null, newAdminEmail);
        }
    };

    const addNewUser = () => {
        if (!validateEmail(newUserEmail)) {
            setNewUserEmailHelper('Email is invalid');
        } else {
            setSearchingAdmin(false);
            props.searchUser(props.authToken, null, newUserEmail);
        }
    };

    const deleteAdminHandler = (e) => {
        if (roomAdmins !== null && roomAdmins.length > 0) {
            setRoomAdmins(roomAdmins.filter((a) => a.publicId !== e.publicId));
        }
    };

    const deleteUserHandler = (e) => {
        if (roomUsers !== null && roomUsers.length > 0) {
            setRoomUsers(roomUsers.filter((a) => a.publicId !== e.publicId));
        }
    };

    const addEmptyBlockedTimeslot = () => {
        console.log('addEmptyBlockedTimeslot');
        setBlockedTimeslots([...blockedTimeslots, {type: '', day: '', timeFrom: '', timeTo: ''}]);
    };

    const deleteTimeslotHandler = (deleteTimeslotIndex) => {
        setBlockedTimeslots(blockedTimeslots.filter((e, i) => i !== deleteTimeslotIndex));
    };

    //validation
    const validateRoomName = () => {
        if (validateNotEmpty(roomName)) {
            setRoomNameHelper('');
            return true;
        }
        setRoomNameHelper('Room name must not be empty');
        return false;
    };

    const validateCapacity = () => {
        if (validateNumber(capacity)) {
            setCapacityHelper('');
            return true;
        }
        setCapacityHelper('Capactiy must be a number');
        return false;
    };

    const validateRoomAdmins = () => {
        if (roomAdmins && roomAdmins.length > 0) {
            setNewAdminEmailHelper('');
            return true;
        }
        setNewAdminEmailHelper('Room admin must not be empty');
        return false;
    };

    const skipValidation = () => {
        return !roomName && !capacity && !facilities && !description && !roomAdmins && !roomUsers;
    };

    const removeRoomImage = (image) => {
        let index = roomImages.indexOf(image);
        roomImages.splice(index, 1);
        setRoomImages([...roomImages]);
    }


    //useEffect
    useEffect(() => {
        if (searchingAdmin) {
            if (props.searchUserStatus === 'success') {
                if (props.searchedUser === undefined) {
                    setNewAdminEmailHelper('Unable to find a user with this email');
                } else {
                    let adminExists = false;
                    if (roomAdmins !== null && roomAdmins.length > 0) {
                        roomAdmins.forEach((a) => {
                            if (a.publicId === props.searchedUser.publicId) {
                                setNewAdminEmailHelper('This user is already in the admin list');
                                adminExists = true;
                            }
                        });
                    }
                    if (!adminExists) {
                        setNewAdminEmail('');
                        setNewAdminEmailHelper('');

                        const newAdmin = {};
                        newAdmin.publicId = props.searchedUser.publicId;
                        newAdmin.name = props.searchedUser.name;
                        newAdmin.email = props.searchedUser.email;
                        newAdmin.active = props.searchedUser.active;
                        newAdmin.verified = props.searchedUser.emailVerified;
                        setRoomAdmins([...roomAdmins, newAdmin]);
                        // admins.push(newAdmin);
                    }
                }
            } else if (props.searchUserStatus === 'failed') {
                setNewAdminEmailHelper('Unable to find user, please try again later');
            }
        } else {
            if (props.searchUserStatus === 'success') {
                if (!props.searchedUser) {
                    setNewUserEmailHelper('Unable to find a user with this email');
                } else {
                    let userExists = false;
                    if (roomUsers && roomUsers.length > 0) {
                        roomUsers.forEach((a) => {
                            if (a.publicId === props.searchedUser.publicId) {
                                setNewUserEmailHelper('This user is already in the user list');
                                userExists = true;
                            }
                        });
                    }
                    if (!userExists) {
                        setNewUserEmail('');
                        setNewUserEmailHelper('');

                        const newUser = {};
                        newUser.publicId = props.searchedUser.publicId;
                        newUser.name = props.searchedUser.name;
                        newUser.email = props.searchedUser.email;
                        newUser.active = props.searchedUser.active;
                        newUser.verified = props.searchedUser.emailVerified;
                        setRoomUsers([...roomUsers, newUser]);
                        // admins.push(newAdmin);
                    }
                }
            } else if (props.searchUserStatus === 'failed') {
                setNewUserEmailHelper('Unable to find user, please try again later');
            }
        }
    }, [props.searchedUser, props.searchUserStatus, props.searchUserErrorMessage]);

    useEffect(() => {
        if (!skipValidation()) {
            validateRoomName();
        }
    }, [roomName]);

    useEffect(() => {
        if (!skipValidation()) {
            validateCapacity();
        }
    }, [capacity]);

    useEffect(() => {
        if (!skipValidation()) {
            validateRoomAdmins();
        }
    }, [roomAdmins]);

    useEffect(() => {
        if (props.room.name) {
            setRoomName(props.room.name);
        }
    }, [props.room.name]);

    useEffect(() => {
        if (props.room.capacity) {
            setCapacity(props.room.capacity ? props.room.capacity.toString() : 0);
        }
    }, [props.room.capacity]);

    useEffect(() => {
        if (props.room.facilities) {
            setFacilities(props.room.facilities);
        }
    }, [props.room.facilities]);

    useEffect(() => {
        if (props.room.description) {
            setDescription(props.room.description);
        }
    }, [props.room.description]);

    useEffect(() => {
        if (props.room.active) {
            setActive(props.room.active);
        }
    }, [props.room.active]);

    useEffect(() => {
        if (props.room.admins) {
            setRoomAdmins(props.admins);
        }
    }, [props.admins]);

    useEffect(() => {
        console.log(props.room.blockedTimeslots);
        if (props.room.blockedTimeslots) {
            setBlockedTimeslots(props.room.blockedTimeslots);
        }
    }, [props.room.blockedTimeslots]);

    useEffect(() => {
        if (props.room.users) {
            setRoomUsers(props.users);
        }
    }, [props.users]);

    useEffect(() => {
        const param = new URLSearchParams(window.location.search);
        props.viewRoom(props.authToken, param.get('roomId'));
        props.listRoomAdmins(props.authToken, param.get('roomId'));
        props.listRoomUsers(props.authToken, param.get('roomId'));
        props.listRoomBookings(props.authToken, param.get('roomId'));
    }, []);

    useEffect(() => {
        if (props.room.organization) {
            props.viewOrg(props.authToken, props.room.organization);
        }
    }, [props.room.organization]);


    useEffect(() => {
        setRoomImagesNameList(roomImages.map((e, i) =>
            <Grid item key={i} sm={12} style={{marginTop: "10px"}}>
                <span onClick={() => removeRoomImage(e)}> x </span>{e.name}
            </Grid>))
    }, [roomImages]);

    return (
        <>
            <Grid item xs={12} sm={11} md={10} lg={9} xl={8}>
                <Grid container style={{marginBottom: '3em'}} justify='center'>
                    <Typography align='center' variant='h5'>
                        Edit Room Details
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
                        <TableContainer component={Paper}>
                            <Table>
                                <TableBody>
                                    {/* <TableRow>
										<TableCell>Public ID</TableCell>
										<TableCell>{props.room.publicId}</TableCell>
									</TableRow> */}

                                    <TableRow>
                                        <TableCell>Organization</TableCell>
                                        <TableCell>{props.organization}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>Room Name</TableCell>
                                        <TableCell>
                                            <TextField
                                                variant='outlined'
                                                fullWidth
                                                helperText={roomNameHelper}
                                                error={roomNameHelper !== ''}
                                                value={roomName}
                                                onChange={(event) => setRoomName(event.target.value)}
                                            />
                                        </TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>Capacity</TableCell>
                                        <TableCell>
                                            <TextField
                                                variant='outlined'
                                                fullWidth
                                                helperText={capacityHelper}
                                                error={capacityHelper !== ''}
                                                value={capacity}
                                                onChange={(event) => setCapacity(event.target.value)}
                                            />
                                        </TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>Facilities</TableCell>
                                        <TableCell>
                                            <TextField
                                                variant='outlined'
                                                fullWidth
                                                helperText={facilitiesHelper}
                                                error={facilitiesHelper !== ''}
                                                value={facilities}
                                                onChange={(event) => setFacilities(event.target.value)}
                                            />
                                        </TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>Description</TableCell>
                                        <TableCell>
                                            <TextField
                                                variant='outlined'
                                                fullWidth
                                                helperText={descriptionHelper}
                                                error={descriptionHelper !== ''}
                                                value={description}
                                                onChange={(event) => setDescription(event.target.value)}
                                            />
                                        </TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>Active</TableCell>
                                        <TableCell>
                                            <InputRadioButton
                                                justify='flex-start'
                                                label1='Yes'
                                                value1={true}
                                                label2='No'
                                                value2={false}
                                                value={active}
                                                setValue={setActive}
                                            />
                                        </TableCell>
                                    </TableRow>
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </Grid>
                </Grid>

                <Grid
                    container
                    alignItems='center'
                    justify='space-around'
                    style={{marginBottom: '1em', marginTop: '2em'}}>
                    <Grid item xs={8}>
                        <Typography variant='h6'>Room Administrator</Typography>
                    </Grid>
                </Grid>

                <Grid container alignItems='center' justify='space-around' style={{marginBottom: '1em'}}>
                    <Grid item xs={8}>
                        <Grid container direction='row' alignItems='center'>
                            <Grid item xs={7}>
                                <TextField
                                    fullWidth
                                    width='90%'
                                    value={newAdminEmail}
                                    helperText={newAdminEmailHelper}
                                    error={newAdminEmailHelper !== ''}
                                    onChange={(event) => setNewAdminEmail(event.target.value)}
                                    label='New Admin Email'
                                    variant='outlined'
                                />
                            </Grid>
                            <Grid item xs={1}>
                                <AddCircleOutlineOutlined fontSize='large' onClick={addNewAdmin}/>
                            </Grid>
                        </Grid>
                    </Grid>
                </Grid>
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
                                        <TableCell></TableCell>
                                    </TableRow>
                                    {roomAdmins &&
                                    roomAdmins.length > 0 &&
                                    roomAdmins.map((e, i) => (
                                        <TableRow
                                            key={i}>
                                            <TableCell>{e.name}</TableCell>
                                            <TableCell>{e.email}</TableCell>
                                            <TableCell>{e.active ? 'Yes' : 'No'}</TableCell>
                                            <TableCell>{e.emailVerified ? 'Yes' : 'No'}</TableCell>
                                            <TableCell classes={{root: classes.tableCellNoBorder}}>
                                                <DeleteOutline
                                                    fontSize='large'
                                                    className={classes.hover}
                                                    onClick={() => deleteAdminHandler(e)}
                                                />
                                            </TableCell>
                                        </TableRow>
                                    ))}

                                </TableBody>
                            </Table>
                        </TableContainer>
                    </Grid>
                </Grid>
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
                        <Grid container direction='row' alignItems='center'>
                            <Grid item xs={7}>
                                <TextField
                                    fullWidth
                                    width='90%'
                                    value={newUserEmail}
                                    helperText={newUserEmailHelper}
                                    error={newUserEmailHelper !== ''}
                                    onChange={(event) => setNewUserEmail(event.target.value)}
                                    label='New User Email'
                                    variant='outlined'
                                />
                            </Grid>
                            <Grid item xs={1}>
                                <AddCircleOutlineOutlined fontSize='large' onClick={addNewUser}/>
                            </Grid>
                        </Grid>
                    </Grid>
                </Grid>
                {roomUsers && roomUsers.length === 0 && (
                    <Grid container alignItems='center' justify='space-around' style={{marginBottom: '1em'}}>
                        <Grid item xs={8}>
                            There is no users in this room
                        </Grid>
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
                                    {roomUsers &&
                                    roomUsers.length > 0 &&
                                    <TableRow>
                                        <TableCell>Name</TableCell>
                                        <TableCell>Email</TableCell>
                                        <TableCell>Active</TableCell>
                                        <TableCell>Email Verified</TableCell>
                                        <TableCell></TableCell>
                                    </TableRow>
                                    }
                                    {roomUsers &&
                                    roomUsers.length > 0 &&
                                    roomUsers.map((e, i) => (
                                        <TableRow
                                            key={i}>
                                            <TableCell>{e.name}</TableCell>
                                            <TableCell>{e.email}</TableCell>
                                            <TableCell>{e.active ? 'Yes' : 'No'}</TableCell>
                                            <TableCell>{e.emailVerified ? 'Yes' : 'No'}</TableCell>
                                            <TableCell>
                                                <DeleteOutline
                                                    fontSize='large'
                                                    className={classes.hover}
                                                    onClick={() => deleteUserHandler(e)}
                                                />
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </Grid>
                </Grid>

                {props.room.bookings && props.room.bookings.length > 0 && (
                    <Grid
                        container
                        alignItems='center'
                        justify='space-around'
                        style={{marginBottom: '1em', marginTop: '2em'}}>
                        <Grid item xs={8}>
                            <Typography variant='h6'>Room Bookings</Typography>
                        </Grid>
                    </Grid>
                )}
                {props.rooms &&
                props.room.bookings.length > 0 &&
                props.room.bookings.map((e, i) => (
                    <Grid container alignItems='center' justify='space-around' style={{marginBottom: '1em'}} key={i}>
                        <Grid item xs={8}>
                            <TableContainer component={Paper}>
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
                            <Grid item>
                                <MyButton
                                    label='Submit'
                                    width='10em'
                                    onClick={submitHandler}
                                    disabled={!roomName && !capacity && !roomAdmins}
                                />
                            </Grid>
                        </Grid>
                    </Grid>
                </Grid>
            </Grid>
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
    listBookingsErrorMessage: state.roomListBookingsReducer.errorMessage,

    searchedUser:
        state.searchUserReducer.users && state.searchUserReducer.users.length > 0
            ? state.searchUserReducer.users[0]
            : undefined,
    searchUserErrorMessage: state.searchUserReducer.errorMessage,
    searchUserStatus: state.searchUserReducer.status
});

export default connect(mapStateToProps, {
    viewRoom,
    listRoomAdmins,
    listRoomUsers,
    listRoomBookings,
    viewOrg,
    deleteRoom,
    searchUser,
    editRoom
})(requireAuth(layout2(EditRoom)));
