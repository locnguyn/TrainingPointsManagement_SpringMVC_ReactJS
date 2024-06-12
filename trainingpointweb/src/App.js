import "bootstrap/dist/css/bootstrap.min.css";
import { Container, Spinner } from "react-bootstrap";
import { BrowserRouter, Route, Routes, useLocation } from "react-router-dom";
import Header from "./components/Common/Header";
import Footer from "./components/Common/Footer";
import { Fragment, useReducer } from "react";
import "./App.css";
import Activity from "./components/Activity/Activity";
import Login from "./components/User/Login";
import Register from "./components/User/Register";
import Home from "./components/Common/Home";
import { MyDispatcherContext, MyUserContext } from "./configs/Contexts";
import MyUserReducer from "./components/Reducer/UserReducer";
import cookie from 'react-cookies'
import User from "./components/User/User";
import Report from "./components/Activity/ReportMissing";
import Password from "./components/User/Password";
import ActivityDetails from "./components/Activity/ActivityDetails";

function App() {
  const [user, dispatch] = useReducer(MyUserReducer, cookie.load("user") || null);
  return (
    <Container className="App">
      <BrowserRouter>
        <MyUserContext.Provider value={user}>
          <MyDispatcherContext.Provider value={dispatch}>
            <Header />
            {/* <Spinner /> */}
            <Routes>
              <Route path="/" element={<Home />}/>
              <Route path="/activity" element={<Activity/>}/>
              <Route path="/report_missing" element={<Report/>}/>
              <Route path="/activity/:activityId" element={<ActivityDetails/>}/>
              <Route path="/register" element={<Register />}/>
              <Route path="/login" element={<Login />} />
              <Route path="/current-user" element={<User />}/>
              <Route path="/change_password" element={<Password />} />
            </Routes>
            <Footer />
          </MyDispatcherContext.Provider>
        </MyUserContext.Provider>
      </BrowserRouter>
    </Container>
  );
}

export default App;
