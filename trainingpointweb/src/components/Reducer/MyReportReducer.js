import cookie from 'react-cookies'

const MyReportReducer = (current, action) =>{
    switch(action.type){
        case "initReports": {
            localStorage.setItem("userReports", JSON.stringify(action.payload));
            return action.payload;
        }
        case "update-reports":return action.payload;
            
    }

    return current;
}

export default MyReportReducer;