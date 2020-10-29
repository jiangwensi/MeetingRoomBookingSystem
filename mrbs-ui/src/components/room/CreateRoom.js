import React, {useState, useEffect} from 'react';
import {connect} from 'react-redux';
import layout2 from 'components/layout/layout2';
import requireAuth from 'hoc/requireAuth';
import {
    Grid,
    Typography,
    TextField,
    List,
    ListItem,
    ListItemText,
    Button
} from '@material-ui/core';
import {MyButton, InputRadioButton, StatusMessage} from 'components/element';
import { validateCommaSeparatedEmails, validateNotEmpty, validateNumber} from 'utils/validation';
import {createRoom} from 'myRedux/actions/room';
import {searchOrg} from 'myRedux/actions/org';
import {ROUTE_ROOM_SEARCH} from 'utils/const';

const CreateRoom = (props) => {
    const [roomName, setRoomName] = useState('');
    const [capacity, setCapacity] = useState('');
    const [description, setDescription] = useState('');
    const [facilities, setFacilities] = useState('');
    const [orgNameList, setOrgNameList] = useState([]);
    const [orgName, setOrgName] = useState('');
    const [orgNameError, setOrgNameError] = useState(false);
    const [orgPublicId, setOrgPublicId] = useState('');
    const [adminEmails, setAdminEmails] = useState('');
    const [active, setActive] = useState(true);
    const [blockedTimeslots, setBlockedTimeslots] = useState([]);
    const [roomImages, setRoomImages] = useState([]);
    const [roomImagesNameList, setRoomImagesNameList] = useState('');

    const [roomNameHelper, setRoomNameHelper] = useState('');
    const [capacityHelper, setCapacityHelper] = useState('');
    const [facilitiesHelper, setFacilitiesHelper] = useState('');
    const [orgNameHelper, setOrgNameHelper] = useState('');
    const [adminEmailsHelper, setAdminEmailsHelper] = useState('');

    const submitHandler = () => {
        console.log('submitHandler', blockedTimeslots);
        if (
            validateNotEmpty(orgName) &&
            validateNotEmpty(roomName) &&
            validateNotEmpty(facilities) &&
            validateNumber(capacity) &&
            validateCommaSeparatedEmails(adminEmails)
        ) {
            console.log("roomImages:",roomImages)
            props.createRoom(
                props.authToken,
                roomName,
                capacity,
                description,
                facilities,
                orgPublicId,
                adminEmails,
                active,
                blockedTimeslots,
                roomImages,
                successHandler
            );
        }
    };

    const successHandler = () => {
        props.history.push(ROUTE_ROOM_SEARCH);
    };

    const clickOrgItemHandler = (orgItem) => {
        setOrgPublicId(orgItem.publicId);
        setOrgName(orgItem.name);
        setOrgNameList([]);
        setOrgNameHelper('');
        setOrgNameError(false);
    };

    useEffect(() => {
        if (!skipValidation() && !validateNotEmpty(roomName)) {
            setRoomNameHelper('Room name must not be empty');
        } else {
            setRoomNameHelper('');
        }
    }, [roomName]);

    useEffect(() => {
        if (!skipValidation() && !validateNotEmpty(orgPublicId)) {
            setOrgNameHelper('Organization name must be valid');
        } else {
            setOrgNameHelper('');
        }
    }, [orgName]);

    useEffect(() => {
        if (!skipValidation() && !validateNotEmpty(facilities)) {
            setFacilitiesHelper('Facilities must not be empty');
        } else {
            setFacilitiesHelper('');
        }
    }, [facilities]);

    useEffect(() => {
        if (!skipValidation() && !validateNumber(capacity)) {
            setCapacityHelper('Capacity must be number');
        } else {
            setCapacityHelper('');
        }
    }, [capacity]);

    useEffect(() => {
        if (!skipValidation() && !validateCommaSeparatedEmails(adminEmails)) {
            setAdminEmailsHelper('Invalid Admin Emails');
        } else {
            setAdminEmailsHelper('');
        }
    }, [adminEmails]);

    const validateOrgName = () => {
        props.searchOrg(props.authToken, orgName);
    };

    useEffect(() => {
        // setOrgNameHelper('props.checkOrgStatus', props.checkOrgStatus);
        if (props.checkOrgStatus === '') {
            setOrgNameHelper('Please validate organization name');
            setOrgPublicId('');
            setOrgNameError(false);
        } else {
            if (props.checkOrgStatus === 'success') {
                // setOrgNameHelper('Invalid organization name');
                if (props.organizations) {
                    if (props.organizations.length === 0) {
                        setOrgNameHelper('Invalid organization name');
                        setOrgPublicId('');
                        setOrgNameList('');
                        setOrgNameError(true);
                        // } else if (props.organizations.length === 1) {
                        // 	setOrgNameHelper('Organization name is valid');
                        // 	setOrgPublicId(props.organizations[0].publicId);
                        // 	setOrgNameList('');
                        // 	setOrgNameError(false);
                    } else if (props.organizations.length >= 1) {
                        setOrgNameHelper('Please select an organization name');
                        setOrgPublicId('');
                        setOrgNameList(props.organizations);
                        setOrgNameError(true);
                    }
                }
            } else {
                setOrgNameHelper('Unable to validate organization name. Please try again later');
                setOrgPublicId('');
                setOrgNameList('');
                setOrgNameError(true);
            }
        }
    }, [props.checkOrgStatus, props.organizations]);

    const returnHander = () => {
        props.history.goBack();
    };

    const skipValidation = () => {
        return !orgName && !roomName && !facilities && !description && !adminEmails && !capacity;
    };

    const onRoomImageChange = (event) =>{
        console.log("onRoomImageChange")
        let image = event.target.files[0]
        console.log(roomImages);
        setRoomImages([...roomImages,image])
    }

    const removeRoomImage = (image)=> {;
        let index = roomImages.indexOf(image);
        roomImages.splice(index,1);
        setRoomImages([...roomImages]);
    }

    useEffect(()=> {
        setRoomImagesNameList(roomImages.map((e,i)=><Grid item key={i} sm={12} style={{marginTop:"10px"}} ><span onClick={()=>removeRoomImage(e)}> x </span>{e.name}</Grid>))
    },[roomImages]);

    return (
        <Grid item xs={12} sm={11} md={10} lg={9} xl={8}>
            <Grid container style={{marginBottom: '3em'}} justify='center'>
                <Typography align='center' variant='h5'>
                    Create Room
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
                    <Grid container justify='space-between'>
                        <Grid item xs={6}>
                            <TextField
                                helperText={roomNameHelper}
                                error={roomNameHelper !== ''}
                                fullWidth
                                variant='outlined'
                                label='Room Name'
                                value={roomName}
                                onChange={(event) => setRoomName(event.target.value)}></TextField>
                        </Grid>
                        <Grid item xs={2}>
                            <TextField
                                fullWidth
                                helperText={capacityHelper}
                                error={capacityHelper !== ''}
                                variant='outlined'
                                label='Capacity'
                                value={capacity}
                                onChange={(event) => setCapacity(event.target.value)}></TextField>
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
            <Grid container alignItems='center' justify='space-around' style={{marginBottom: '1em'}}>
                <Grid item xs={8}>
                    <Grid container justify='space-between' alignItems='flex-start'>
                        <Grid item xs={9}>
                            <Grid
                                container
                                direction='column'
                                style={{width: '100%'}}
                                alignItems='flex-start'
                                justify='center'>
                                <Grid item xs={12} style={{width: '100%'}}>
                                    <TextField
                                        fullWidth
                                        variant='outlined'
                                        label='Organization Name'
                                        value={orgName}
                                        error={orgNameError}
                                        helperText={orgNameHelper}
                                        onChange={(event) => setOrgName(event.target.value)}></TextField>
                                </Grid>
                                {orgNameList && orgNameList.length > 0 && (
                                    <Grid item xs={12} style={{width: '100%'}}>
                                        <List>
                                            {orgNameList.map((e) => (
                                                <ListItem key={e.name} onClick={() => clickOrgItemHandler(e)}>
                                                    <ListItemText>{e.name}</ListItemText>
                                                </ListItem>
                                            ))}
                                        </List>
                                    </Grid>
                                )}
                            </Grid>
                        </Grid>
                        <Grid item xs={2}>
                            <MyButton label='Validate' onClick={validateOrgName}></MyButton>
                        </Grid>
                    </Grid>
                </Grid>
            </Grid>
            <Grid container alignItems='center' justify='space-around' style={{marginBottom: '1em'}}>
                <Grid item xs={8}>
                    <TextField
                        fullWidth
                        variant='outlined'
                        label='Facilities'
                        value={facilities}
                        onChange={(event) => setFacilities(event.target.value)}></TextField>
                </Grid>
            </Grid>
            <Grid container alignItems='center' justify='space-around' style={{marginBottom: '1em'}}>
                <Grid item xs={8}>
                    <TextField
                        fullWidth
                        variant='outlined'
                        label='Description'
                        value={description}
                        onChange={(event) => setDescription(event.target.value)}></TextField>
                </Grid>
            </Grid>
            <Grid container alignItems='center' justify='space-around' style={{marginBottom: '1em'}}>
                <Grid item xs={8}>
                    <TextField
                        helperText={adminEmailsHelper}
                        error={adminEmailsHelper !== ''}
                        fullWidth
                        variant='outlined'
                        label='Room Admin Email (separate multiple emails by comma)'
                        value={adminEmails}
                        onChange={(event) => setAdminEmails(event.target.value)}></TextField>
                </Grid>
            </Grid>

            <Grid
                container
                alignItems='center'
                justify='space-around'
                style={{marginBottom: '1em', marginTop: '2em'}}>
                <Grid item xs={8}>
                    <Button
                        variant="contained"
                        component="label"
                    >
                        Upload Room Image
                        <input
                            type="file"
                            style={{display: "none"}}
                            onChange={(event) => onRoomImageChange(event)}
                        />
                    </Button>
                    <Grid container>
                        {roomImagesNameList}
                    </Grid>
                </Grid>
            </Grid>


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
                                disabled={
                                    !orgName && !roomName && !facilities && !description && !adminEmails && !capacity
                                }
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
        status: state.roomCreateReducer.status,
        errorMessage: state.roomCreateReducer.errorMessage,
        organizations: state.searchOrgReducer.organizations,
        checkOrgStatus: state.searchOrgReducer.status
    };
};

export default connect(mapStateToProps, {createRoom, searchOrg})(requireAuth(layout2(CreateRoom)));
