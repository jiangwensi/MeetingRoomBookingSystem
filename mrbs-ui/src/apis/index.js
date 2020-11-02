import axios from 'axios';

// export const domain = "localhost";
// export const domain = "192.168.99.100";
// export const port="8080";
// export const domain = "mrbs.jiangwensi.com";
// prod aws elb
export const backendDomain = "mrbs.jiangwensi.com/api";
export const frontEndDomain = "mrbs.jiangwensi.com";

// dev docker
// export const domain = "192.168.99.100/api";
export const port = "";

export default axios.create({
    baseURL: 'http://' + backendDomain + '/',
    // headers: {
    // 	accept: 'application/json'
    // }
});
