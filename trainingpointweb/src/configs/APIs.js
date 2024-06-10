import axios from "axios"
import cookie from "react-cookies";
const BASE_URL = 'http://localhost:8080/TrainingPointApp/';

export const endpoints = {
    'activites': "api/activities/",
    'register': "api/register/",
    'login': "api/login/",
    'current-user': "api/current-user/",
    'update-current-user': "api/current-user/",
    'change-password': "api/current-user/change-password/",
    'activity-participation-type': "api/activity-participation-type/list/"
}

export const authApi = () => {
    return axios.create({
        baseURL: BASE_URL,
        headers: {
            'Authorization': `${cookie.load("token")}`
        }
    });
}

export default axios.create({
    baseURL: BASE_URL
})
