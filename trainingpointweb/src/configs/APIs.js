import axios from "axios";
import cookie from "react-cookies";
// const BASE_URL = "http://localhost:8080/TrainingPointApp/";
// const BASE_URL = "http://localhost:8085/TrainingPointApp_war/";
// const BASE_URL = "http://18.141.12.170:8080/TrainingPointApp-1.0-SNAPSHOT/";
const BASE_URL = "/ConductPoints"
const PAY_PAL = "https://api-m.sandbox.paypal.com/";
const CLIENT_ID =
  "AdUJsj4oizEC1GPXkuRs25s-024YionolowWse7xtfDPoeAZpKSk6LE7YUyO0-JKCRtSxn-89QMrKk-F";
const CLIENT_SECRET =
  "EBv5s_OpIG4a95ytJyUu3JbtqgWpQExuQ0fDD8KKA02qCZ98gKj5B4uIYwmAOPx0Fo7e3If5wve5sazT";

export const endpoints = {
    'activity-list': "/activity/list/",
    'create-registration': "/registration/create/",
    'actitivty-report': "/activity/report",
    'activity-details': (activityId) => `/activity/${activityId}`,
    'activity-comments': (activityId) => `/activity/${activityId}/comments`,
    'activity-add-comment': (activityId) => `/activity/${activityId}/add-comment/`,
    'activity-like': (activityId) => `/activity/${activityId}/like/`,
    'user-registration': "/registration/user-registration/",
    'registration-delete': (registrationId) =>`/registration/${registrationId}`,
    'faculty-list': "/common/faculty-list/",
    'article-list': "/common/article-list/",
    'class-list': "/common/class-list/",
    'semester-list': "/common/semester-list/",
    'register': "/register/",
    'login': "/login/",
    'current-user': "/current-user/",
    'update-current-user': "/current-user/",
    'change-password': "/current-user/change-password/",
    'delete-comment': (commentId) => `/comment/${commentId}`,
    'update-comment': (commentId) => `/comment/${commentId}`,
    'like-comment': (commentId) => `/comment/${commentId}/like/`,
    'create-otp': '/otp/create/',
    'verify-otp': "/otp/verify/",
    'assistant-list': "/assistant-list/",
    'create-report': "/report/create/",
    'user-report': "/report/student-reports/",
    'vn-pay': "/payment/submitOrder",
    "paypal-genrate-access-token": "/v1/oauth2/token",
    "paypal-create-order": "/payment/paypal/create/",
    "paypal-capture-order": (orderId) =>  `/payment/paypal/capture/${orderId}`,
    "zalopay-create-order": "/payment/zalopay/create/"
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
