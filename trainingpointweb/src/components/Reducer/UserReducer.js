import cookie from 'react-cookies'

const MyUserReducer = (current, action) => {
    switch (action.type){
        case "login":
            cookie.save("user", action.payload);
            return action.payload;
        case "logout":
            cookie.remove("token");
            cookie.remove("user");
            localStorage.removeItem("userActivities");
            localStorage.removeItem("userReports");
            return null;
        case  "update_user":
            return action.payload;
    }
    return current;
}

export default MyUserReducer;