import axios from 'axios';

// export const domain = "localhost";
// export const domain = "192.168.99.100";
// export const port="8080";
// export const domain = "mrbs.jiangwensi.com";
export const domain = "mrbs.jiangwensi.com/api";
export const port = "";

export default axios.create({
    baseURL: 'http://' + domain + '/',
    // headers: {
    // 	accept: 'application/json'
    // }
});
