import axios from "axios"
import cookie from "react-cookies";
const BASE_URL = 'http://localhost:8080/TrainingPointApp/';

export const endpoints = {
    'activity-list': "api/activity/list/",
    'activity-registration': "api/activity/registration/",
    'actitivty-report': "api/activity/report",
    'activity-details': (activityId) => `/api/activity/${activityId}`,
    'activity-add-comment': (activityId) => `/api/activity/${activityId}/add-comment/`,
    'faculty-list': "api/common/faculty-list/",
    'article-list': "api/common/article-list/",
    'register': "api/register/",
    'login': "api/login/",
    'current-user': "api/current-user/",
    'update-current-user': "api/current-user/",
    'change-password': "api/current-user/change-password/",
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
