import React, {useState, useEffect} from 'react';
import {connect} from 'react-redux';
import layout2 from 'components/layout/layout2';
import requireAuth from 'hoc/requireAuth';
import {
    Grid,
    Typography,
    TextField,
    Table,
    TableBody,
    TableCell,
    TableRow,
    Menu,
    MenuItem
} from '@material-ui/core';
import {MyButton} from 'components/element';
import {createBooking, getAvailableTimeslotByRoom,searchBooking} from 'myRedux/actions/booking';
import {searchRoom} from 'myRedux/actions/room';
import {searchOrg} from 'myRedux/actions/org';
import { ROUTE_BOOKING_SEARCH} from 'utils/const';
import makeStyles from "@material-ui/core/styles/makeStyles";

const CreateBooking = (props) => {

    const isSysadmin = () => {
        return sessionStorage.getItem("roles")==='SYSADM';
    }

    useEffect(() => {
        props.searchRoom(props.authToken, null, null, true,isSysadmin()?false:true);
        props.searchOrg(props.authToken, null, true);
    }, [])

    return (<Grid item xs={12}>{console.log(props.slots)}
        <MyRooms {...props}
                 rooms={props.rooms}
                 getAvailableTimeslotByRoom={props.getAvailableTimeslotByRoom}
                 authToken={props.authToken}
                 slots={props.slots}
                 orgs={props.orgs}
                 createBooking={props.createBooking}/>
    </Grid>)
}

const useStyles = makeStyles({
    MuiTableCell: {
        borderBottom: "none"
    }
});
const MyRooms = (props) => {

    const classes = useStyles();
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
    const [selectedRoomIndex, setSelectedRoomIndex] = useState(0);
    const [selectedDate, setSelectedDate] = useState(today());
    const [openMenu, setOpenMenu] = useState(false);
    const [anchorEl, setAnchorEl] = useState(null);

    const returnHander = () => {
        props.history.goBack();
    };

    useEffect(() => {
        if (props.rooms && props.rooms.length > 0) {
            props.getAvailableTimeslotByRoom(props.authToken, props.rooms[selectedRoomIndex].publicId, selectedDate);
        }
    }, [selectedDate, selectedRoomIndex, props.rooms]);

    const toggleMenu = (event) => {
        if (openMenu) {
            setAnchorEl(null);
        } else {
            setAnchorEl(event.target);
        }
        setOpenMenu(!openMenu);
    }

    const submitBooking = (e)=> {
        props.createBooking(props.authToken,props.rooms[selectedRoomIndex].publicId,e.from,e.to,props.history.push(ROUTE_BOOKING_SEARCH));
    }

    return (
        <Grid container justify="center">
            <Grid item xs={12} md={6}>
                <Grid container justify="center">
                    <Table>
                        <TableBody>
                            <TableRow>
                                <TableCell classes={{root: classes.MuiTableCell}}><Typography
                                    variant={"h6"}>Room:</Typography></TableCell>
                                <TableCell classes={{root: classes.MuiTableCell}} onClick={event => {
                                    toggleMenu(event);
                                }}>
                                    <Typography
                                        variant={"h6"}>{props.rooms && props.rooms.length > 0 && props.orgs && (props.orgs.filter(e => e.publicId === props.rooms[selectedRoomIndex].organization
                                    )[0].name)}
                                    </Typography>
                                    <Typography
                                        variant={"h6"}>{props.rooms && props.rooms.length > 0 && props.orgs && props.rooms[selectedRoomIndex].name}
                                    </Typography>

                                </TableCell>
                                <TableCell classes={{root: classes.MuiTableCell}}><Typography
                                    variant={"h6"}>Date:</Typography></TableCell>
                                <TableCell classes={{root: classes.MuiTableCell}}>
                                    <Typography variant={"h6"}>
                                        <TextField type="date" value={selectedDate}
                                                                          onChange={(event => setSelectedDate(event.target.value))}/></Typography>
                                </TableCell>
                            </TableRow>
                        </TableBody>
                    </Table>
                    <Menu open={toggleMenu} anchorEl={anchorEl} keepMounted open={openMenu}>
                        {props.rooms && props.rooms.map((r, i) =>
                            <MenuItem key={i}
                                      onClick={() => {
                                          setSelectedRoomIndex(i);
                                          toggleMenu()
                                      }}
                                      selected={i === selectedRoomIndex}>
                                {props.orgs && (props.orgs.filter(e => e.publicId === r.organization
                                )[0].name)+" - "+r.name}
                            </MenuItem>)}
                    </Menu>
                </Grid>
                <Grid container>
                    {console.log(props.slots)}
                    {props.slots && props.slots.length > 0 && (
                        <Table size="small">
                            <TableBody>
                                {props.slots.map((e, i) =>
                                    <TableRow key={i}>
                                        <TableCell align="center">
                                            {i+1}
                                        </TableCell>
                                        <TableCell align="center">
                                            {e.from.substring(11, 19) + " - " + e.to.substring(11, 19)}
                                        </TableCell>
                                        <TableCell>
                                            <MyButton label="Book" onClick={()=>submitBooking(e)}/>
                                        </TableCell>
                                    </TableRow>
                                )}</TableBody>
                        </Table>)}
                </Grid>
                <Grid container justify='center' style={{ width: '100%' ,marginTop:'10px'}}>
                    <Grid item>
                        <MyButton label='Cancel' width='10em' onClick={returnHander} />
                    </Grid>
                </Grid>
            </Grid>
        </Grid>
    )
}

const mapStateToProps = (state) => {
    return {
        authToken: state.authReducer.authToken,
        rooms: state.roomSearchReducer.rooms,
        slots: state.getAvailableTimeslotReducer.slots,
        orgs: state.searchOrgReducer.organizations
    }
}

export default connect(mapStateToProps, {
    searchRoom,
    createBooking,
    getAvailableTimeslotByRoom,
    searchOrg,searchBooking
})(requireAuth(layout2(CreateBooking)));