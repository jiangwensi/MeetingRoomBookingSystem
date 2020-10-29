import React, {useState} from 'react';
import {connect} from 'react-redux';

import {makeStyles, useTheme} from '@material-ui/core/styles';
import useMediaQuery from '@material-ui/core/useMediaQuery';
import Button from '@material-ui/core/Button';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';
import TextField from '@material-ui/core/TextField';
import {validateEmail, validatePasswordMatch, validatePassword} from 'utils/validation';

import {resetPassword} from 'myRedux/actions/auth';
import {useEffect} from 'react';
import {ROUTE_SIGN_IN} from 'utils/const';
import {MyButton, StatusMessage} from 'components/element';

const useStyles = makeStyles((theme) => ({
    button: {
        color: 'white',
        marginTop: '1em',
        marginBottom: '1em',
        '&:hover': {
            backgroundColor: theme.palette.primary.main
        }
    },
    errorBox: {
        backgroundColor: theme.palette.secondary.light
    },
    successBox: {
        backgroundColor: theme.palette.primary.light
    }
}));

const ResetPassword = (props) => {
    const classes = useStyles();
    const [email, setEmail] = useState('');
    const [emailHelper, setEmailHelper] = useState('');
    const [oldPassword, setOldPassword] = useState('');
    const [oldPasswordHelper, setOldPasswordHelper] = useState('');
    const [password, setPassword] = useState('');
    const [passwordHelper, setPasswordHelper] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [confirmPasswordHelper, setConfirmPasswordHelper] = useState('');

    const theme = useTheme();
    const matchesXS = useMediaQuery(theme.breakpoints.down('xs'));

    function checkOldPassword() {
        if (validatePassword(oldPassword)) {
            setOldPasswordHelper('');
            return true;
        } else {
            setOldPasswordHelper('Password must be at least 8 characters long.');
            return false;
        }
    }

    function checkPassword() {
        if (validatePassword(password)) {
            setPasswordHelper('');
            return true;
        } else {
            setPasswordHelper('Password must be at least 8 characters long.');
            return false;
        }
    }

    function checkPasswordMatch() {
        if (validatePasswordMatch(password, confirmPassword)) {
            setConfirmPasswordHelper('');
            return true;
        } else {
            setConfirmPasswordHelper("Password and confirm password doesn't match.");
            return false;
        }
    }

    useEffect(() => {
        if (props.status === 'success') {
            setTimeout(() => {
                props.history.push(ROUTE_SIGN_IN);
            }, 2000);
        }
    }, [props.status]);

    useEffect(() => {
        clearText();
    }, [props.message, props.errorMessage]);

    useEffect(() => {
        if (allFieldsEmpty()) {
            return;
        }
        checkPasswordMatch();
    }, [password, confirmPassword]);

    useEffect(() => {
        if (allFieldsEmpty()) {
            return;
        }
        checkPassword();
    }, [password]);

    useEffect(() => {
        if (allFieldsEmpty()) {
            return;
        }
        checkOldPassword();
    }, [oldPassword]);

    useEffect(() => {
        if (allFieldsEmpty()) {
            return;
        }
        checkEmail();
    }, [email]);

    const resetPasswordHandler = () => {
        const validEmail = checkEmail();
        const validOldPassword = checkOldPassword();
        const validPassword = checkPassword();
        const passwordMatch = checkPasswordMatch();
        if (validEmail && validOldPassword && validPassword && passwordMatch) {
            props.resetPassword(email, oldPassword, password);
        }
    };

    const checkEmail = () => {
        if (validateEmail(email)) {
            setEmailHelper('');
            return true;
        } else {
            setEmailHelper('Email is not valid.');
            return false;
        }
    };

    const clearText = () => {
        setEmail('');
        setOldPassword('');
        setPassword('');
        setConfirmPassword('');

        setEmailHelper('');
        setOldPasswordHelper('');
        setPasswordHelper('');
        setConfirmPasswordHelper('');
    };

    const allFieldsEmpty = () => {
        return !email && !oldPassword && !password && !confirmPassword;
    };

    const backToLoginHandler = () => {
        props.history.push(ROUTE_SIGN_IN);
    };

    return (
        <Grid container
              direction='row'
              justify="center"
              style={{zIndex: 2000, marginTop: matchesXS ? '5em' : '10em'}}>
            <Grid item  xs={12} sm={10} md={6} lg={5} >
                <Grid container direction="column">
                    {props.message && (
                        <Grid item component={Box} >

                            <StatusMessage type='success' message={props.message} />
                        </Grid>
                    )}
                    {props.errorMessage && (
                        <Grid item component={Box}>
                            <StatusMessage type='failed' errorMessage={props.errorMessage} />
                        </Grid>
                    )}
                    <Grid item>
                        <Typography variant='h6' align='center'>
                            Reset Password
                        </Typography>
                    </Grid>
                    <Grid item>
                        <TextField
                            id='email'
                            error={emailHelper !== ''}
                            helperText={emailHelper}
                            variant='outlined'
                            fullWidth
                            margin='normal'
                            label='Email'
                            value={email}
                            onChange={(event) => {
                                setEmail(event.target.value);
                            }}
                        />
                        <TextField
                            id='oldPassword'
                            type='password'
                            error={oldPasswordHelper !== ''}
                            helperText={oldPasswordHelper}
                            variant='outlined'
                            fullWidth
                            margin='normal'
                            label='Old Password'
                            value={oldPassword}
                            onChange={(event) => {
                                setOldPassword(event.target.value);
                            }}
                        />
                        <TextField
                            id='password'
                            type='password'
                            error={passwordHelper !== ''}
                            helperText={passwordHelper}
                            variant='outlined'
                            fullWidth
                            margin='normal'
                            label='New Password'
                            value={password}
                            onChange={(event) => {
                                setPassword(event.target.value);
                            }}
                        />
                        <TextField
                            id='confirmPassword'
                            type='password'
                            error={confirmPasswordHelper !== ''}
                            helperText={confirmPasswordHelper}
                            variant='outlined'
                            fullWidth
                            margin='normal'
                            label='Confirm New Password'
                            value={confirmPassword}
                            onChange={(event) => {
                                setConfirmPassword(event.target.value);
                            }}
                        />
                    </Grid>

                    <Grid item style={{marginTop:"1em"}}>
                        <Grid container justify="space-between">
                            <Grid item>
                                <MyButton
                                    onClick={backToLoginHandler}
                                    label="Back To SignIn"
                                    width={"15em"}>
                                </MyButton>
                            </Grid>
                            <Grid item>
                                <MyButton
                                    label="Submit"
                                    onClick={resetPasswordHandler}
                                    width={"15em"}>
                                </MyButton>
                            </Grid>
                        </Grid>
                    </Grid>

                </Grid>

            </Grid>


            {/*</Grid>*/}
        </Grid>
    );
};

const mapStateToProps = (state) => {
    console.log(state.resetPasswordReducer.message)
    return {
        errorMessage: state.resetPasswordReducer.errorMessage,
        message: state.resetPasswordReducer.message,
        status: state.resetPasswordReducer.status
    };
}

export default connect(mapStateToProps, {resetPassword})(ResetPassword);
