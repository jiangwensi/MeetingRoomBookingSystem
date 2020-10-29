import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router } from 'react-router-dom';
import { Provider } from 'react-redux';
import reduxThunk from 'redux-thunk';
import { createStore, applyMiddleware, compose } from 'redux';

import App from 'components/App';
import reducers from 'myRedux/reducers';

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
const store = createStore(
	reducers,
	{
		authReducer: {
			// userId: sessionStorage.getItem('userId'),
			authToken: sessionStorage.getItem('authToken'),
			roles: sessionStorage.getItem('roles'),
			username: sessionStorage.getItem('username'),
			myOrgs: sessionStorage.getItem('myOrgs'),
			myRooms: sessionStorage.getItem('myRooms')
		}
	},
	composeEnhancers(applyMiddleware(reduxThunk))
);

ReactDOM.render(
	<Provider store={store}>
		<Router>
			<App />
		</Router>
	</Provider>,
	document.querySelector('#root')
);
