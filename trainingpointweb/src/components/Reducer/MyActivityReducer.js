import cookie from "react-cookies";

const MyActivityReducer = (current, action) => {
  switch (action.type) {
    case "initActivities": {
      localStorage.setItem("userActivities", JSON.stringify(action.payload));
      return action.payload;
    }
    case "update-activities":
      return action.payload;
  }

  return current;
};

export default MyActivityReducer;
