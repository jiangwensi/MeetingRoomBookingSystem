import React, {useState} from 'react';
import {connect} from 'react-redux';

import {makeStyles, useTheme} from '@material-ui/core/styles';
import useMediaQuery from '@material-ui/core/useMediaQuery';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';
import TextField from '@material-ui/core/TextField';

import {validateEmail} from 'utils/validation';
import {requestResetForgottenPassword} from 'myRedux/actions/auth';
import {MyButton, StatusMessage} from 'components/element';
import {ROUTE_SIGN_IN} from "../../utils/const";


const RequestResetForgottenPassword = (props) => {
    const [email, setEmail] = useState('');
    const [emailHelper, setEmailHelper] = useState('');

    const theme = useTheme();
    const matchesXS = useMediaQuery(theme.breakpoints.down('xs'));
    const requestResetPasswordHandler = () => {
        if (checkEmail()) {
            props.requestResetForgottenPassword(email);
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

    return (
        <Grid container
              alignItems='center'
              justify='center'
              style={{marginTop: matchesXS ? '5em' : '10em', zIndex: 2000}}>

            <Grid item  xs={12} sm={10} md={6} lg={5} xl={4} >
                <Grid container direction="column">
                    <Grid item>
                        <Typography variant='h6' align='center'>
                            Reset Forgotten Password
                        </Typography>
                    </Grid>
                    {props.message && (
                        <Grid item component={Box}>
                            <StatusMessage type="success" message={props.message}/>
                        </Grid>
                    )}
                    {props.errorMessage && (
                        <Grid item component={Box}>
                            <StatusMessage type="failed" errorMessage={props.errorMessage}/>
                        </Grid>
                    )}
                    <Grid item>
                        <TextField
                            id='email'
                            error={emailHelper != ''}
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
                    </Grid>
                </Grid>
                <Grid container justify="space-between" style={{marginTop: "1em"}}>
                    <MyButton
                        onClick={backToLoginHandler}
                        label="Back to Login"
                        width="15em">
                    </MyButton>
                    <MyButton
                        onClick={requestResetPasswordHandler} width="15em" label="Submit">
                    </MyButton>
                </Grid>
            </Grid>


        </Grid>
    );
};

const mapStateToProps = (state) => ({
    errorMessage: state.requestResetForgottenPasswordReducer.errorMessage,
    message: state.requestResetForgottenPasswordReducer.message
});

export default connect(mapStateToProps, {requestResetForgottenPassword})(RequestResetForgottenPassword);
