import React from 'react';

import requireAuth from 'hoc/requireAuth';
import layout1 from 'components/layout/layout1';

function Home(props) {
	return <div style={{ marginTop: '10em' }}>Welcome to Home</div>;
}

export default requireAuth(layout1(Home));
