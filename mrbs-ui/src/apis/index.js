import axios from 'axios';

export const domain = "192.168.99.100";

export default axios.create({
	baseURL: 'http://'+domain+':8080/',
	// headers: {
	// 	accept: 'application/json'
	// }
});
