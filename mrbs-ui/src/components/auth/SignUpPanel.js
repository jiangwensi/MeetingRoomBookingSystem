import React, {useState, useEffect} from 'react';
import {connect} from 'react-redux';

import {makeStyles} from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import TextField from '@material-ui/core/TextField';

import {signUp} from 'myRedux/actions/auth';
import {validateEmail, validatePasswordMatch, validatePassword, validateName} from 'utils/validation';
import {StatusMessage} from 'components/element';

const useStyles = makeStyles((theme) => ({
    button: {
        color: 'white',
        marginTop: '1em',
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

function SignUpPanel(props) {
    const classes = useStyles();
    const [password, setPassword] = useState('');
    const [passwordHelper, setPasswordHelper] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [confirmPasswordHelper, setConfirmPasswordHelper] = useState('');
    const [email, setEmail] = useState('');
    const [emailHelper, setEmailHelper] = useState('');
    const [name, setName] = useState('');
    const [nameHelper, setNameHelper] = useState('');

    const noError = () => {
        const validPassword = checkPassword();
        const validConfirmPassword = validatePasswordMatch();
        const validName = checkName();
        const validEmail = checkEmail();
        return validPassword && validConfirmPassword && validName && validEmail;
    };

    const signUpHandler = () => {
        if (noError()) {
            props.signUp(name, email, password);
        }
    };

    const clearText = () => {
        setName('');
        setEmail('');
        setPassword('');
        setConfirmPassword('');

        setNameHelper('');
        setEmailHelper('');
        setPasswordHelper('');
        setConfirmPasswordHelper('');
    };

    const allFieldsEmpty = () => {
        return !name && !email && !password && !confirmPassword;
    };

    function checkName() {
        if (validateName(name)) {
            setNameHelper('');
            return true;
        } else {
            setNameHelper('Name is not valid.');
            return false;
        }
    }

    function checkEmail() {
        if (validateEmail(email)) {
            setEmailHelper('');
            return true;
        } else {
            setEmailHelper('Email is not valid.');
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
        clearText();
    }, [props.signUpState]);

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
        if (allFieldsEmpty()) {
            return;
        }
        checkName();
    }, [name]);

    return (
        <Grid container justify="center" alignItems='center' spacing={3}>
            <Grid item  xs={12} sm={10} md={6} lg={5}>
                <Grid container direction="column">
                    {props.signUpState.message && (
                        <Grid
                            item
                            component={Box}>
                            <StatusMessage type="success" message={props.signUpState.message}/>
                        </Grid>
                    )}
                    {props.signUpState.errorMessage && (
                        <Grid
                            item
                            component={Box}>
                            <StatusMessage type="failed" errorMessage={props.signUpState.errorMessage}/>
                        </Grid>
                    )}
                    <Grid item component={Box} width='100%' textAlign='right'>
                        <TextField
                            id='name'
                            error={nameHelper !== ''}
                            helperText={nameHelper}
                            variant='outlined'
                            label='Name'
                            value={name}
                            fullWidth
                            margin='normal'
                            onChange={(event) => setName(event.target.value)}></TextField>
                        <TextField
                            id='email'
                            error={emailHelper !== ''}
                            helperText={emailHelper}
                            variant='outlined'
                            label='Email'
                            value={email}
                            fullWidth
                            margin='normal'
                            onChange={(event) => setEmail(event.target.value)}></TextField>
                        <TextField
                            type='password'
                            id='password'
                            error={passwordHelper !== ''}
                            helperText={passwordHelper}
                            value={password}
                            variant='outlined'
                            label='Password'
                            fullWidth
                            margin='normal'
                            onChange={(event) => {
                                setPassword(event.target.value);
                            }}></TextField>
                        <TextField
                            error={confirmPasswordHelper !== ''}
                            helperText={confirmPasswordHelper}
                            type='password'
                            id='confirmPassword'
                            value={confirmPassword}
                            variant='outlined'
                            label='Confirm Password'
                            fullWidth
                            margin='normal'
                            onChange={(event) => {
                                setConfirmPassword(event.target.value);
                            }}></TextField>
                        {/* <Grid container justify='center' alignItems='center'>
							<Avatar alt='Remy Sharp' className={classes.large} src='/static/images/avatar/1.jpg' />
						</Grid>
						<br />
						<Grid container justify='center' alignItems='center'>
							<Button variant='contained' color='primary' fullWidth className={classes.button}>
								Upload Avatar
							</Button>
						</Grid> */}
                        <Button
                            variant='contained'
                            color='primary'
                            fullWidth
                            className={classes.button}
                            onClick={() => signUpHandler()}
                            size='large'>
                            Sign Up
                        </Button>
                    </Grid>
                </Grid>
            </Grid>
        </Grid>
    );
}

const mapStateToProps = (state) => {
    return {authState: state.authReducer, signUpState: state.signUpReducer};
};

export default connect(mapStateToProps, {signUp})(SignUpPanel);
