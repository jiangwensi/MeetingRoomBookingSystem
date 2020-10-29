import React, {useState, useEffect} from 'react';
import {connect} from 'react-redux';
import layout2 from 'components/layout/layout2';
import requireAuth from 'hoc/requireAuth';
import {DeleteOutline, AddCircleOutlineOutlined} from '@material-ui/icons';
import {
    Grid,
    Typography,
    TextField,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableRow,
    Paper,
    makeStyles,
} from '@material-ui/core';
import {MyButton, InputRadioButton, StatusMessage, MyTextField} from 'components/element';
import {validateOrgName, validateEmail} from 'utils/validation';
import {viewOrg, listOrgAdmins, editOrg} from 'myRedux/actions/org';
import {searchUser} from 'myRedux/actions/user';
import {ROUTE_ORG_SEARCH} from 'utils/const';

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
const EditOrg = (props) => {
    const classes = useStyles();
    const [orgName, setOrgName] = useState('');
    const [description, setDescription] = useState('');
    const [active, setActive] = useState(null);
    const [admins, setAdmins] = useState([]);

    const [orgNameHelper, setOrgNameHelper] = useState('');

    const [newAdminEmail, setNewAdminEmail] = useState('');
    const [newAdminEmailHelper, setNewAdminEmailHelper] = useState('');

    const returnHander = () => {
        props.history.goBack();
    };
    const skipValidation = () => {
        return !orgName && !description;
    };

    useEffect(() => {
        if (!skipValidation() && !validateOrgName(orgName)) {
            setOrgNameHelper('Invalid Org Name');
        } else {
            setOrgNameHelper('');
        }
    }, [orgName]);

    useEffect(() => {
        const param = new URLSearchParams(window.location.search);
        props.viewOrg(props.authToken, param.get('orgId'));
        props.listOrgAdmins(props.authToken, param.get('orgId'));
    }, []);

    useEffect(() => {
        setOrgName(props.name);
    }, [props.name]);

    useEffect(() => {
        setDescription(props.description);
    }, [props.description]);

    useEffect(() => {
        setActive(props.active);
    }, [props.active]);

    useEffect(() => {
        setAdmins(props.admins);
    }, [props.admins]);

    const submitHandler = () => {
        props.editOrg(
            props.authToken,
            orgName,
            props.publicId,
            description,
            active,
            admins,
            props.history.push(ROUTE_ORG_SEARCH)
        );
    };

    const addNewAdmin = () => {
        if (!validateEmail(newAdminEmail)) {
            setNewAdminEmailHelper('Email is invalid');
        } else {
            props.searchUser(props.authToken, null, newAdminEmail);
        }
    };

    useEffect(() => {
        if (props.searchUserStatus === 'success') {
            if (props.searchedUser === undefined) {
                setNewAdminEmailHelper('Unable to find a user with this email');
            } else {
                let adminExists = false;
                if (admins !== null && admins.length > 0) {
                    admins.forEach((a) => {
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
                    setAdmins([...admins, newAdmin]);
                    // admins.push(newAdmin);
                }
            }
        } else if (props.searchUserStatus === 'failed') {
            setNewAdminEmailHelper('Unable to find user, please try again later');
        }
    }, [props.searchedUser, props.searchUserStatus, props.searchUserErrorMessage]);

    const deleteAdminHandler = (e) => {
        if (admins !== null && admins.length > 0) {
            setAdmins(admins.filter((a) => a.publicId !== e.publicId));
        }
    };
    return (
        <Grid item xs={12} sm={11} md={10} lg={9} xl={8}>
            <Grid container style={{marginBottom: '3em'}} justify='center'>
                <Typography align='center' variant='h5'>
                    Edit Organization Details
                </Typography>
            </Grid>
            {props.errorMessage && (
                <Grid container alignItems='center' justify='space-around' style={{marginBottom: '1em'}}>
                    <Grid item xs={8}>
                        <StatusMessage type='failed' errorMessage={props.errorMessage}/>
                    </Grid>
                </Grid>
            )}
            {props.updateOrgErrorMessage && (
                <Grid container alignItems='center' justify='space-around' style={{marginBottom: '1em'}}>
                    <Grid item xs={8}>
                        <StatusMessage type='failed' errorMessage={props.updateOrgErrorMessage}/>
                    </Grid>
                </Grid>
            )}

            <Grid container alignItems='center' justify='space-around' style={{marginBottom: '1em'}}>
                <Grid item xs={8}>
                    <MyTextField
                        fullWidth
                        label='Org Name'
                        helperText={orgNameHelper}
                        error={orgNameHelper !== ''}
                        value={orgName}
                        onChange={(event) => setOrgName(event.target.value)}
                    />
                </Grid>
                <Grid item xs={8} style={{marginTop: '1em'}}>
                    <MyTextField
                        label='Description'
                        value={description}
                        fullWidth
                        onChange={(event) => setDescription(event.target.value)}
                    />
                </Grid>
                <Grid item xs={8} style={{marginTop: '1em'}}>
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
            {admins && admins.length > 0 && (
                <Grid container alignItems='center' justify='center' style={{marginBottom: '1em', marginTop: '2em'}}>
                    <Grid item xs={8}>
                        <Typography variant='h6'>Organization Administrator</Typography>
                    </Grid>
                </Grid>
            )}
            <Grid container alignItems='center' justify='center' style={{marginBottom: '1em'}}>
                {props.searchUserErrorMessage && (
                    <Grid container alignItems='center' justify='space-around' style={{marginBottom: '1em'}}>
                        <Grid item xs={8}>
                            {props.searchUserErrorMessage}
                        </Grid>
                    </Grid>
                )}
                {props.searchUserErrorMessage && (
                    <Grid container alignItems='center' justify='space-around' style={{marginBottom: '1em'}}>
                        <Grid item xs={8}>
                            {props.searchUserErrorMessage}
                        </Grid>
                    </Grid>
                )}
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
            {admins &&
            admins.length > 0 &&
            admins.map((e) => (
                <Grid
                    container
                    alignItems='center'
                    justify='space-around'
                    style={{marginBottom: '1em'}}
                    key={e.publicId}>
                    <Grid item xs={8}>
                        <TableContainer component={Paper}>
                            <Table>
                                <TableBody>
                                    {/* <TableRow>
											<TableCell>Public ID</TableCell>
											<TableCell>{e.publicId}</TableCell>
											
										</TableRow> */}
                                    <TableRow>
                                        <TableCell>Name</TableCell>
                                        <TableCell>{e.name}</TableCell>
                                        <TableCell classes={{root: classes.tableCellNoBorder}}>
                                            <DeleteOutline
                                                fontSize='large'
                                                className={classes.hover}
                                                onClick={() => deleteAdminHandler(e)}
                                            />
                                        </TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>Email</TableCell>
                                        <TableCell>{e.email}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>Active</TableCell>
                                        <TableCell>{e.active ? 'Yes' : 'No'}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell>Email Verified</TableCell>
                                        <TableCell>{e.emailVerified ? 'Yes' : 'No'}</TableCell>
                                    </TableRow>
                                </TableBody>
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
                            <MyButton label='Submit' width='10em' onClick={submitHandler}/>
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
        status: state.viewOrgReducer.status,
        errorMessage: state.viewOrgReducer.errorMessage,
        publicId: state.viewOrgReducer.publicId,
        name: state.viewOrgReducer.name,
        active: state.viewOrgReducer.active,
        description: state.viewOrgReducer.description,
        admins: state.listOrgAdminsReducer.admins,

        searchedUser:
            state.searchUserReducer.users && state.searchUserReducer.users.length > 0
                ? state.searchUserReducer.users[0]
                : undefined,
        searchUserErrorMessage: state.searchUserReducer.errorMessage,
        searchUserStatus: state.searchUserReducer.status,
        updateOrgStatus: state.editOrgReducer.status,
        updateOrgErrorMessage: state.editOrgReducer.errorMessage
    };
};

export default connect(mapStateToProps, {viewOrg, listOrgAdmins, searchUser, editOrg})(
    requireAuth(layout2(EditOrg))
);
