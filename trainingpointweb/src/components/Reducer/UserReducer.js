import cookie from 'react-cookies'

const MyUserReducer = (current, action) => {
    switch (action.type){
        case "login":
            return action.payload;
        case "logout":
            cookie.remove("token");
            cookie.remove("user");
            return null;
        case  "update_user":
            return action.payload;
    }
    return current;
}

export default MyUserReducer;