import React, { useState } from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';

import { ThemeProvider } from '@material-ui/core/styles';

import MyHeader from 'components/layout/MyHeader';
import MyDrawer from 'components/layout/MyDrawer';
import SignInSignUp from 'components/auth/SignInSignUp';
import RequestResetForgottenPassword from 'components/auth/RequestResetForgottenPassword';
import ResetForgottenPassword from 'components/auth/ResetForgottenPassword';
import ResetPassword from 'components/auth/ResetPassword';
import WithSessionTimeout from 'components/auth/WithSessionTimeout';
import VerifyEmail from 'components/auth/VerifyEmail';

import NotFoundPage from 'components/PageNotFound';
import myTheme from 'Theme';

import * as ROUTE_CONST from 'utils/const';

const App = () => {
	const [openMenu, setOpenMenu] = useState(false);
	// const [openMobileMenu, setOpenMobileMenu] = useState(false);

	return (
		<ThemeProvider theme={myTheme}>
			<BrowserRouter>
				<Route
					path='/'
					render={(props) => (
						<MyHeader
							{...props}
							setOpenMenu={setOpenMenu}
							openMenu={openMenu}
							// setOpenMobileMenu={setOpenMobileMenu}
							// openMobileMenu={openMobileMenu}
						/>
					)}
				/>
				<Route
					path='/'
					render={(props) => (
						<MyDrawer
							{...props}
							setOpenMenu={setOpenMenu}
							openMenu={openMenu}
							// setOpenMobileMenu={setOpenMobileMenu}
							// openMobileMenu={openMobileMenu}
						/>
					)}
				/>
				<Switch>
					<Route
						path={`${ROUTE_CONST.ROUTE_SIGN_IN_SIGN_UP}/:action`}
						exact
						render={(props) => <SignInSignUp {...props} action={props.match.params.action} />}
					/>
					<Route path={ROUTE_CONST.ROUTE_VERIFY_EMAIL} exact component={VerifyEmail} />
					<Route
						path={ROUTE_CONST.ROUTE_REQUEST_RESET_FORGOTTEN_PASSWORD}
						exact
						component={RequestResetForgottenPassword}
					/>
					<Route path={ROUTE_CONST.ROUTE_RESET_FORGOTTEN_PASSWORD} exact component={ResetForgottenPassword} />
					<Route path={ROUTE_CONST.ROUTE_RESET_PASSWORD} exact component={ResetPassword} />

					<Route
						path={[
							ROUTE_CONST.ROUTE_HOME,
							ROUTE_CONST.ROUTE_CANLENDAR,
							ROUTE_CONST.ROUTE_USER_SEARCH,
							ROUTE_CONST.ROUTE_PROFILE,

							ROUTE_CONST.ROUTE_ORG_SEARCH,
							ROUTE_CONST.ROUTE_ORG_CREATE,
							ROUTE_CONST.ROUTE_ORG_VIEW,
							ROUTE_CONST.ROUTE_ORG_EDIT,

							ROUTE_CONST.ROUTE_ROOM_CREATE,
							ROUTE_CONST.ROUTE_ROOM_SEARCH,
							ROUTE_CONST.ROUTE_ROOM_VIEW,
							ROUTE_CONST.ROUTE_ROOM_EDIT,

							ROUTE_CONST.ROUTE_BOOKING_CREATE,
							ROUTE_CONST.ROUTE_BOOKING_EDIT,
							ROUTE_CONST.ROUTE_BOOKING_VIEW,
							ROUTE_CONST.ROUTE_BOOKING_SEARCH
						]}
						exact
						component={WithSessionTimeout}
					/>
					<Route component={NotFoundPage} />
				</Switch>
			</BrowserRouter>
		</ThemeProvider>
	);
};

export default App;
