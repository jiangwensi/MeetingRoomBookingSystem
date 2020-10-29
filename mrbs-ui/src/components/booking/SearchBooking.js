import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Grid, Typography, Paper, Box, Link} from '@material-ui/core';
import {searchBooking, deleteBooking, getAvailableTimeslotByRoom} from 'myRedux/actions/booking';
import {searchOrg} from "../../myRedux/actions/org";
import requireAuth from 'hoc/requireAuth';
import layout2 from 'components/layout/layout2';
import SearchBar from 'components/booking/SearchBar';
import {MyModal, MyButton, StatusMessage, MyDataList} from 'components/element';
import {searchRoom, viewRoom} from 'myRedux/actions/room';
import {
    ROUTE_BOOKING_CREATE,
    ROUTE_BOOKING_SEARCH
} from 'utils/const';
import * as ROUTE_CONST from "../../utils/const";

const today = () => {
    let date = new Date();
    let month = date.getMonth() + 1;
    if (month < 10) {
        month = "0" + month;
    }
    let d = date.getDate();
    if (d < 10) {
        d = "0" + month;
    }
    let dateStr = date.getFullYear() + "-" + month + "-" + d;
    return dateStr;
}

class SearchBooking extends Component {
    state = {
        deletePublicId: '',
        // rooms: [],
        openDeleteModal: false,
        bookingData: [],
        ignoreData: [],
        roomName: '',
        date: '',
        openMenu: false,
        anchorEL: null,
        selectedRoomIndex: -1,
        selectedDate: ''
    };
    // searchBarStates = createRef();

    isSysadmin = () => {
        return sessionStorage.getItem("roles") === 'SYSADM';
    }

    createHandler = () => {
        this.props.history.push(ROUTE_BOOKING_CREATE);
    };

    toggleMenu = (event) => {
        if (this.state.openMenu) {
            this.setState({anchorEL: null});
        } else {
            this.setState({anchorEL: event.target});
        }
        this.setState({openMenu: !this.state.openMenu});
    }

    searchHandler = () => {
        this.props.searchBooking(this.props.authToken);
    };

    clickDeleteIconHandler(deletePublicId) {
        this.setState({deletePublicId});
        this.setState({openDeleteModal: true});
    }

    submitDeleteHandler = () => {
        this.props.deleteBooking(this.props.authToken, this.state.deletePublicId, this.state.deleteRoomName, () =>
            this.props.history.push(ROUTE_BOOKING_SEARCH)
        );
        this.setState({openDeleteModal: false});
    };

    componentDidMount = () => {
        this.props.searchRoom(this.props.authToken, null, null, true, this.isSysadmin() ? false : true);
        this.props.searchOrg(this.props.authToken, null, true);
        this.props.searchBooking(this.props.authToken);
    };

    componentDidUpdate = (prevProps, prevState) => {
        if (
            prevProps.customCreateMessage !== this.props.customCreateMessage ||
            prevProps.customDeleteMessage !== this.props.customDeleteMessage
        ) {
            this.props.searchBooking(this.props.authToken, this.state.roomName, this.state.fromTime, this.state.toTime);
        }
        // if (this.props.bookings&& this.props.bookings.length>0 && this.props.rooms && this.props.rooms.length>0) {
        if (prevProps.status !== this.props.status || prevProps.bookings !== this.props.bookings || prevProps.rooms != this.props.rooms || prevProps.orgs != this.props.orgs) {
            if (this.props.rooms && this.props.rooms.length > 0 && this.props.bookings) {
                const bookings = [];
                const roomIds = new Set();

                this.props.bookings.forEach((e) => {
                    roomIds.add(e.roomId);
                    const booking = {};
                    booking['Public Id'] = e['publicId'];
                    let rm = this.props.rooms.filter(r => r.publicId === e['roomId'])[0];
                    let og;
                    if (rm) {
                        og = this.props.orgs.filter(o => o.publicId === rm.organization)[0];
                        if (og) {
                            booking['Room'] = og.name + " - " + rm.name;
                        }
                    }
                    booking['Date'] = e['date'];
                    booking['From'] = e['fromTime'];
                    booking['To'] = e['toTime'];
                    booking['Booked By'] = e['bookedByName'];
                    booking['_roomId'] = e['roomId'];
                    booking['_bookedBy'] = e['bookedBy'];
                    this.setState({ignoreData: [...this.state.ignoreData, '_roomId', '_bookedBy', 'Public Id']});
                    booking['Actions'] = [
                        {icon: 'delete', clickHandler: () => this.clickDeleteIconHandler(e['publicId'])}
                    ];
                    // room['_organization'] = e['organization'];
                    bookings.push(booking);
                });
                this.setState({bookingData: bookings});
                roomIds.forEach((e) => {
                    this.props.viewRoom(this.props.authToken, e);
                });
            }

        }

        if (prevState.selectedRoomIndex != this.state.selectedRoomIndex || prevState.selectedDate != this.state.selectedDate) {
            this.props.searchBooking(this.props.authToken, this.state.selectedRoomIndex === -1 ? null : this.props.rooms[this.state.selectedRoomIndex].publicId, this.state.selectedDate ? this.state.selectedDate : null);
        }
    };

    render = () => {
        return (
            <>
                <Grid container justify='center' alignItems='center' style={{width: '100%'}}>
                    <Grid item xs={12} sm={11} md={10} lg={9} xl={8}>
                        <SearchBar
                            searchHandler={this.searchHandler}
                            ref={this.searchBarStates}
                            {...this.state}
                            parent={this}
                            searchRoom={this.props.searchRoom}
                            searchOrg={this.props.searchOrg}
                            getAvailableTimeslotByRoom={this.props.getAvailableTimeslotByRoom}/>
                    </Grid>
                    <Grid item xs={12} sm={11} md={10} lg={9} xl={8} style={{marginTop: '3em'}}>
                        {this.props.rooms && this.props.rooms.length > 0 &&
                        <MyButton label='Create Booking' width='15em' onClick={this.createHandler}></MyButton>}
                        {(!this.props.rooms || this.props.rooms.length === 0)
                        &&
                        <>
                            <StatusMessage type="failed"
                                           errorMessage="You are not enrolled in any room yet. Please contact room admin for enrollment. "/>


                            <Typography variant='h6'>
                                Click <Link
                                onClick={() => this.props.history.push(ROUTE_CONST.ROUTE_ROOM_SEARCH)}>here</Link> to
                                list all rooms.
                            </Typography>
                        </>
                        }
                    </Grid>

                    {this.props.customCreateMessage && (
                        <Grid item xs={12} sm={11} md={10} lg={9} xl={8} style={{marginTop: '3em'}}>
                            <StatusMessage type='success' message={this.props.customCreateMessage}/>
                        </Grid>
                    )}
                    {this.props.createErrorMessage && (
                        <Grid item xs={12} sm={11} md={10} lg={9} xl={8} style={{marginTop: '3em'}}>
                            <StatusMessage type='failed' errorMessage={this.props.createErrorMessage}/>
                        </Grid>
                    )}
                    {this.props.customDeleteMessage && (
                        <Grid item xs={12} sm={11} md={10} lg={9} xl={8} style={{marginTop: '3em'}}>
                            <StatusMessage type='success' message={this.props.customDeleteMessage}/>
                        </Grid>
                    )}
                    {this.props.deleteErrorMessage && (
                        <Grid item xs={12} sm={11} md={10} lg={9} xl={8} style={{marginTop: '3em'}}>
                            <StatusMessage type='failed' errorMessage={this.props.deleteErrorMessage}/>
                        </Grid>
                    )}
                    {this.props.rooms &&
                    this.props.rooms.map((r, i) => (
                        <React.Fragment key={i}>
                            {this.state.bookingData.filter((b) => r.publicId === b._roomId).length > 0 && (
                                <Grid
                                    item
                                    xs={12}
                                    sm={11}
                                    md={10}
                                    lg={9}
                                    xl={8}
                                    style={{marginTop: '3em'}}
                                    component={Paper}
                                    key={r.publicId}>
                                    <Grid
                                        container
                                        direction='column'
                                        alignItems='center'
                                        justify='center'
                                        width='100%'>
                                        {/*<Grid item>*/}
                                        {/*    <Typography align='center' variant='h5'>*/}
                                        {/*        {r.name}*/}
                                        {/*    </Typography>*/}
                                        {/*</Grid>*/}
                                        <Grid item style={{marginTop: '1em'}}>
                                            <MyDataList
                                                data={this.state.bookingData.filter(
                                                    (bd) => r.publicId === bd._roomId
                                                )}
                                                ignoreData={this.state.ignoreData}></MyDataList>
                                        </Grid>
                                    </Grid>
                                </Grid>
                            )}
                        </React.Fragment>
                    ))}
                </Grid>

                <MyModal
                    open={this.state.openDeleteModal}
                    setOpen={() => this.setState({openDeleteModal: false})}
                    okLabel='Confirm Delete'
                    noLabel='Cancel'
                    title='Confirm Deletion?'
                    okHandler={this.submitDeleteHandler}
                    message={`You are going to delete this booking. Do you confirm to continue? `}
                />
            </>
        );
    };
}

const mapStateToProps = (state) => {
    return {
        authToken: state.authReducer.authToken,
        status: state.bookingSearchReducer.status,
        bookings: state.bookingSearchReducer.bookings,
        customCreateMessage: state.bookingCreateReducer.customMessage,
        createErrorMessage: state.bookingCreateReducer.errorMessage,
        customDeleteMessage: state.bookingDeleteReducer.customMessage,
        deleteErrorMessage: state.bookingDeleteReducer.errorMessage,
        roomId: state.roomViewReducer.room.publicId,
        roomName: state.roomViewReducer.room.name,
        rooms: state.roomSearchReducer.rooms,
        orgs: state.searchOrgReducer.organizations
    };
};

export default connect(mapStateToProps, {
    searchBooking,
    deleteBooking,
    viewRoom,
    searchRoom,
    searchOrg,
    getAvailableTimeslotByRoom
})(
    requireAuth(layout2(SearchBooking))
);
