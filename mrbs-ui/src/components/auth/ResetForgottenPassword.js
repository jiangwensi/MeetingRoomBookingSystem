import React, {useState} from 'react';
import {connect} from 'react-redux';

import {makeStyles, useTheme} from '@material-ui/core/styles';
import useMediaQuery from '@material-ui/core/useMediaQuery';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';
import TextField from '@material-ui/core/TextField';
import {validateEmail, validatePasswordMatch, validatePassword} from 'utils/validation';

import {resetForgottenPassword} from 'myRedux/actions/auth';
import {useEffect} from 'react';
import {ROUTE_SIGN_IN} from 'utils/const';
import {MyButton, StatusMessage} from 'components/element';

const ResetForgottenPassword = (props) => {
    const [email, setEmail] = useState('');
    const [emailHelper, setEmailHelper] = useState('');
    const [password, setPassword] = useState('');
    const [passwordHelper, setPasswordHelper] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [confirmPasswordHelper, setConfirmPasswordHelper] = useState('');

    const theme = useTheme();
    const matchesXS = useMediaQuery(theme.breakpoints.down('xs'));

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

        checkEmail();
    }, [email]);

    useEffect(() => {
        if (props.status === 'success') {
            setTimeout(() => {
                props.history.push(ROUTE_SIGN_IN);
            }, 2000);
        }
    }, [props.status]);

    const resetPasswordHandler = () => {
        if (checkEmail() && checkPassword() && checkPasswordMatch()) {
            const token = new URLSearchParams(props.location.search).get('token');
            // const token = props.match.params.token;
            props.resetForgottenPassword(email, password, token);
        }
    };

    const backToLoginHandler = () => {
        props.history.push(ROUTE_SIGN_IN);
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
        setPassword('');
        setConfirmPassword('');

        setEmailHelper('');
        setPasswordHelper('');
        setConfirmPasswordHelper('');
    };

    const allFieldsEmpty = () => {
        return !email && !password && !confirmPassword;
    };

    return (
        <Grid container
              justify='center'
              alignItems='center'
              style={{zIndex: 2000, marginTop: matchesXS ? '5em' : '10em'}}>

            <Grid item>
                <Grid container direction="column">
                    <Grid item >
                        <Typography variant='h6' align='center'>
                            Reset Forgotten Password
                        </Typography>
                    </Grid>
                    {props.message && (
                        <Grid item component={Box} >
                            <StatusMessage type="success" message={props.message}/>
                        </Grid>
                    )}
                    {props.errorMessage && (
                        <Grid item component={Box} >
                            <StatusMessage type="failed" errorMessage={props.errorMessage}/>
                        </Grid>
                    )}
                    <Grid item >
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
                            id='password'
                            type='password'
                            error={passwordHelper !== ''}
                            helperText={passwordHelper}
                            variant='outlined'
                            fullWidth
                            margin='normal'
                            label='Password'
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
                            label='Confirm Password'
                            value={confirmPassword}
                            onChange={(event) => {
                                setConfirmPassword(event.target.value);
                            }}
                        />
                    </Grid>
                </Grid>
                <Grid item>
                    <Grid container direction="row" justify="space-between" style={{marginTop:"1em"}}>
                        <MyButton
                            onClick={backToLoginHandler}
                            label="Back to Login"
                            width="15em">
                        </MyButton>
                        <MyButton
                            onClick={resetPasswordHandler}
                            label="Reset Password"
                            width="15em">
                        </MyButton>
                    </Grid>
                </Grid>
            </Grid>
        </Grid>
    );
};

const mapStateToProps = (state) => ({
    errorMessage: state.resetForgottenPasswordReducer.errorMessage,
    message: state.resetForgottenPasswordReducer.message,
    status: state.resetPasswordReducer.status
});

export default connect(mapStateToProps, {resetForgottenPassword})(ResetForgottenPassword);
