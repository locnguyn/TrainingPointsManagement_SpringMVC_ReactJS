import axios from "axios";
import cookie from "react-cookies";
const BASE_URL = "http://localhost:8080/TrainingPointApp/";
const PAY_PAL = "https://api-m.sandbox.paypal.com/";
const CLIENT_ID =
  "AdUJsj4oizEC1GPXkuRs25s-024YionolowWse7xtfDPoeAZpKSk6LE7YUyO0-JKCRtSxn-89QMrKk-F";
const CLIENT_SECRET =
  "EBv5s_OpIG4a95ytJyUu3JbtqgWpQExuQ0fDD8KKA02qCZ98gKj5B4uIYwmAOPx0Fo7e3If5wve5sazT";

export const endpoints = {
    'activity-list': "api/activity/list/",
    'create-registration': "api/registration/create/",
    'actitivty-report': "api/activity/report",
    'activity-details': (activityId) => `/api/activity/${activityId}`,
    'activity-add-comment': (activityId) => `/api/activity/${activityId}/add-comment/`,
    'activity-like': (activityId) => `/api/activity/${activityId}/like/`,
    'user-registration': "api/registration/user-registration/",
    'registration-delete': (registrationId) =>`api/registration/${registrationId}`,
    'faculty-list': "api/common/faculty-list/",
    'article-list': "api/common/article-list/",
    'class-list': "api/common/class-list/",
    'semester-list': "api/common/semester-list/",
    'register': "api/register/",
    'login': "api/login/",
    'current-user': "api/current-user/",
    'update-current-user': "api/current-user/",
    'change-password': "api/current-user/change-password/",
    'delete-comment': (commentId) => `api/comment/${commentId}`,
    'update-comment': (commentId) => `api/comment/${commentId}`,
    'like-comment': (commentId) => `api/comment/${commentId}/like/`,
    'create-otp': '/api/otp/create/',
    'verify-otp': "/api/otp/verify/",
    'assistant-list': "/api/assistant-list/",
    'create-report': "api/report/create/",
    'user-report': "/api/report/student-reports/",
    'vn-pay': "/api/payment/submitOrder",
    "paypal-genrate-access-token": "/v1/oauth2/token",
    "paypal-create-order": "api/payment/paypal/create/",
    "paypal-capture-order": (orderId) =>  `api/payment/paypal/capture/${orderId}`
}

export const authApi = () => {
  return axios.create({
    baseURL: BASE_URL,
    headers: {
      Authorization: `${cookie.load("token")}`,
    },
  });
};

export const paypal = () => {
  return axios.create({
    baseURL: PAY_PAL,
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
    },
    auth: {
      username: CLIENT_ID,
      password: CLIENT_SECRET,
    },
    params: {
      grant_type: "client_credentials",
    },
  });
};

export default axios.create({
  baseURL: BASE_URL,
});
