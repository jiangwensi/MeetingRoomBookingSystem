import React, {useState, useEffect} from 'react';
import {connect} from 'react-redux';
import {makeStyles, useTheme} from '@material-ui/core/styles';
import useMediaQuery from '@material-ui/core/useMediaQuery';
import Button from '@material-ui/core/Button';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import SignInPanel from 'components/auth/SignInPanel';
import SignUpPanel from 'components/auth/SignUpPanel';
import layout1 from 'components/layout/layout1';
import {signOut} from 'myRedux/actions/auth';
import {Grid} from "@material-ui/core";
// import TabPanel from '@material-ui/lab/TabPanel';

const useStyles = makeStyles((theme) => ({
    header: {
        marginTop: '5em',
        marginBottom: '0.5em',
        color: theme.palette.text.primary
    },
    colorPrimary: {
        backgroundColor: theme.palette.success.main
    },
    tab: {
        fontWeight: 'bold',
        width: '50%',
        maxWidth: '1000px'
    },
    textColorInherit: {
        opacity: 1
    },
    inputLabelGrid: {
        marginRight: '1em'
    }
}));

function MyTab(props) {
    const classes = useStyles();
    const theme = useTheme();
    const {label, setSelectedIndex, selectedIndex, value} = props;
    return (
        <Tab
            component={Button}
            label={label}
            value={value}
            onClick={() => setSelectedIndex(value)}
            classes={{root: classes.tab}}
            style={{
                backgroundColor: selectedIndex !== value ? theme.palette.grey[100] : theme.palette.common.white
            }}></Tab>
    );
}

function SignInSignUp(props) {
    // const [selectedIndex, setSelectedIndex] = useState(useParams().action === 'signup' ? 1 : 0);
    const [selectedIndex, setSelectedIndex] = useState(props.action === 'signup' ? 1 : 0);
    const theme = useTheme();
    const matchesXS = useMediaQuery(theme.breakpoints.down('xs'));
    const matchesSM = useMediaQuery(theme.breakpoints.down('sm'));
    const matchesMD = useMediaQuery(theme.breakpoints.down('md'));

    useEffect(() => {
        props.signOut();
    }, []);

    return (
        // <Grid container justify='center' style={{ zIndex: 2000 }}>
        // 	<Box
        // 		style={{ marginTop: matchesXS ? '5em' : '10em' }}
        // 		boxShadow={1}
        // 		borderRadius={15}
        // 		width={matchesXS ? '100%' : matchesSM ? '75%' : matchesMD ? '50%' : '30%'}>
        <>
            <div style={{width: '100%', paddingX: '0', margin: '3em 0'}}>
                <Grid container justify="center" alignItems='center' spacing={3}>
                    <Grid item  xs={12} sm={10} md={6} lg={5}>
                        <Grid container direction="column">
                            <Tabs value={selectedIndex} indicatorColor='primary' TabIndicatorProps={{style: {width: '50%'}}}>
                                <MyTab label='Sign In' index={0} setSelectedIndex={setSelectedIndex} selectedIndex={selectedIndex}/>
                                <MyTab label='Sign Up' index={1} setSelectedIndex={setSelectedIndex} selectedIndex={selectedIndex}/>
                            </Tabs>
                        </Grid>
                    </Grid>
                </Grid>
            </div>
            <div style={{width: '100%', paddingX: '0', margin: '3em 0'}} role='tabpanel' hidden={0 !== selectedIndex}>
                <SignInPanel {...props} />
            </div>
            <div style={{width: '100%', paddingX: '0', margin: '3em 0'}} role='tabpanel' hidden={1 !== selectedIndex}>
                <SignUpPanel {...props} />
            </div>
        </>
        // 	</Box>
        // </Grid>
    );
}

export default connect(null, {signOut})(layout1(SignInSignUp));
