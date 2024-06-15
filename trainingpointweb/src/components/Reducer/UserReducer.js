import cookie from 'react-cookies'

const MyUserReducer = (current, action) => {
    switch (action.type){
        case "login":
            return {
                userInfo: action.payload.resData,
                userRegistration: action.payload.regData
            };
        case "logout":
            cookie.remove("token");
            cookie.remove("user");

            return null;
        case  "update_user":
            return {
                userInfo: action.payload.resData,
                userRegistration: action.payload.regData
            };
    }
    return current;
}

export default MyUserReducer;