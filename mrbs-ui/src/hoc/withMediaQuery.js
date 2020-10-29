import React from 'react';
import useMediaQuery from '@material-ui/core/useMediaQuery';

// const withMediaQuery = (...args) => {
// 	return (Component) => {
// 		return (props) => {
// 			const mediaQuery = useMediaQuery(...args);
// 			return <Component mediaQuery={mediaQuery} {...props} />;
// 		};
// 	};
// };

// const withMediaQuery = (...args) => (Component) => (props) => {
// 	const mediaQuery = useMediaQuery(...args);
// 	return <Component mediaQuery={mediaQuery} {...props} />;
// };

//https://stackoverflow.com/a/61560866
const withMediaQuery = (queries = []) => (Component) => (props) => {
	const mediaProps = {};
	queries.forEach((q) => {
		mediaProps[q[0]] = useMediaQuery(q[1]);
	});
	// const mediaQuery = useMediaQuery(...args);
	return <Component {...mediaProps} {...props} />;
};

export default withMediaQuery;
