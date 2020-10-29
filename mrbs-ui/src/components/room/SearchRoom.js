import React, {useState, useEffect} from 'react';
import {connect} from 'react-redux';
import {searchRoom, deleteRoom} from 'myRedux/actions/room';
import {viewOrg} from 'myRedux/actions/org';
import SearchBar from 'components/room/SearchBar';
import requireAuth from 'hoc/requireAuth';
import layout2 from 'components/layout/layout2';
import {Grid, Typography, Paper, Box} from '@material-ui/core';
import {MyModal, MyButton, StatusMessage, MyDataList} from 'components/element';
import {ROUTE_ROOM_CREATE, ROUTE_ROOM_VIEW, ROUTE_ROOM_EDIT, ROUTE_ROOM_SEARCH} from 'utils/const';

const SearchRoom = (props) => {
    const [deletePublicId, setDeletePublicId] = useState('');
    const [deleteRoomName, setDeleteRoomName] = useState('');
    const [organizations, setOrganizations] = useState([]);
    const [openDeleteModal, setOpenDeleteModal] = useState(false);
    const [roomData, setRoomData] = useState([]);
    const [ignoreData, setIgnoreData] = useState([]);

    const createHandler = () => {
        props.history.push(ROUTE_ROOM_CREATE);
    };

    const searchHandler = (roomName, orgName, active) => {
        props.searchRoom(props.authToken, roomName, orgName, active);
    };

    const clickDeleteIconHandler = (deletePublicId, deleteRoomName) => {
        setDeletePublicId(deletePublicId);
        setDeleteRoomName(deleteRoomName);
        setOpenDeleteModal(true);
    };

    const submitDeleteHandler = () => {
        props.deleteRoom(props.authToken, deletePublicId, deleteRoomName, () => props.history.push(ROUTE_ROOM_SEARCH));
        // window.location.reload();
        setOpenDeleteModal(false);
    };

    const allowCreateEditDeleteRoom = () => {
        if (sessionStorage.getItem("roles") === 'SYSADM') {
            return true;
        }
        if (props.admins && props.admins.length > 0 && props.admins.filter(e => e === sessionStorage.getItem("userPublicId")).length > 0) {
            return true;
        }
        return false;
    }

    useEffect(() => {
        props.searchRoom(props.authToken, '', '');
    }, [props.customCreateMessage, props.customDeleteMessage]);

    useEffect(() => {
        if (props.rooms) {
            const rooms = [];
            const organizationIds = new Set();

            props.rooms.forEach((e) => {
                organizationIds.add(e.organization);
                const room = {};
                room['Public Id'] = e['publicId'];
                room['Name'] = e['name'];
                room['Capacity'] = e['capacity'];
                room['Facilities'] = e['facilities'];
                room['Description'] = e['description'];
                room['Active'] = e['active'] === true ? 'Yes' : 'No';
                room['Organization'] = e['organization'];
                setIgnoreData([...ignoreData, 'Organization', 'Public Id']);
                room['Actions'] = [
                    {
                        icon: 'view',
                        clickHandler: () => props.history.push(`${ROUTE_ROOM_VIEW}?roomId=${e['publicId']}`)
                    },

                ];
                if (allowCreateEditDeleteRoom()) {
                    room['Actions'].push({
                            icon: 'edit',
                            clickHandler: () => props.history.push(`${ROUTE_ROOM_EDIT}?roomId=${e['publicId']}`)
                        },
                        {icon: 'delete', clickHandler: () => clickDeleteIconHandler(e['publicId'], e['name'])})
                }


                // room['_organization'] = e['organization'];
                rooms.push(room);
            });
            setRoomData(rooms);
            organizationIds.forEach((e) => {
                props.viewOrg(props.authToken, e);
            });
        }
    }, [props.status, props.rooms]);

    useEffect(() => {
        if (props.orgId && props.orgId !== '') {
            if (!organizations.map((e) => e.publicId).includes(props.orgId)) {
                setOrganizations([
                    ...organizations,
                    {
                        publicId: props.orgId,
                        name: props.orgName,
                        desc: props.orgDesc
                    }
                ]);
            }
        } else {
            setOrganizations([]);
        }
    }, [props.orgId]);

    return (
        <>
            <Grid container justify='center' alignItems='center' style={{width: '100%'}}>
                <Grid item xs={12} sm={11} md={10} lg={9} xl={8}>
                    <SearchBar searchHandler={searchHandler}/>
                </Grid>
                {/*{allowCreateEditDeleteRoom() &&*/}
                <Grid item xs={12} sm={11} md={10} lg={9} xl={8} style={{marginTop: '3em'}}>
                    <MyButton label='Create Room' width='15em' onClick={createHandler}></MyButton>
                </Grid>
                {/*}*/}

                {props.customCreateMessage && (
                    <Grid item xs={12} sm={11} md={10} lg={9} xl={8} style={{marginTop: '3em'}}>
                        <StatusMessage type='success' message={props.customCreateMessage}/>
                    </Grid>
                )}
                {props.customDeleteMessage && (
                    <Grid item xs={12} sm={11} md={10} lg={9} xl={8} style={{marginTop: '3em'}}>
                        <StatusMessage type='success' message={props.customDeleteMessage}/>
                    </Grid>
                )}
                {props.deleteErrorMessage && (
                    <Grid item xs={12} sm={11} md={10} lg={9} xl={8} style={{marginTop: '3em'}}>
                        <StatusMessage type='success' message={props.deleteErrorMessage}/>
                    </Grid>
                )}
                {props.customEditMessage && (
                    <Grid item xs={12} sm={11} md={10} lg={9} xl={8} style={{marginTop: '3em'}}>
                        <StatusMessage type='success' message={props.customEditMessage}/>
                    </Grid>
                )}
                {organizations &&
                organizations.map((e,i) => (
                    <React.Fragment key={i}>
                        {roomData.filter((r) => e.publicId === r.Organization).length > 0 && (
                            <Grid
                                item
                                xs={12}
                                sm={11}
                                md={10}
                                lg={9}
                                xl={8}
                                style={{marginTop: '3em'}}
                                component={Paper}
                                key={e.publicId}>
                                <Grid
                                    container
                                    direction='column'
                                    alignItems='center'
                                    justify='center'
                                    width='100%'>
                                    <Grid item>
                                        <Typography align='center' variant='h5'>
                                            Organization: {e.name}
                                        </Typography>
                                    </Grid>
                                    <Grid item style={{marginTop: '1em'}}>
                                        <MyDataList
                                            data={roomData.filter((r) => e.publicId === r.Organization)}
                                            ignoreData={ignoreData}></MyDataList>
                                    </Grid>
                                </Grid>
                            </Grid>
                        )}
                    </React.Fragment>
                ))}
            </Grid>

            <MyModal
                open={openDeleteModal}
                setOpen={setOpenDeleteModal}
                okLabel='Confirm Delete'
                noLabel='Cancel'
                title='Confirm Deletion?'
                okHandler={submitDeleteHandler}
                message={`You are going to delete Room ${deleteRoomName}. Do you confirm to continue? `}
            />
        </>
    );
};

const mapStateToProps = (state) => ({
    authToken: state.authReducer.authToken,
    status: state.roomSearchReducer.status,
    rooms: state.roomSearchReducer.rooms,
    customCreateMessage: state.roomCreateReducer.customMessage,
    customDeleteMessage: state.roomDeleteReducer.customMessage,
    deleteErrorMessage: state.roomDeleteReducer.errorMessage,
    customEditMessage: state.roomEditReducer.customMessage,
    orgId: state.viewOrgReducer.publicId,
    orgName: state.viewOrgReducer.name,
    orgDesc: state.viewOrgReducer.description
});

export default connect(mapStateToProps, {searchRoom, deleteRoom, viewOrg})(requireAuth(layout2(SearchRoom)));
