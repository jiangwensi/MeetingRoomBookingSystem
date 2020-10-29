import React, { Component} from 'react';
import {TextField, Grid, Table, TableBody, TableRow, TableCell, Typography, Menu, MenuItem} from '@material-ui/core';
import {connect} from 'react-redux';
import withStyles from "@material-ui/core/styles/withStyles";

const useStyles = theme => ({
    MuiTableCell: {
        borderBottom: "none"
    }
});

class SearchBar extends Component {
    classes = this.props.classes;
    parent = this.props.parent;

    render = () => (
        <Grid container justify='space-evenly'>
            <Grid item xs={12} md={8}>

                <Table>
                    <TableBody>
                        <TableRow>
                            <TableCell classes={{root: this.classes.MuiTableCell}}><Typography
                                variant={"h6"}>Room:</Typography></TableCell>
                            <TableCell classes={{root: this.classes.MuiTableCell}} onClick={event => {
                                this.parent.toggleMenu(event);
                            }}>
                                <Typography
                                    variant={"h6"}>{this.parent.state.selectedRoomIndex === -1?"---- Please Select a Room ----": ( this.parent.props.rooms && this.parent.props.rooms.length > 0 && this.parent.props.orgs && (this.parent.props.orgs.filter(e => e.publicId === this.parent.props.rooms[this.parent.state.selectedRoomIndex].organization
                                )[0].name))}
                                </Typography>
                                <Typography
                                    variant={"h6"}>{this.parent.state.selectedRoomIndex > -1 && this.parent.props.rooms && this.parent.props.rooms.length > 0 && this.parent.props.orgs && this.parent.props.rooms[this.parent.state.selectedRoomIndex].name}
                                </Typography>

                            </TableCell>
                            <TableCell classes={{root: this.classes.MuiTableCell}}><Typography
                                variant={"h6"}>Date:</Typography></TableCell>
                            <TableCell classes={{root: this.classes.MuiTableCell}}>
                                <Typography variant={"h6"}>
                                    <TextField type="date" value={this.parent.state.selectedDate} onChange={(event) => this.parent.setState({selectedDate: event.target.value})}/>
                                    <span onClick={()=>this.parent.setState({selectedDate:''})}>&nbsp;x</span>
                                </Typography>
                            </TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
                <Menu open={this.parent.toggleMenu} anchorEl={this.parent.state.anchorEL} keepMounted open={this.parent.state.openMenu}>
                    <MenuItem onClick={() => {
                        this.parent.setState({selectedRoomIndex: -1});
                        this.parent.toggleMenu()
                    }}
                              selected={this.parent.state.selectedRoomIndex === -1}>
                        ---- &nbsp;Please Select a Room&nbsp; ----
                    </MenuItem>
                    {this.parent.props.rooms && this.parent.props.rooms.map((r, i) =>
                        <MenuItem key={i}
                                  onClick={() => {
                                      this.parent.setState({selectedRoomIndex: i});
                                      this.parent.toggleMenu()
                                  }}
                                  selected={i === this.parent.state.selectedRoomIndex}>
                            {this.parent.props.orgs && (this.parent.props.orgs.filter(o => {
                                return o.publicId === r.organization
                            })[0]?.name) + " - " + r.name}
                        </MenuItem>)}
                </Menu>
            </Grid>
        </Grid>
    );
}


export default connect()(withStyles(useStyles)(SearchBar));
